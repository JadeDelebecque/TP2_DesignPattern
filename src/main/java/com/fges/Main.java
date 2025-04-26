package com.fges;

import com.fges.Commande.CommandeManager;
import com.fges.File.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.ArrayList;
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
        // Vérifier si la commande est "info"
        boolean isInfoCommand = false;
        for (String arg : args) {
            if (arg.equals("info")) {
                isInfoCommand = true;
                break;
            }
        }

        Options cliOptions = new Options();
        CommandLineParser parser = new DefaultParser();

        // Pour la commande "info", l'option -s est optionnelle
        if (isInfoCommand) {
            cliOptions.addOption("s", "source", true, "File containing the grocery list");
        } else {
            cliOptions.addRequiredOption("s", "source", true, "File containing the grocery list");
        }

        cliOptions.addOption("c", "category", true, "Category of the item");

        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            throw new IllegalArgumentException("Parse erreur : erreur au parsement de la ligne de commande");
        }

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            throw new IllegalArgumentException("Erreur : Commande manquante");
        }

        String command = positionalArgs.get(0);
        String category = cmd.getOptionValue("category");

        // Initialiser le fichier
        File file = new File();
        if (command.equals("info") && !cmd.hasOption("s")) {
            // Pour la commande info sans fichier spécifié, utiliser un fichier temporaire
            file.formatAFile("dummy.json");
        } else {
            String fileName = cmd.getOptionValue("s");
            file.formatAFile(fileName);
        }

        // Création du CommandeManager
        CommandeManager commandeManager = new CommandeManager(file);

        // Extraction et envoi des arguments de commande
        List<String> commandArgs = new ArrayList<>(positionalArgs);
        commandArgs.remove(0); // On retire le nom de la commande

        commandeManager.executeCommand(command, commandArgs, category);
        return 0;
    }
}