package com.fges;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

                try {
                    List<Map<String, Object>> itemsList = OBJECT_MAPPER.readValue(fileContent,
                            new TypeReference<List<Map<String, Object>>>() {});

                    for (Map<String, Object> itemMap : itemsList) {
                        if (itemMap.containsKey("name") && itemMap.containsKey("quantity")) {
                            String name = (String) itemMap.get("name");
                            int quantity = Integer.parseInt(itemMap.get("quantity").toString());
                            String category = itemMap.containsKey("category") ?
                                    (String) itemMap.get("category") : "default";

                            items.put(name, new GroceryItem(name, quantity, category));
                        }
                    }
                    return items;
                } catch (Exception e) {
                    // Si le nouveau format échoue, essayer l'ancien format
                    List<String> itemStrings = OBJECT_MAPPER.readValue(fileContent, new TypeReference<List<String>>() {});

                    for (String itemString : itemStrings) {
                        String[] parts = itemString.split(":");
                        if (parts.length >= 2) {
                            String name = parts[0].trim();
                            int quantity = Integer.parseInt(parts[1].trim());
                            items.put(name, new GroceryItem(name, quantity, "default"));
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Impossible de lire le fichier: " + e.getMessage());
            }
        }

        return items;
    }

    @Override
    public void write(String filePath, Map<String, GroceryItem> items) throws IOException {
        List<Map<String, Object>> itemsList = new ArrayList<>();

        for (GroceryItem item : items.values()) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("name", item.getName());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("category", item.getCategory());
            itemsList.add(itemMap);
        }

        File outputFile = new File(filePath);
        OBJECT_MAPPER.writeValue(outputFile, itemsList);
    }
}