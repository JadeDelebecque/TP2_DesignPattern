package com.fges;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvFormat implements FileFormat {

    @Override
    public Map<String, GroceryItem> read(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Map<String, GroceryItem> items = new HashMap<>();

        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);

            // Ignorer l'en-tête si présente
            boolean isFirstLine = true;

            for (String line : lines) {
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.contains("Nom") || line.contains("name") || line.contains("Quantité") || line.contains("quantity")) {
                        continue;
                    }
                }

                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String name = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    String category = (parts.length >= 3) ? parts[2].trim() : "default";
                    items.put(name, new GroceryItem(name, quantity, category));
                }
            }
        }

        return items;
    }

    @Override
    public void write(String filePath, Map<String, GroceryItem> items) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Écrire l'en-tête
            writer.write("Nom,Quantité,Catégorie\n");

            // Écrire les articles
            for (GroceryItem item : items.values()) {
                writer.write(item.getName() + "," + item.getQuantity() + "," + item.getCategory() + "\n");
            }
        }
    }
}