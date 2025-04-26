package com.fges.commands;

import java.text.SimpleDateFormat;
import java.util.Date;

// Commande pour afficher les informations

public class InfoCommand extends SystemCommand {

    public InfoCommand() {
        super("info");
    }

    @Override
    public int execute(String[] args) {
        // Date du jour
        Date dateActuelle = new Date();
        SimpleDateFormat formatteur = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormatee = formatteur.format(dateActuelle);

        // Informations sur le système
        String os = System.getProperty("os.name");
        String javaVersion = System.getProperty("java.version");

        // Affichage
        System.out.println("Date actuelle: " + dateFormatee);
        System.out.println("Système d'exploitation: " + os);
        System.out.println("Version Java: " + javaVersion);

        return 0;
    }

    @Override
    public String getUsage() {
        return "Usage: info (affiche les informations système)";
    }
}