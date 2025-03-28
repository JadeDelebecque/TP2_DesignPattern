package com.fges;


public class GroceryItem {
    private String name;
    private int quantity;

    public GroceryItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public GroceryItem() {
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

    @Override
    public String toString() {
        return name + ": " + quantity;
    }
}