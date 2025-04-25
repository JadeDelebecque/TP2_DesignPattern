package com.fges.Commande;

import com.fges.File.File;
import com.fges.GroceryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CommandeManagerTest {

    private CommandeManager commandeManager;
    private MockFile mockFile;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        mockFile = new MockFile();
        commandeManager = new CommandeManager(mockFile);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testHandleAddCommand() throws IOException {
        // Arrange
        List<String> args = Arrays.asList("Pommes", "5");
        String category = "Fruits";
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act
        commandeManager.handleAddCommand(args, category);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertTrue(result.containsKey("Pommes"));
        assertEquals(5, result.get("Pommes").getQuantity());
        assertEquals("Fruits", result.get("Pommes").getCategory());
    }

    @Test
    public void testHandleAddCommandArgumentsManquants() {
        // Arrange
        List<String> args = Arrays.asList("Pommes");
        String category = "Fruits";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commandeManager.handleAddCommand(args, category);
        });
    }

    @Test
    public void testHandleAddCommandQuantiteNonNumerique() {
        // Arrange
        List<String> args = Arrays.asList("Pommes", "abc");
        String category = "Fruits";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commandeManager.handleAddCommand(args, category);
        });
    }

    @Test
    public void testHandleAddCommandQuantiteInvalide() {
        // Arrange
        List<String> args = Arrays.asList("Pommes", "-5");
        String category = "Fruits";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commandeManager.handleAddCommand(args, category);
        });
    }

    @Test
    public void testHandleListCommand() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5, "Fruits"));
        items.put("Lait", new GroceryItem("Lait", 2, "Produits Laitiers"));
        mockFile.setItems(items);

        // Act
        commandeManager.handleListCommand();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("# Fruits:"));
        assertTrue(output.contains("Pommes: 5"));
        assertTrue(output.contains("# Produits Laitiers:"));
        assertTrue(output.contains("Lait: 2"));
    }

    @Test
    public void testHandleListCommandListeVide() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commandeManager.handleListCommand();
        });
    }

    @Test
    public void testHandleRemoveCommand() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5, "Fruits"));
        mockFile.setItems(items);
        List<String> args = Arrays.asList("Pommes");

        // Act
        commandeManager.handleRemoveCommand(args);

        // Assert
        assertFalse(mockFile.getItems().containsKey("Pommes"));
    }

    @Test
    public void testHandleRemoveCommandQuantitePartielle() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5, "Fruits"));
        mockFile.setItems(items);
        List<String> args = Arrays.asList("Pommes", "3");

        // Act
        commandeManager.handleRemoveCommand(args);

        // Assert
        assertTrue(mockFile.getItems().containsKey("Pommes"));
        assertEquals(2, mockFile.getItems().get("Pommes").getQuantity());
    }

    @Test
    public void testHandleRemoveCommandQuantiteTotale() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5, "Fruits"));
        mockFile.setItems(items);
        List<String> args = Arrays.asList("Pommes", "5");

        // Act
        commandeManager.handleRemoveCommand(args);

        // Assert
        assertFalse(mockFile.getItems().containsKey("Pommes"));
    }

    @Test
    public void testHandleRemoveCommandElementInexistant() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);
        List<String> args = Arrays.asList("Pommes");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commandeManager.handleRemoveCommand(args);
        });
    }

    @Test
    public void testHandleRemoveCommandQuantiteNonNumerique() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5, "Fruits"));
        mockFile.setItems(items);
        List<String> args = Arrays.asList("Pommes", "abc");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commandeManager.handleRemoveCommand(args);
        });
    }

    @Test
    public void testHandleRemoveCommandArgumentsManquants() {
        // Arrange
        List<String> args = new ArrayList<>();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commandeManager.handleRemoveCommand(args);
        });
    }

    @Test
    public void testExecuteCommandAdd() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);
        List<String> args = Arrays.asList("Pommes", "5");

        // Act
        commandeManager.executeCommand("add", args, "Fruits");

        // Assert
        assertTrue(mockFile.getItems().containsKey("Pommes"));
        assertEquals(5, mockFile.getItems().get("Pommes").getQuantity());
        assertEquals("Fruits", mockFile.getItems().get("Pommes").getCategory());
    }

    @Test
    public void testExecuteCommandList() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5, "Fruits"));
        mockFile.setItems(items);

        // Act
        commandeManager.executeCommand("list", new ArrayList<>(), null);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("# Fruits:"));
        assertTrue(output.contains("Pommes: 5"));
    }

    @Test
    public void testExecuteCommandRemove() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5, "Fruits"));
        mockFile.setItems(items);
        List<String> args = Arrays.asList("Pommes");

        // Act
        commandeManager.executeCommand("remove", args, null);

        // Assert
        assertFalse(mockFile.getItems().containsKey("Pommes"));
    }

    @Test
    public void testExecuteCommandInconnue() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commandeManager.executeCommand("inconnu", new ArrayList<>(), null);
        });
    }

    // Mock class for testing
    private class MockFile extends File {
        private Map<String, GroceryItem> items = new HashMap<>();

        public void setItems(Map<String, GroceryItem> items) {
            this.items = items;
        }

        public Map<String, GroceryItem> getItems() {
            return items;
        }

        @Override
        public Map<String, GroceryItem> loadFile() {
            return items;
        }

        @Override
        public void saveFile(Map<String, GroceryItem> items) {
            this.items = items;
        }
    }
}