package com.fges;

import com.fges.Commande.CommandeManager;
import com.fges.Commande.InfoCommand;
import com.fges.File.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe de test pour la fonctionnalité de commande "info"
 * Vérifie que la commande info affiche correctement la date, le système d'exploitation et la version Java
 */
public class InfoCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Configure la redirection de la sortie standard pour les tests
     */
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Restaure la sortie standard après les tests
     */
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * Teste que la classe InfoCommand affiche correctement les informations système
     */
    @Test
    public void testInfoCommandOutput() {
        // Execution
        InfoCommand infoCommand = new InfoCommand();
        infoCommand.displaySystemInfo();

        // Récupération de la sortie
        String output = outContent.toString();

        // Vérification
        assertTrue(output.contains("Today's date:"));
        assertTrue(output.contains("Operating System:"));
        assertTrue(output.contains("Java version:"));

        // Vérification que la date du jour est correctement formatée
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expectedDate = "Today's date: " + today.format(formatter);
        assertTrue(output.contains(expectedDate));

        // Vérification de l'OS et de la version Java
        String osName = System.getProperty("os.name");
        assertTrue(output.contains("Operating System: " + osName));

        String javaVersion = System.getProperty("java.version");
        assertTrue(output.contains("Java version: " + javaVersion));
    }

    /**
     * Teste que la commande "info" est correctement gérée par CommandeManager
     */
    @Test
    public void testInfoCommandViaManager() throws IOException {
        // Initialisation
        File file = new File();
        file.formatAFile("test.json");
        CommandeManager manager = new CommandeManager(file);

        // Execution
        manager.handleInfoCommand();

        // Récupération de la sortie
        String output = outContent.toString();

        // Vérification
        assertTrue(output.contains("Today's date:"));
        assertTrue(output.contains("Operating System:"));
        assertTrue(output.contains("Java version:"));
    }

    /**
     * Teste que la commande "info" est correctement gérée via executeCommand
     */
    @Test
    public void testInfoCommandExecution() throws IOException {
        // Initialisation
        File file = new File();
        file.formatAFile("test.json");
        CommandeManager manager = new CommandeManager(file);

        // Execution
        manager.executeCommand("info", new ArrayList<>(), null);

        // Récupération de la sortie
        String output = outContent.toString();

        // Vérification
        assertTrue(output.contains("Today's date:"));
        assertTrue(output.contains("Operating System:"));
        assertTrue(output.contains("Java version:"));
    }

    /**
     * Teste que Main.exec() gère correctement la commande "info"
     */
    @Test
    public void testInfoCommandFromMain() throws IOException {
        // Exécution
        Main.exec(new String[]{"info"});

        // Récupération de la sortie
        String output = outContent.toString();

        // Vérification
        assertTrue(output.contains("Today's date:"));
        assertTrue(output.contains("Operating System:"));
        assertTrue(output.contains("Java version:"));
    }

    /**
     * Teste que Main.exec() gère correctement la commande "info" avec des arguments supplémentaires
     */
    @Test
    public void testInfoCommandWithArguments() throws IOException {
        // Exécution avec différents arguments qui doivent être ignorés
        Main.exec(new String[]{"info", "-s", "dummy.json"});

        // Récupération de la sortie
        String output = outContent.toString();

        // Vérification
        assertTrue(output.contains("Today's date:"));
        assertTrue(output.contains("Operating System:"));
        assertTrue(output.contains("Java version:"));
    }
}