package com.fges.commands;

import java.util.ArrayList;
import java.util.List;


// Factory pour créer les commandes appropriées
public class CommandFactory {
    // Liste de toutes les commandes disponibles
    private static final List<Command> AVAILABLE_COMMANDS = new ArrayList<>();

    static {
        AVAILABLE_COMMANDS.add(new AddCommand());
        AVAILABLE_COMMANDS.add(new RemoveCommand());
        AVAILABLE_COMMANDS.add(new ListCommand());
        AVAILABLE_COMMANDS.add(new InfoCommand());
    }

    /**
     * Crée la commande appropriée en fonction du nom
     * @param commandName Nom de la commande à créer
     * @return Commande correspondante
     * @throws IllegalArgumentException Si la commande n'existe pas
     */
    public static Command createCommand(String commandName) {
        for (Command command : AVAILABLE_COMMANDS) {
            if (command.canHandle(commandName)) {
                return command;
            }
        }

        throw new IllegalArgumentException("Commande inconnue: " + commandName);
    }

    // Affiche l'aide pour toutes les commandes disponibles

    public static void displayHelp() {
        System.out.println("Commandes disponibles:");
        for (Command command : AVAILABLE_COMMANDS) {
            System.out.println("  " + command.getUsage());
        }
    }
}