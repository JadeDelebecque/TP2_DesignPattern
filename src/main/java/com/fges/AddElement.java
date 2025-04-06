package com.fges;

import java.io.IOException;
import java.util.Map;

/**
 * Classe responsable d'ajouter des éléments à la liste d'épicerie
 */
public class AddElement {
    private File file;


    public AddElement(File file){
        this.file = file;
    }


    public void ajouterElement(String name, int quantity, String category) throws IOException {
        var items = file.entrée();

        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à zéro");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'article ne peut pas être vide");
        }

        // Si aucune catégorie n'est spécifiée, utiliser "default"
        String finalCategory = (category == null || category.trim().isEmpty()) ? "default" : category;

        boolean trouve = false;
        for (String key : items.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                GroceryItem item = items.get(key);
                item.setQuantity(item.getQuantity() + quantity);
                // Mettre à jour la catégorie
                item.setCategory(finalCategory);
                trouve = true;
                break;
            }
        }

        if (!trouve) {
            items.put(name, new GroceryItem(name, quantity, finalCategory));
        }

        file.sortie(items);
    }
    g
    public void ajouterElement(String name, int quantity) throws IOException {
        ajouterElement(name, quantity, "default");
    }
}