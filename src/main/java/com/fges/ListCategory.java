package com.fges;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Classe pour gérer l'affichage des catégories
 */
public class ListCategory {
    private File file;


    public ListCategory(File file) {
        this.file = file;
    }

    public void listByCategory() throws IOException {
        Map<String, GroceryItem> items = file.entrée();

        if (items.isEmpty()) {
            System.out.println("La liste est vide.");
            return;
        }

        // Regrouper les éléments par catégorie
        Map<String, Map<String, GroceryItem>> categorizedItems = new TreeMap<>();

        for (GroceryItem item : items.values()) {
            String category = item.getCategory();
            if (!categorizedItems.containsKey(category)) {
                categorizedItems.put(category, new HashMap<>());
            }
            categorizedItems.get(category).put(item.getName(), item);
        }

        // Afficher les éléments par catégorie
        for (String category : categorizedItems.keySet()) {
            System.out.println("# " + category + ":");
            for (GroceryItem item : categorizedItems.get(category).values()) {
                System.out.println(item);
            }
        }
    }
}