package com.fges;

import java.util.Map;

public class AddElement {
    public static boolean ajouterElement(Map<String, GroceryItem> items, String name, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        for (String key : items.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                GroceryItem item = items.get(key);
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }
        items.put(name, new GroceryItem(name, quantity));
        return true;
    }
    public static boolean addSiNonExistant(Map<String, GroceryItem> items, String name, int quantity) {
        if (quantity <= 0 || items.containsKey(name)) {
            return false;
        }

        items.put(name, new GroceryItem(name, quantity));
        return true;
    }
}