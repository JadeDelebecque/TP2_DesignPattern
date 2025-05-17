package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import com.fges.Web.SimpleGroceryShop;
import fr.anthonyquere.GroceryShopServer;
import fr.anthonyquere.MyGroceryShop;

import java.io.IOException;
import java.util.List;

public class WebCommand implements Command {

    private final File file;

    public WebCommand(File file) {
        this.file = file;
    }

    @Override
    public void execute(List<String> args, CommandContext context) throws IOException {
        int port = 8080;

        // Vérifier si un port est spécifié
        if (args.size() > 0) {
            try {
                port = Integer.parseInt(args.get(0));
            } catch (NumberFormatException e) {
                System.err.println("Port invalide, utilisation du port 8080 par défaut");
            }
        }

        MyGroceryShop groceryShop = new SimpleGroceryShop();
        GroceryShopServer server = new GroceryShopServer(groceryShop);
        server.start(port);

        System.out.println("Serveur d'épicerie démarré sur http://localhost:" + port);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}