package com.fges.Web;

import com.fges.CLI.CommandContext;
import com.fges.Commande.AddElement;
import com.fges.Commande.RemoveElement;
import com.fges.File.File;
import com.fges.GroceryItem;
import fr.anthonyquere.MyGroceryShop;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleGroceryShop implements MyGroceryShop {
    private final File file;
    private final AddElement addCommand;
    private final RemoveElement removeCommand;

    public SimpleGroceryShop(File file) {
        this.file = file;
        this.addCommand = new AddElement(file);
        this.removeCommand = new RemoveElement(file);
    }

    @Override
    public List<WebGroceryItem> getGroceries() {
        List<WebGroceryItem> webItems = new ArrayList<>();

        try {
            Map<String, GroceryItem> items = file.loadFile();
            for (GroceryItem item : items.values()) {
                webItems.add(new WebGroceryItem(
                        item.getName(),
                        item.getQuantity(),
                        item.getCategory()
                ));
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier : " + e.getMessage());
        }

        return webItems;
    }

    @Override
    public void addGroceryItem(String name, int quantity, String category) {
        CommandContext context = new CommandContext();
        context.setCategory(category);

        List<String> args = new ArrayList<>();
        args.add(name);
        args.add(String.valueOf(quantity));

        try {
            addCommand.execute(args, context);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ajout de l'élément : " + e.getMessage());
        }
    }

    @Override
    public void removeGroceryItem(String name) {
        CommandContext context = new CommandContext();

        List<String> args = new ArrayList<>();
        args.add(name);

        try {
            removeCommand.execute(args, context);
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression de l'élément : " + e.getMessage());
        }
    }

    @Override
    public Runtime getRuntime() {
        return new Runtime(
                LocalDate.now(),
                System.getProperty("java.version"),
                System.getProperty("os.name")
        );
    }
}