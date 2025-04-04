package com.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GererListeTest {

    @TempDir
    Path tempDir;

    private Path tempFile;
    private GererListe gererListe;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = tempDir.resolve("test.json");
        Files.createFile(tempFile);

        // Redirect System.out for testing the afficher method
        System.setOut(new PrintStream(outContent));

        // Initialize with empty file
        gererListe = new GererListe(tempFile.toString());
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testConstructor() {
        assertNotNull(gererListe);
        assertNotNull(gererListe.getItems());
    }

    @Test
    public void testAjouter() throws IOException {
        // Act
        gererListe.ajouter("Pommes", 5);

        // Assert
        Map<String, GroceryItem> items = gererListe.getItems();
        assertTrue(items.containsKey("Pommes"));
        assertEquals(5, items.get("Pommes").getQuantity());
    }

    @Test
    public void testEnlever() throws IOException {
        // Arrange
        gererListe.ajouter("Pommes", 5);

        // Act
        gererListe.enlever("Pommes");

        // Assert
        Map<String, GroceryItem> items = gererListe.getItems();
        assertFalse(items.containsKey("Pommes"));
    }

    @Test
    public void testRéduireQuantité() throws IOException {
        // Arrange
        gererListe.ajouter("Pommes", 10);

        // Act
        gererListe.réduireQuantité("Pommes", 3);

        // Assert
        Map<String, GroceryItem> items = gererListe.getItems();
        assertEquals(7, items.get("Pommes").getQuantity());
    }

    @Test
    public void testRéduireQuantitéComplètement() throws IOException {
        // Arrange
        gererListe.ajouter("Pommes", 5);

        // Act
        gererListe.réduireQuantité("Pommes", 5);

        // Assert
        Map<String, GroceryItem> items = gererListe.getItems();
        assertFalse(items.containsKey("Pommes"));
    }

    @Test
    public void testAfficherListeVide() {
        // Act
        gererListe.afficher();

        // Assert
        assertEquals("La liste est vide." + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testAfficherAvecElements() throws IOException {
        // Arrange
        gererListe.ajouter("Pommes", 5);
        gererListe.ajouter("Lait", 2);
        outContent.reset(); // Clear previous output

        // Act
        gererListe.afficher();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Pommes: 5"));
        assertTrue(output.contains("Lait: 2"));
    }

    @Test
    public void testGetItems() throws IOException {
        // Arrange
        gererListe.ajouter("Pommes", 5);

        // Act
        Map<String, GroceryItem> items = gererListe.getItems();

        // Assert
        assertNotNull(items);
        assertEquals(1, items.size());
        assertTrue(items.containsKey("Pommes"));
    }
}