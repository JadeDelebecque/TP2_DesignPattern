package com.fges.commands;

import com.fges.AddElement;
import com.fges.GererListe;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.fges.GroceryItem;

//Commande pour ajouter un article à la liste d'épicerie

public class AddCommand extends FileRequiredCommand {

    public AddCommand() {
        super("add");
    }

    @Override
    protected int executeWithFile(GererListe gererListe, CommandLine cmd) throws IOException {
        List<String> positionalArgs = cmd.getArgList();

        // Vérifier qu'il y a suffisamment d'arguments (nom de commande + nom article + quantité)
        if (positionalArgs.size() < 3) {
            System.err.println("Arguments manquants");
            System.err.println(getUsage());
            return 1;
        }

        String itemName = positionalArgs.get(1);
        int quantity;

        try {
            quantity = Integer.parseInt(positionalArgs.get(2));
        } catch (NumberFormatException e) {
            System.err.println("La quantité doit être un nombre entier");
            return 1;
        }

        // On réutilise la logique existante dans AddElement
        Map<String, GroceryItem> items = gererListe.getItems();
        boolean success = AddElement.ajouterElement(items, itemName, quantity);

        if (success) {
            gererListe.sauvegarder();
            return 0;
        } else {
            System.err.println("Impossible d'ajouter l'article: quantité invalide");
            return 1;
        }
    }

    @Override
    public String getUsage() {
        return "Usage: add <nom_article> <quantité> -s <fichier_source>";
    }
}