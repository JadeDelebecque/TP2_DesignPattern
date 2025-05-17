package com.fges.Commande;

import com.fges.File.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Registre centralisé des commandes disponibles dans l'application
 */
public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Initialise le registre avec les commandes de base
     * @param file Le fichier pour les opérations de lecture/écriture
     */
    public CommandRegistry(File file) {
        // Enregistrement des commandes standard
        register("add", new AddElement(file));
        register("list", new ListCategory(file));
        register("remove", new RemoveElement(file));
        register("info", new InfoCommand());
        register("display", new DisplayList(file));

        // ajouter commande ici -> renvoie vers le fichier de la commande
    }

    public void register(String name, Command command) {
        // pour enregistrer une commande
        commands.put(name.toLowerCase(), command);
    }

    public Command getCommand(String name) {
        Command command = commands.get(name.toLowerCase());
        if (command == null) {
            throw new IllegalArgumentException("Commande inconnue: " + name);
        }
        return command;
    }

    public boolean hasCommand(String name) {
        return commands.containsKey(name.toLowerCase());
    }
}