package com.fges;

import java.io.IOException;
import java.util.Map;

public class AddElement {
    private File file;
    public AddElement(File file){
        this.file = file;
    }
    public void ajouterElement(String name, int quantity) throws IOException {
        var items = file.entrée();

        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à zéro");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'article ne peut pas être vide");
        }

        for (String key : items.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                GroceryItem item = items.get(key);
                item.setQuantity(item.getQuantity() + quantity);
            }
        }
        items.put(name, new GroceryItem(name, quantity));
        file.sortie(items);
    }
}