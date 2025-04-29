package com.fges.Commande;

import com.fges.CLI.CommandContext;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Classe responsable d'afficher les informations système
 * Cette commande affiche la date du jour, le nom du système d'exploitation et la version de Java
 */
public class InfoCommand implements Command {
    @Override
    public void execute(List<String> args, CommandContext context) throws IOException {
        displaySystemInfo();
    }


    public void displaySystemInfo(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        System.out.println("Today's date: " + formatter.format(date));
        System.out.println("Operating System: " + System.getProperty("os.name"));
        System.out.println("Java version: " + System.getProperty("java.version"));
    }
}