package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import com.fges.GroceryItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Classe pour gérer la suppression d'éléments de la liste
 */
public class RemoveElement implements Command {

    private final File file;

    public RemoveElement(File file) {
        this.file = file;
    }

    @Override
    public void execute(List<String> args, CommandContext context) throws IOException {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Nom du produit requis pour la commande remove");
        }

        String name = args.get(0);

        if (args.size() == 1) {
            // Suppression complète
            remove(name);
        } else {
            // Suppression partielle
            try {
                int quantity = Integer.parseInt(args.get(1));
                removeQuantity(name, quantity);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La quantité doit être un nombre");
            }
        }
    }

    /**
     * Supprime complètement un élément de la liste
     */
    public void remove(String name) throws IOException {
        Map<String, GroceryItem> itemsList = file.loadFile();
        GroceryItem deletion = itemsList.remove(name);

        if (deletion == null) {
            throw new IllegalArgumentException("Le produit '" + name + "' n'existe pas");
        }

        file.saveFile(itemsList);
    }

    /**
     * Retire une quantité précise d'un item
     */
    public void removeQuantity(String name, int quantity) throws IOException {
        Map<String, GroceryItem> itemsList = file.loadFile();

        if (!itemsList.containsKey(name)) {
            throw new IllegalArgumentException("Le produit '" + name + "' n'existe pas");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantité à retirer doit être positive");
        }

        GroceryItem item = itemsList.get(name);
        int newQuantity = item.getQuantity() - quantity;

        if (newQuantity <= 0) {
            itemsList.remove(name);
        } else {
            item.setQuantity(newQuantity);
        }

        file.saveFile(itemsList);
    }
}