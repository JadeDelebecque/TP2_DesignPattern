package com.fges;


/**
 * Classe qui definit l'objet GroceryItem
 */
public class GroceryItem {
    private String name;
    private int quantity;
    private String category;

    public GroceryItem() {
        this.quantity = 0;
        this.category = "default";
    }

    public GroceryItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.category = "default";
    }

    public GroceryItem(String name, int quantity, String category) {
        this.name = name;
        this.quantity = quantity;
        setCategory(category); // Utilise la méthode setCategory pour appliquer la même logique
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        // Vérifie si la catégorie est null, vide ou ne contient que des espaces
        if (category == null || category.trim().isEmpty()) {
            this.category = "default";
        } else {
            this.category = category;
        }
    }

    public void addQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity += quantity;
        }
    }

    @Override
    public String toString() {
        return name + ": " + quantity;
    }
}