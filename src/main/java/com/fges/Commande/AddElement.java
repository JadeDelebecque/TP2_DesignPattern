package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import com.fges.GroceryItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Classe responsable d'ajouter des éléments à la liste d'épicerie
 */
public class AddElement implements Command {
    private final File file;
    private static final String DEFAULT_CATEGORY = "default";

    public AddElement(File file) {
        this.file = file;
    }

    @Override
    public void execute(List<String> args, CommandContext context) throws IOException {
        if (args.size() < 2) {
            throw new IllegalArgumentException("Arguments manquants pour la commande add");
        }

        String name = args.get(0);
        String category = context.hasCategory() ? context.getCategory() : DEFAULT_CATEGORY;

        int quantity;
        try {
            quantity = Integer.parseInt(args.get(1));
            if (quantity <= 0) {
                throw new IllegalArgumentException("La quantité doit être positive");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La quantité doit être un nombre");
        }

        Map<String, GroceryItem> items = file.loadFile();
        if (items.containsKey(name)) {
            GroceryItem existingItem = items.get(name);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(name, new GroceryItem(name, quantity, category));
        }
        file.saveFile(items);
    }
}