package com.fges.commands;

import com.fges.GererListe;
import com.fges.RemoveElement;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.fges.GroceryItem;

// Commande pour supprimer un article de la liste d'épicerie

public class RemoveCommand extends FileRequiredCommand {

    public RemoveCommand() {
        super("remove");
    }

    @Override
    protected int executeWithFile(GererListe gererListe, CommandLine cmd) throws IOException {
        List<String> positionalArgs = cmd.getArgList();

        // Vérifier qu'il y a suffisamment d'arguments (nom de commande + nom article)
        if (positionalArgs.size() < 2) {
            System.err.println("Arguments manquants");
            System.err.println(getUsage());
            return 1;
        }

        String itemName = positionalArgs.get(1);

        if (positionalArgs.size() >= 3) {
            // Retrait d'une quantité spécifique
            try {
                int quantity = Integer.parseInt(positionalArgs.get(2));
                boolean success = RemoveElement.removeQuantité(gererListe.getItems(), itemName, quantity);
                if (!success) {
                    System.err.println("Impossible de réduire la quantité: article non trouvé ou quantité invalide");
                    return 1;
                }
                gererListe.sauvegarder();
            } catch (NumberFormatException e) {
                System.err.println("La quantité doit être un nombre entier");
                return 1;
            }
        } else {
            // Suppression complète de l'article
            boolean success = RemoveElement.remove(gererListe.getItems(), itemName);
            if (!success) {
                System.err.println("Impossible de supprimer l'article: article non trouvé");
                return 1;
            }
            gererListe.sauvegarder();
        }

        return 0;
    }

    @Override
    public String getUsage() {
        return "Usage: remove <nom_article> [quantité] -s <fichier_source>";
    }
}