package com.fges;

import com.fges.CLI.CommandContext;
import com.fges.CLI.CommandLineHandler;
import com.fges.CLI.CommandOptions;
import com.fges.Commande.CommandeManager;
import com.fges.File.File;

import java.io.IOException;
import java.util.List;

/**
 * Classe principale de l'application qui gère les commandes en ligne
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.exit(exec(args));
    }

    /**
     * Exécute la commande spécifiée par les arguments de ligne de commande
     * @param args Arguments de ligne de commande
     * @return Code de retour (0 pour succès)
     * @throws IOException En cas d'erreur d'E/S
     */
    public static int exec(String[] args) throws IOException {
        // Vérifier si des arguments existent
        if (args.length == 0) {
            System.err.println("Missing Command");
            throw new IllegalArgumentException("Erreur : Commande manquante");
        }
        // Initialiser le gestionnaire d'arguments
        CommandLineHandler cmdHandler = new CommandLineHandler(args);
        CommandOptions options = new CommandOptions();

        // On verifie si on a besoin de configuration pour la commande
        String potentialCommand = args.length > 0 ? args[0] : "";
        options.configureForCommand(potentialCommand);

        CommandContext context = new CommandContext();

        // Parser les arguments
        try {
            cmdHandler.parse(options);
        } catch (IllegalArgumentException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            return 1;
        }

        // Récupérer la commande et la catégorie
        String command = cmdHandler.getCommand();
        context.setCategory(cmdHandler.getOptionValue("category"));

        // Initialiser le fichier
        File file = new File();

        if (!cmdHandler.requiresSourceFile(command) && !cmdHandler.hasOption("s")) {
            // Pour les commandes ne nécessitant pas de fichier source, utiliser un fichier temporaire
            file.formatAFile("dummy.json");
        } else {
            String fileName = cmdHandler.getOptionValue("s");

            // Check for format option (both short and long form)
            String formatOption = cmdHandler.hasOption("f") ?
                    cmdHandler.getOptionValue("f") :
                    cmdHandler.getOptionValue("format");

            if (formatOption != null) {
                // If format is specified via command line, use it
                file.formatAFileWithSpecifiedFormat(fileName, formatOption);
            } else {
                // Otherwise, determine format from file extension
                file.formatAFile(fileName);
            }
        }

        // Création du CommandeManager avec le registre de commandes
        CommandeManager commandeManager = new CommandeManager(file);

        // Récupérer les arguments de commande et exécuter
        List<String> commandArgs = cmdHandler.getCommandArgs();

        try {
            commandeManager.executeCommand(command, commandArgs, context);
            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return 1;
        } catch (IOException e) {
            System.err.println("Erreur d'E/S: " + e.getMessage());
            return 1;
        }
    }
}