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

public class Main {

    public static void main(String[] args) throws IOException {
        System.exit(exec(args));
    }

    public static int exec(String[] args) throws IOException {
        Options cliOptions = new Options();
        CommandLineParser parser = new DefaultParser();

        cliOptions.addRequiredOption("s", "source", true, "File containing the grocery list");
        cliOptions.addOption("c", "category", true, "Category of the item");

        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            throw new IllegalArgumentException("Parse erreur : erreur au parsement de la ligne de commande");
        }

        String fileName = cmd.getOptionValue("s");
        String category = cmd.getOptionValue("category");

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            throw new IllegalArgumentException("Erreur : Commande manquante");
        }

        String command = positionalArgs.get(0);
        File file = new File();
        file.formatAFile(fileName);

        // Creation du commande Manager
        CommandeManager commandeManager = new CommandeManager(file);

        // Extraction et Envoie de la commande
        List<String> commandArgs = new ArrayList<>(positionalArgs);
        commandArgs.remove(0); // On retire le nom pour pas l'executer

        commandeManager.executeCommand(command, commandArgs, category);
        return 0;
    }
}