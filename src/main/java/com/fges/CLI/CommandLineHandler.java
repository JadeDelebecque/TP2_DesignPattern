package com.fges.CLI;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyse la ligne de commande et extrait les informations utiles
 */
public class CommandLineHandler {
    private final String[] args;
    private CommandLine parsedCommand;

    public CommandLineHandler(String[] args) {
        this.args = args;
    }

    /**
     * Vérifie si un argument correspond à une commande spécifique
     * @param commandName Nom de la commande à vérifier
     * @return true si la commande existe dans les arguments
     */
    public boolean hasCommand(String commandName) {
        for (String arg : args) {
            if (arg.equals(commandName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Analyse les arguments avec les options définies
     * @param options Options de ligne de commande
     * @return true si le parsing est réussi
     * @throws IllegalArgumentException En cas d'erreur de parsing
     */
    public boolean parse(CommandOptions options) throws IllegalArgumentException {
        CommandLineParser parser = new DefaultParser();
        try {
            parsedCommand = parser.parse(options.getOptions(), args);
            return true;
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Erreur de parsing: " + ex.getMessage());
        }
    }

    /**
     * Obtient la commande principale (premier argument positif)
     * @return Nom de la commande ou null si aucune commande
     */
    public String getCommand() {
        if (parsedCommand != null) {
            List<String> positionalArgs = parsedCommand.getArgList();
            if (!positionalArgs.isEmpty()) {
                return positionalArgs.get(0);
            }
        }
        throw new IllegalArgumentException("Commande manquante");
    }

    /**
     * Obtient les arguments de la commande (sans le nom de commande)
     * @return Liste d'arguments
     */
    public List<String> getCommandArgs() {
        List<String> commandArgs = new ArrayList<>(parsedCommand.getArgList());
        if (!commandArgs.isEmpty()) {
            commandArgs.remove(0); // Retire le nom de la commande
        }
        return commandArgs;
    }

    /**
     * Obtient la valeur d'une option
     * @param option Nom de l'option
     * @return Valeur de l'option ou null si non spécifiée
     */
    public String getOptionValue(String option) {
        return parsedCommand.getOptionValue(option);
    }

    /**
     * Vérifie si une option est présente
     * @param option Nom de l'option
     * @return true si l'option est présente
     */
    public boolean hasOption(String option) {
        return parsedCommand.hasOption(option);
    }
}