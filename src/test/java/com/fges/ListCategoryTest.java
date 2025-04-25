package com.fges;

import com.fges.Commande.ListCategory;
import com.fges.File.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class ListCategoryTest {

    private ListCategory listCategory;
    private MockFile mockFile;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        mockFile = new MockFile();
        listCategory = new ListCategory(mockFile);

        // Redirect System.out
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testListByCategory() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Lait", new GroceryItem("Lait", 2, "produits laitiers"));
        items.put("Fromage", new GroceryItem("Fromage", 1, "produits laitiers"));
        items.put("Pomme", new GroceryItem("Pomme", 5, "fruits"));
        items.put("Pain", new GroceryItem("Pain", 1, "default"));
        mockFile.setItems(items);

        // Act
        listCategory.listByCategory();
        String output = outContent.toString();

        // Assert
        assertTrue(output.contains("# default:"));
        assertTrue(output.contains("Pain: 1"));
        assertTrue(output.contains("# fruits:"));
        assertTrue(output.contains("Pomme: 5"));
        assertTrue(output.contains("# produits laitiers:"));
        assertTrue(output.contains("Lait: 2"));
        assertTrue(output.contains("Fromage: 1"));

        // Vérifier l'ordre des catégories (ordre alphabétique)
        int defaultPos = output.indexOf("# default:");
        int fruitsPos = output.indexOf("# fruits:");
        int laitiersPos = output.indexOf("# produits laitiers:");

        assertTrue(defaultPos < fruitsPos);
        assertTrue(fruitsPos < laitiersPos);
    }

    @Test
    public void testListEmptyItems() {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        mockFile.setItems(items);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> listCategory.listByCategory()
        );
        assertEquals("La liste est vide", exception.getMessage());
    }

    // Mock class for testing
    private class MockFile extends File {
        private Map<String, GroceryItem> items = new HashMap<>();

        public void setItems(Map<String, GroceryItem> items) {
            this.items = items;
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