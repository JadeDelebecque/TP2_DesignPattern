package com.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AddElementTest {

    private AddElement addElement;
    private MockFile mockFile;

    @BeforeEach
    public void setUp() {
        mockFile = new MockFile();
        addElement = new AddElement(mockFile);
    }

    @Test
    public void testAjouterNouvelElement() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act
        addElement.ajouterElement("Pommes", 5);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertTrue(result.containsKey("Pommes"));
        assertEquals(5, result.get("Pommes").getQuantity());
    }

    @Test
    public void testAjouterElementExistant() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Lait", new GroceryItem("Lait", 2));
        mockFile.setItems(items);

        // Act
        addElement.ajouterElement("Lait", 3);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertEquals(5, result.get("Lait").getQuantity());
    }

    @Test
    public void testAjouterElementCasInsensitive() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Lait", new GroceryItem("Lait", 2));
        mockFile.setItems(items);

        // Act
        addElement.ajouterElement("lait", 3);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertEquals(5, result.get("Lait").getQuantity());
    }

    @Test
    public void testAjouterElementQuantiteInvalide() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            addElement.ajouterElement("Pommes", 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            addElement.ajouterElement("Pommes", -5);
        });
    }

    @Test
    public void testAjouterElementNomInvalide() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            addElement.ajouterElement(null, 5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            addElement.ajouterElement("", 5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            addElement.ajouterElement("  ", 5);
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
        public Map<String, GroceryItem> entrée() {
            return items;
        }

        @Override
        public void sortie(Map<String, GroceryItem> items) {
            this.items = items;
        }
    }
}