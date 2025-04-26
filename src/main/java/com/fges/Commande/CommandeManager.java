package com.fges.Commande;

import com.fges.Commande.AddElement;
import com.fges.Commande.ListCategory;
import com.fges.Commande.RemoveElement;
import com.fges.File.File;
import java.io.IOException;
import java.util.List;

/**
 * Une classe qui recoit une ligne de commande et les arguments et
 * qui va creer l'instance de la commande appelée
 */

public class CommandeManager {
    private File file;

    public CommandeManager(File file) {
        this.file = file;
    }

    public void executeCommand(String command, List<String> args, String category) throws IOException {
        try {
            switch (command) {
                case "add" -> {
                    handleAddCommand(args, category);
                    break;
                }
                case "list" -> {
                    handleListCommand();
                    break;
                }
                case "remove" -> {
                    handleRemoveCommand(args);
                    break;
                }
                case "info" -> {
                    handleInfoCommand();
                    break;
                }
                default -> {
                    throw new IllegalArgumentException("Commande Inconnu");
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier: " + e.getMessage());
            throw new IllegalArgumentException("Erreur lors de l'ecriture du fichier");

        }
    }

    public void handleInfoCommand() {
        InfoCommand infoCommand = new InfoCommand();
        infoCommand.displaySystemInfo();
    }

    public void  handleAddCommand(List<String> args, String category) throws IOException {
        if (args.size() < 2) {
            throw new IllegalArgumentException("Arguments manquants");
        }

        String itemName = args.get(0);
        int quantity;
        try {
            quantity = Integer.parseInt(args.get(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Entier requis, la quantité doit etre un entier");
        }

        AddElement addElement = new AddElement(file);
        try {
            addElement.addItemInGrocery(itemName, quantity, category);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            throw new IllegalArgumentException("Erreur lors du add");
        }
    }

    public void handleListCommand() throws IOException {
        ListCategory listCategory = new ListCategory(file);
        listCategory.listByCategory();
    }

    public void handleRemoveCommand(List<String> args) throws IOException {
        if (args.isEmpty()) {
            System.err.println("Missing arguments");
            throw new IllegalArgumentException("Arguments manquants");
        }

        String itemName = args.get(0);
        RemoveElement removeElement = new RemoveElement(file);

        if (args.size() >= 2) {
            // Retrait d'une quantité spécifique
            try {
                int quantity = Integer.parseInt(args.get(1));
                try {
                    removeElement.removeQuantity(itemName, quantity);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                    throw new IllegalArgumentException("Erreur lors du remove de la quantite");
                }
            } catch (NumberFormatException e) {
                System.err.println("La quantité doit être un nombre entier");
                throw new IllegalArgumentException("Entier requis");
            }
        } else {
            // Suppression complète de l'article
            try {
                removeElement.remove(itemName);
            } catch (IllegalArgumentException e) {
                System.err.println("Impossible de supprimer l'article: " + e.getMessage());
                throw new IllegalArgumentException("Erreur lors de la suppression de l'article");

            }
        }
    }
}
