package com.fges;

import com.fges.commands.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests simplifiés pour les commandes
 */
public class CommandsTest {

    // Pour capturer la sortie standard
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * Test pour la commande InfoCommand
     */
    @Test
    public void testInfoCommand() throws Exception {
        // Créer une commande Info
        Command infoCommand = CommandFactory.createCommand("info");

        // Exécuter la commande
        int result = infoCommand.execute(new String[]{"info"});

        // Vérifier le résultat
        assertEquals(0, result, "La commande Info devrait retourner 0");

        // Vérifier le contenu de la sortie standard
        String output = outContent.toString();
        assertTrue(output.contains("Date actuelle:"), "La sortie devrait contenir 'Date actuelle:'");
        assertTrue(output.contains("Système d'exploitation:"), "La sortie devrait contenir 'Système d'exploitation:'");
        assertTrue(output.contains("Version Java:"), "La sortie devrait contenir 'Version Java:'");
    }

    /**
     * Test pour CommandFactory avec une commande valide
     */
    @Test
    public void testCommandFactoryValidCommand() {
        // Créer une commande via la factory
        Command command = CommandFactory.createCommand("info");

        // Vérifier le type de la commande
        assertTrue(command instanceof InfoCommand, "La commande créée devrait être une InfoCommand");
    }

    /**
     * Test pour CommandFactory avec une commande invalide
     */
    @Test
    public void testCommandFactoryInvalidCommand() {
        // Tenter de créer une commande inexistante
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CommandFactory.createCommand("commande_inexistante");
        });

        // Vérifier le message d'erreur
        assertTrue(exception.getMessage().contains("Commande inconnue"),
                "Le message d'erreur devrait mentionner que la commande est inconnue");
    }

    /**
     * Test simplifié pour Main avec la commande info
     */
    @Test
    public void testMainWithInfoCommand() {
        // Tester l'exécution de Main avec la commande info
        int result = Main.exec(new String[]{"info"});

        // Vérifier le résultat
        assertEquals(0, result, "L'exécution de 'info' devrait retourner 0");

        // Vérifier la sortie standard
        String output = outContent.toString();
        assertTrue(output.contains("Date actuelle:"), "La sortie devrait contenir les infos système");
    }
}