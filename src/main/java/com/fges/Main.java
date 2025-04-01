package com.fges;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        System.exit(exec(args));
    }

    public static int exec(String[] args) throws IOException {
        Options cliOptions = new Options();
        CommandLineParser parser = new DefaultParser();

        cliOptions.addRequiredOption("s", "source", true, "File containing the grocery list");

        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            return 1;
        }

        String fileName = cmd.getOptionValue("s");

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        String command = positionalArgs.get(0);

        // Création d'une instance de gestion de liste

        GererListe gererListe;
        try {
            gererListe = new GererListe(fileName);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
            return 1;
        }



        // Traitement des commandes
        try {
            switch (command) {
                case "add" -> {

                    if (positionalArgs.size() < 3) {
                        System.err.println("Missing arguments");
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
                    try {
                        gererListe.ajouter(itemName, quantity);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                        return 1;
                    }
                }
                case "list" -> {
                    gererListe.afficher();
                    return 0;
                }
                case "remove" -> {
                    if (positionalArgs.size() < 2) {
                        System.err.println("Missing arguments");
                        return 1;
                    }

                    String itemName = positionalArgs.get(1);

                    if (positionalArgs.size() >= 3) {
                        // Retrait d'une quantité spécifique
                        try {
                            int quantity = Integer.parseInt(positionalArgs.get(2));
                            boolean success = gererListe.réduireQuantité(itemName, quantity);
                            if (!success) {
                                System.err.println("Impossible de réduire la quantité: article non trouvé ou quantité invalide");
                                return 1;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("La quantité doit être un nombre entier");
                            return 1;
                        }
                    } else {
                        // Suppression complète de l'article
                        boolean success = gererListe.enlever(itemName);
                        if (!success) {
                            System.err.println("Impossible de supprimer l'article: article non trouvé");
                            return 1;
                        }
                    }
                    return 0;
                }
                default -> {
                    System.err.println("Unknown command: " + command);
                    return 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier: " + e.getMessage());
            return 1;
        }
        return 0;
    }
}