package com.fges.Commande;
import com.fges.CLI.CommandContext;

import java.io.IOException;
import java.util.List;

public interface Command {
    void execute(List<String> args, CommandContext context) throws IOException;
    default void execute(List<String> args, String category) throws IOException {
        CommandContext context = new CommandContext();
        context.setCategory(category);
        execute(args, context);
    }


}
