package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import com.fges.GroceryItem;

import java.io.IOException;
import java.util.List;

/**
 * Une classe qui s'occupe de l'affichage de la Liste d'Item
 */

public class DisplayList implements Command{
    private final File file;

    public DisplayList(File file){
        this.file = file;
    }

    public void execute(List<String> args, CommandContext context) throws IOException {
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
