package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import com.fges.Web.GroceryShopAdapter;
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
        // Vérifier si le port est spécifié
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Port requis pour la commande web");
        }

        // Extraire et valider le port
        int port;
        try {
            port = Integer.parseInt(args.get(0));
            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException("Port invalide: " + port);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le port doit être un nombre");
        }

        // Créer l'adaptateur et le serveur
        MyGroceryShop groceryShop = new GroceryShopAdapter(file);
        GroceryShopServer server = new GroceryShopServer(groceryShop);

        // Démarrer le serveur
        System.out.println("Démarrage du serveur web sur le port " + port);
        server.start(port);
        System.out.println("Serveur web démarré à http://localhost:" + port);

        // Maintenir le serveur actif
        try {
            // Attendre indéfiniment (jusqu'à ce que l'utilisateur interrompe le programme)
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.err.println("Serveur interrompu: " + e.getMessage());
        }
    }
}