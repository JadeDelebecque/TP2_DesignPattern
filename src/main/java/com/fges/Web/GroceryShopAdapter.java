package com.fges.Web;

import com.fges.File.File;
import com.fges.GroceryItem;
import fr.anthonyquere.MyGroceryShop;
import fr.anthonyquere.Runtime;
import fr.anthonyquere.WebGroceryItem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroceryShopAdapter implements MyGroceryShop {

    private final File file;

    public GroceryShopAdapter(File file) {
        this.file = file;
    }

    @Override
    public List<WebGroceryItem> getGroceries() {
        try {
            Map<String, GroceryItem> groceries = file.loadFile();
            List<WebGroceryItem> result = new ArrayList<>();

            for (GroceryItem item : groceries.values()) {
                result.add(new WebGroceryItem(
                        item.getName(),
                        item.getQuantity(),
                        item.getCategory()
                ));
            }

            return result;
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des données: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void addGroceryItem(String name, int quantity, String category) {
        try {
            Map<String, GroceryItem> groceries = file.loadFile();

            if (groceries.containsKey(name)) {
                GroceryItem existingItem = groceries.get(name);
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                groceries.put(name, new GroceryItem(name, quantity, category));
            }

            file.saveFile(groceries);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ajout d'un élément: " + e.getMessage());
        }
    }

    @Override
    public void removeGroceryItem(String name) {
        try {
            Map<String, GroceryItem> groceries = file.loadFile();
            groceries.remove(name);
            file.saveFile(groceries);
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression d'un élément: " + e.getMessage());
        }
    }

    @Override
    public Runtime getRuntime() {
        return new Runtime(
                LocalDate.now(),
                System.getProperty("java.version"),
                System.getProperty("os.name")
        );
    }
}