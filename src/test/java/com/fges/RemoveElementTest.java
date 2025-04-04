package com.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RemoveElementTest {

    private RemoveElement removeElement;
    private MockFile mockFile;

    @BeforeEach
    public void setUp() {
        mockFile = new MockFile();
        removeElement = new RemoveElement(mockFile);
    }

    @Test
    public void testRemoveExistingElement() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5));
        mockFile.setItems(items);

        // Act
        removeElement.remove("Pommes");

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertFalse(result.containsKey("Pommes"));
    }

    @Test
    public void testRemoveNonExistingElement() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            removeElement.remove("Bananes");
        });
    }

    @Test
    public void testRemoveQuantiteExistingElement() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 10));
        mockFile.setItems(items);

        // Act
        removeElement.removeQuantité("Pommes", 3);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertTrue(result.containsKey("Pommes"));
        assertEquals(7, result.get("Pommes").getQuantity());
    }

    @Test
    public void testRemoveQuantiteExactMatch() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5));
        mockFile.setItems(items);

        // Act
        removeElement.removeQuantité("Pommes", 5);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertFalse(result.containsKey("Pommes"));
    }

    @Test
    public void testRemoveQuantiteMoreThanAvailable() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5));
        mockFile.setItems(items);

        // Act
        removeElement.removeQuantité("Pommes", 10);

        // Assert
        Map<String, GroceryItem> result = mockFile.getItems();
        assertFalse(result.containsKey("Pommes"));
    }

    @Test
    public void testRemoveQuantiteNonExistingElement() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            removeElement.removeQuantité("Bananes", 3);
        });
    }

    @Test
    public void testRemoveQuantiteInvalidQuantity() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Pommes", new GroceryItem("Pommes", 5));
        mockFile.setItems(items);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            removeElement.removeQuantité("Pommes", 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            removeElement.removeQuantité("Pommes", -3);
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