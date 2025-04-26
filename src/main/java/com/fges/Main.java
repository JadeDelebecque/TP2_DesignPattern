package com.fges;

import com.fges.commands.Command;
import com.fges.commands.CommandFactory;

import java.io.IOException;

/**
 * Classe principale de l'application
 * Maintenant simplifiée pour se concentrer sur le routage des commandes
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.exit(exec(args));
    }

    /**
     * Exécute l'application avec les arguments fournis
     * @param args Arguments de la ligne de commande
     * @return Code de retour (0 pour succès, autre pour erreur)
     */
    public static int exec(String[] args) {
        if (args.length == 0) {
            System.err.println("Commande manquante");
            CommandFactory.displayHelp();
            return 1;
        }

        String commandName = args[0];

        try {
            // Créer et exécuter la commande appropriée
            Command command = CommandFactory.createCommand(commandName);
            return command.execute(args);
        } catch (IllegalArgumentException e) {
            // Commande inconnue
            System.err.println(e.getMessage());
            CommandFactory.displayHelp();
            return 1;
        } catch (IOException e) {
            // Erreur d'entrée/sortie
            System.err.println("Erreur d'entrée/sortie: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            // Autre erreur
            System.err.println("Erreur: " + e.getMessage());
            return 1;
        }
    }
}