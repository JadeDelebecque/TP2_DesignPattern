package com.fges.Commande;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsable d'afficher les informations système
 * Cette commande affiche la date du jour, le nom du système d'exploitation et la version de Java
 */
public class InfoCommand {

    /**
     * Affiche les informations système
     * - La date du jour
     * - Le nom du système d'exploitation
     * - La version de Java
     */
    public void displaySystemInfo() {
        // Afficher la date du jour
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Today's date: " + today.format(formatter));

        // Afficher le nom du système d'exploitation
        String osName = System.getProperty("os.name");
        System.out.println("Operating System: " + osName);

        // Afficher la version de Java
        String javaVersion = System.getProperty("java.version");
        System.out.println("Java version: " + javaVersion);
    }
}