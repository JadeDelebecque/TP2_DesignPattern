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
import java.util.Map;

public class GererListeTest {

    @TempDir
    Path tempDir;

    private Path jsonFilePath;
    private GererListe gererListe;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() throws IOException {
        jsonFilePath = tempDir.resolve("test-grocery.json");
        Files.createFile(jsonFilePath);
        // Initialiser avec un fichier vide valide
        Files.writeString(jsonFilePath, "[]");

        gererListe = new GererListe(jsonFilePath.toString());

        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testCréerListe() throws IOException {
        gererListe.créerListe();
        Map<String, GroceryItem> items = gererListe.getItems();
        assertTrue(items.isEmpty());
    }

    @Test
    public void testAjouter() throws IOException {
        assertTrue(gererListe.ajouter("Milk", 10));

        Map<String, GroceryItem> items = gererListe.getItems();
        assertEquals(1, items.size());
        assertTrue(items.containsKey("Milk"));
        assertEquals(10, items.get("Milk").getQuantity());

        // Vérifier que le fichier a été mis à jour
        GererListe newGererListe = new GererListe(jsonFilePath.toString());
        Map<String, GroceryItem> newItems = newGererListe.getItems();
        assertTrue(newItems.containsKey("Milk"));
    }

    @Test
    public void testAjouterQuantitéNégative() throws IOException {
        assertFalse(gererListe.ajouter("Milk", -5));

        Map<String, GroceryItem> items = gererListe.getItems();
        assertTrue(items.isEmpty());
    }

    @Test
    public void testAjouterItemExistant() throws IOException {
        gererListe.ajouter("Milk", 10);
        assertTrue(gererListe.ajouter("Milk", 5));

        Map<String, GroceryItem> items = gererListe.getItems();
        assertEquals(1, items.size());
        assertEquals(15, items.get("Milk").getQuantity());
    }

    @Test
    public void testModifier() throws IOException {
        gererListe.ajouter("Bread", 2);
        assertTrue(gererListe.modifier("Bread", 5));

        Map<String, GroceryItem> items = gererListe.getItems();
        assertEquals(5, items.get("Bread").getQuantity());
    }

    @Test
    public void testModifierItemInexistant() throws IOException {
        assertFalse(gererListe.modifier("Cheese", 3));
    }

    @Test
    public void testModifierQuantitéNégative() throws IOException {
        gererListe.ajouter("Eggs", 12);
        assertFalse(gererListe.modifier("Eggs", -3));

        Map<String, GroceryItem> items = gererListe.getItems();
        assertEquals(12, items.get("Eggs").getQuantity());
    }

    @Test
    public void testEnlever() throws IOException {
        gererListe.ajouter("Yogurt", 3);
        assertTrue(gererListe.enlever("Yogurt"));

        Map<String, GroceryItem> items = gererListe.getItems();
        assertTrue(items.isEmpty());
    }

    @Test
    public void testEnleverItemInexistant() throws IOException {
        assertFalse(gererListe.enlever("Cheese"));
    }

    @Test
    public void testRéduireQuantité() throws IOException {
        gererListe.ajouter("Apples", 10);
        assertTrue(gererListe.réduireQuantité("Apples", 3));

        Map<String, GroceryItem> items = gererListe.getItems();
        assertEquals(7, items.get("Apples").getQuantity());
    }

    @Test
    public void testRéduireQuantitéJusquàZéro() throws IOException {
        gererListe.ajouter("Bananas", 5);
        assertTrue(gererListe.réduireQuantité("Bananas", 5));

        Map<String, GroceryItem> items = gererListe.getItems();
        assertFalse(items.containsKey("Bananas"));
    }

    @Test
    public void testRéduireQuantitéItemInexistant() throws IOException {
        assertFalse(gererListe.réduireQuantité("Oranges", 2));
    }

    @Test
    public void testRéduireQuantitéNégative() throws IOException {
        gererListe.ajouter("Carrots", 8);
        assertFalse(gererListe.réduireQuantité("Carrots", -2));

        Map<String, GroceryItem> items = gererListe.getItems();
        assertEquals(8, items.get("Carrots").getQuantity());
    }

    @Test
    public void testAfficherListeVide() {
        gererListe.afficher();
        assertEquals("La liste est vide." + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testAfficher() throws IOException {
        gererListe.ajouter("Milk", 10);
        gererListe.ajouter("Bread", 2);
        outContent.reset(); // Clear previous output

        gererListe.afficher();

        String output = outContent.toString();
        assertTrue(output.contains("Milk: 10"));
        assertTrue(output.contains("Bread: 2"));
    }

    @Test
    public void testSupprimer() throws IOException {
        gererListe.ajouter("Cereal", 1);
        gererListe.supprimer();

        Map<String, GroceryItem> items = gererListe.getItems();
        assertTrue(items.isEmpty());

        // Le fichier ne devrait plus exister
        assertFalse(Files.exists(jsonFilePath));
    }
}