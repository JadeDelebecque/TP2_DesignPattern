package com.fges.commands;

import com.fges.GererListe;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;

// Classe abstraite pour les commandes nécessitant un fichier source

public abstract class FileRequiredCommand extends AbstractCommand {

    public FileRequiredCommand(String commandName) {
        super(commandName);
    }

    @Override
    protected void configureOptions() {
        // Ajoute l'option -s (source) obligatoire
        options.addRequiredOption("s", "source", true, "File containing the grocery list");
    }

    @Override
    public int execute(String[] args) throws Exception {
        // Parse les arguments
        CommandLine cmd = parseArguments(args);
        if (cmd == null) {
            return 1;
        }

        // Récupère le chemin du fichier
        String filePath = cmd.getOptionValue("s");

        // Crée un gestionnaire de liste
        GererListe gererListe;
        try {
            gererListe = new GererListe(filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
            return 1;
        }

        // Exécute la commande spécifique
        return executeWithFile(gererListe, cmd);
    }

    /**
     * Exécute la commande avec accès au gestionnaire de liste
     * @param gererListe Gestionnaire de liste initialisé
     * @param cmd Arguments de ligne de commande parsés
     * @return Code de retour (0 pour succès, autre pour erreur)
     * @throws Exception En cas d'erreur pendant l'exécution
     */
    protected abstract int executeWithFile(GererListe gererListe, CommandLine cmd) throws Exception;
}