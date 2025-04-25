package com.fges;

import com.fges.Commande.AddElement;
import com.fges.File.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
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
        addElement.addItemInGrocery("Pommes", 5);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertTrue(result.containsKey("Pommes"));
        assertEquals(5, result.get("Pommes").getQuantity());
    }

    @Test
    public void testAddItemInGroceryExistant() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Lait", new GroceryItem("Lait", 2));
        mockFile.setItems(items);

        // Act
        addElement.addItemInGrocery("Lait", 3);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertEquals(5, result.get("Lait").getQuantity());
    }

    @Test
    public void testAddItemInGroceryCasInsensitive() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Lait", new GroceryItem("Lait", 2));
        mockFile.setItems(items);

        // Act
        addElement.addItemInGrocery("lait", 3);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertEquals(5, result.get("Lait").getQuantity());
    }

    @Test
    public void testAddItemInGroceryQuantiteInvalide() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            addElement.addItemInGrocery("Pommes", 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            addElement.addItemInGrocery("Pommes", -5);
        });
    }

    @Test
    public void testAddItemInGroceryNomInvalide() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            addElement.addItemInGrocery(null, 5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            addElement.addItemInGrocery("", 5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            addElement.addItemInGrocery("  ", 5);
        });
    }


    @Test
    public void testAddItemInGroceryAvecCategorie() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act
        addElement.addItemInGrocery("Lait", 5, "produits laitiers");

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertTrue(result.containsKey("Lait"));
        assertEquals(5, result.get("Lait").getQuantity());
        assertEquals("produits laitiers", result.get("Lait").getCategory());
    }

    @Test
    public void testAddItemInGrocerySansCategorie() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act
        addElement.addItemInGrocery("Lait", 5);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertTrue(result.containsKey("Lait"));
        assertEquals("default", result.get("Lait").getCategory());
    }

    @Test
    public void testMettreAJourCategorieElementExistant() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Lait", new GroceryItem("Lait", 2, "default"));
        mockFile.setItems(items);

        // Act
        addElement.addItemInGrocery("Lait", 3, "produits laitiers");

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertEquals(5, result.get("Lait").getQuantity());
        assertEquals("produits laitiers", result.get("Lait").getCategory());
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