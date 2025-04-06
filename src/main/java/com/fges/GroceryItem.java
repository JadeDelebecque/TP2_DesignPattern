package com.fges;


public class GroceryItem {
    private String name;
    private int quantity;
    private String category;

    public GroceryItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.category = "default";
    }

    public GroceryItem(String name, int quantity, String category) {
        this.name = name;
        this.quantity = quantity;
        this.category = category != null ? category : "default";
    }

    public GroceryItem() {
        this.category = "default";  // Valeur par défaut
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

    public void addQuantity(int quantity){
        if (quantity > 0){
            this.quantity += quantity;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category != null ? category : "default";
    }

    @Override
    public String toString() {
        return name + ": " + quantity;
    }
}