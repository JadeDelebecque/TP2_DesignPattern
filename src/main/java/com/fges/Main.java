package com.fges;

import com.fges.CLI.CommandContext;
import com.fges.CLI.CommandLineHandler;
import com.fges.CLI.CommandOptions;
import com.fges.Commande.CommandeManager;
import com.fges.File.File;
import com.fges.Web.SimpleGroceryShop;
import fr.anthonyquere.GroceryShopServer;
import fr.anthonyquere.MyGroceryShop;

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

        // Vérifier si la commande est "server"
        if (args.length > 0 && "server".equals(args[0])) {
            // Lancer le serveur web
            int port = 8080;
            if (args.length > 1) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Port invalide, utilisation du port 8080 par défaut");
                }
            }

            startServer(port);
            return 0;
        }

        // Traitement des autres commandes comme avant
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
        if (command.equals("info") && !cmdHandler.hasOption("s")) {
            file.formatAFile("dummy.json");
        } else {
            String fileName = cmdHandler.getOptionValue("s");

            String formatOption = cmdHandler.hasOption("f") ?
                    cmdHandler.getOptionValue("f") :
                    cmdHandler.getOptionValue("format");

            if (formatOption != null) {
                file.formatAFileWithSpecifiedFormat(fileName, formatOption);
            } else {
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

    /**
     * Démarre le serveur web d'épicerie
     * @param port Port sur lequel démarrer le serveur
     */
    private static void startServer(int port) {
        MyGroceryShop groceryShop = new SimpleGroceryShop();
        GroceryShopServer server = new GroceryShopServer(groceryShop);
        server.start(port);

        System.out.println("Serveur d'épicerie démarré sur http://localhost:" + port);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}