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

public class GroceryListManagerTest {

    @TempDir
    Path tempDir;

    private Path tempFile;
    private GroceryListManager groceryListManager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = tempDir.resolve("test.json");
        Files.createFile(tempFile);

        // Redirect System.out for testing the afficher method
        System.setOut(new PrintStream(outContent));

        // Initialize with empty file
        groceryListManager = new GroceryListManager(tempFile.toString());
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testConstructor() {
        assertNotNull(groceryListManager);
        assertNotNull(groceryListManager.getItems());
    }

    @Test
    public void testAjouter() throws IOException {
        // Act
        groceryListManager.addItem("Pommes", 5);

        // Assert
        Map<String, GroceryItem> items = groceryListManager.getItems();
        assertTrue(items.containsKey("Pommes"));
        assertEquals(5, items.get("Pommes").getQuantity());
    }

    @Test
    public void testEnlever() throws IOException {
        // Arrange
        groceryListManager.addItem("Pommes", 5);

        // Act
        groceryListManager.removeItem("Pommes");

        // Assert
        Map<String, GroceryItem> items = groceryListManager.getItems();
        assertFalse(items.containsKey("Pommes"));
    }

    @Test
    public void testRéduireQuantité() throws IOException {
        // Arrange
        groceryListManager.addItem("Pommes", 10);

        // Act
        groceryListManager.reduceQuantity("Pommes", 3);

        // Assert
        Map<String, GroceryItem> items = groceryListManager.getItems();
        assertEquals(7, items.get("Pommes").getQuantity());
    }

    @Test
    public void testRéduireQuantitéComplètement() throws IOException {
        // Arrange
        groceryListManager.addItem("Pommes", 5);

        // Act
        groceryListManager.reduceQuantity("Pommes", 5);

        // Assert
        Map<String, GroceryItem> items = groceryListManager.getItems();
        assertFalse(items.containsKey("Pommes"));
    }

    @Test
    public void testDisplayItemsListeVide() {
        // Act
        groceryListManager.displayItems();

        // Assert
        assertEquals("La liste est vide." + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testDisplayItemsAvecElements() throws IOException {
        // Arrange
        groceryListManager.addItem("Pommes", 5);
        groceryListManager.addItem("Lait", 2);
        outContent.reset(); // Clear previous output

        // Act
        groceryListManager.displayItems();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Pommes: 5"));
        assertTrue(output.contains("Lait: 2"));
    }

    @Test
    public void testGetItems() throws IOException {
        // Arrange
        groceryListManager.addItem("Pommes", 5);

        // Act
        Map<String, GroceryItem> items = groceryListManager.getItems();

        // Assert
        assertNotNull(items);
        assertEquals(1, items.size());
        assertTrue(items.containsKey("Pommes"));
    }
}