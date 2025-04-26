package com.fges.commands;

import com.fges.GererListe;
import org.apache.commons.cli.CommandLine;

// Commande pour afficher la liste d'épicerie

public class ListCommand extends FileRequiredCommand {

    public ListCommand() {
        super("list");
    }

    @Override
    protected int executeWithFile(GererListe gererListe, CommandLine cmd) {
        gererListe.afficher();
        return 0;
    }

    @Override
    public String getUsage() {
        return "Usage: list -s <fichier_source>";
    }
}