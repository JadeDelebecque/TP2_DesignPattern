package com.fges.CLI;

import com.fges.File.File;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Gère la définition des options de ligne de commande
 */
public class CommandOptions {
    private Options options;
    private boolean sourceRequired;
    private String commandName;

    public CommandOptions() {
        this.options = new Options();
        this.sourceRequired = true;
        initializeOptions();
    }

    /**
     * Initialise les options de base
     */
    private void initializeOptions() {
        // Option source avec requirement dynamique
        Option sourceOption = Option.builder("s")
                .longOpt("source")
                .hasArg(true)
                .desc("File containing the grocery list")
                .required(sourceRequired)
                .build();

        Option categoryOption = Option.builder("c")
                .longOpt("category")
                .hasArg(true)
                .desc("Category of the item")
                .required(false)
                .build();

        Option formatOption = Option.builder("f")
                .longOpt("format")
                .hasArg(true)
                .desc("File format (csv, json, etc.)")
                .required(false)
                .build();

        options.addOption(sourceOption);
        options.addOption(categoryOption);
        options.addOption(formatOption);
    }

    public void configureForCommand(String commandName) {
        this.commandName = commandName;
        if ("info".equals(commandName)) {
            setSourceRequired(false);
        } else {
            setSourceRequired(true);
        }
    }

    /**
     * Change si l'option source est requise
     * @param required true si l'option doit être requise
     */
    public void setSourceRequired(boolean required) {
        this.sourceRequired = required;
        // Créer un nouvel objet Options au lieu d'essayer de vider l'existant
        this.options = new Options();
        initializeOptions();
    }

    /**
     * Obtient les options configurées
     * @return L'object Options à utiliser pour le parsing
     */
    public Options getOptions() {
        return options;
    }

    /**
     * Configure le fichier selon la commande et les options
     * @param command Nom de la commande
     * @param cmdHandler Gestionnaire de ligne de commande
     * @param file Objet File à configurer
     */
    public void configureFile(String command, CommandLineHandler cmdHandler, File file) {
        if ("info".equals(command) && !cmdHandler.hasOption("s")) {
            // Pour la commande info sans option -s, on ne fait rie
        } else if (cmdHandler.hasOption("s")) {
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
    }
}