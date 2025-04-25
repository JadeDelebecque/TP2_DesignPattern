package com.fges.Commande;

import com.fges.File.File;
import com.fges.GroceryItem;

import java.io.IOException;

/**
 * Une classe qui s'occupe de l'affichage de la Liste d'Item
 */

public class DisplayList {
    private File file;

    public DisplayList(File file){
        this.file = file;
    }

    public void displayGroceryList() throws IOException {
        var groceryList = file.loadFile();

        if (groceryList.isEmpty()) {
            throw new IllegalArgumentException("La liste est vide");
        } else {
            for (GroceryItem item : groceryList.values()) {
                System.out.println(item);
            }
        }
    }
}
