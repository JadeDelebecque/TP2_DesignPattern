package com.fges;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonFormat implements FileFormat {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Map<String, GroceryItem> read(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Map<String, GroceryItem> items = new HashMap<>();

        if (Files.exists(path)) {
            try {
                String fileContent = Files.readString(path);
                if (fileContent.trim().isEmpty()) {
                    return items; // Return empty map for empty file
                }

                List<String> itemStrings = OBJECT_MAPPER.readValue(fileContent, new TypeReference<List<String>>() {});

                for (String itemString : itemStrings) {
                    String[] parts = itemString.split(":");
                    if (parts.length >= 2) {
                        String name = parts[0].trim();
                        int quantity = Integer.parseInt(parts[1].trim());
                        items.put(name, new GroceryItem(name, quantity));
                    }
                }
            } catch (Exception e) {
                // Log error and return empty map
                System.err.println("Impossible de lire le fichier " + e.getMessage());
            }
        }

        return items;
    }

    @Override
    public void write(String filePath, Map<String, GroceryItem> items) throws IOException {
        List<String> itemStrings = items.values().stream()
                .map(GroceryItem::toString)
                .collect(Collectors.toList());

        File outputFile = new File(filePath);
        OBJECT_MAPPER.writeValue(outputFile, itemStrings);
    }
}