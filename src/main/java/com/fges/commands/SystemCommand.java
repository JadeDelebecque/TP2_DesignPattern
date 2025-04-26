package com.fges.commands;

/**
 * Classe abstraite pour les commandes système qui ne nécessitent pas
 * de fichier de données (comme la commande info)
 */
public abstract class SystemCommand extends AbstractCommand {

    public SystemCommand(String commandName) {
        super(commandName);
    }
}