package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import java.io.IOException;
import java.util.List;

public class CommandeManager {
    private final CommandRegistry registry;

    public CommandeManager(File file) {
        this.registry = new CommandRegistry(file);
    }

    public void executeCommand(String commandName, List<String> args, CommandContext context) throws IOException {
        try {
            Command command = registry.getCommand(commandName);
            command.execute(args, context);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier: " + e.getMessage());
            throw new IllegalArgumentException("Erreur lors de l'écriture du fichier");
        }
    }

    // Si vous avez besoin d'accéder au registre directement
    public CommandRegistry getRegistry() {
        return registry;
    }
}