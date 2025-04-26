package com.fges.commands;

import org.apache.commons.cli.*;

// Classe abstraite de base pour les commandes

public abstract class AbstractCommand implements Command {
    // Nom de la commande
    protected final String commandName;

    // Options CLI communes à toutes les commandes
    protected Options options;

    public AbstractCommand(String commandName) {
        this.commandName = commandName;
        this.options = new Options();
        configureOptions();
    }


    protected void configureOptions() {
    }

    /**
     * Parse les arguments pour cette commande
     * @param args Arguments de la ligne de commande
     * @return CommandLine parsée ou null en cas d'erreur
     */
    protected CommandLine parseArguments(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Erreur lors du parsing des arguments: " + e.getMessage());
            System.err.println(getUsage());
            return null;
        }
    }

    @Override
    public boolean canHandle(String commandName) {
        return this.commandName.equals(commandName);
    }

    @Override
    public String getUsage() {
        return "Usage: " + commandName + " [options]";
    }
}