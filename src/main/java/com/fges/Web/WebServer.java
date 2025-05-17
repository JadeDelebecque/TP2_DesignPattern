package com.fges.Web;

import com.fges.Web.SimpleGroceryShop;
import fr.anthonyquere.GroceryShopServer;
import fr.anthonyquere.MyGroceryShop;

public class WebServer {
    public static void main(String[] args) {
        MyGroceryShop groceryShop = new SimpleGroceryShop();
        GroceryShopServer server = new GroceryShopServer(groceryShop);
        server.start(8080);

        System.out.println("Serveur d'épicerie démarré sur http://localhost:8080");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}