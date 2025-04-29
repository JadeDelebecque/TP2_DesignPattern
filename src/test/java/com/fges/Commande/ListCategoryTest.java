package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import com.fges.GroceryItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListCategoryTest {

    @Mock
    private File mockFile;

    private ListCategory listCategory;
    private List<String> args;
    private CommandContext context;

    // For capturing System.out
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        listCategory = new ListCategory(mockFile);
        args = new ArrayList<>();
        context = new CommandContext("test");

        // Set up System.out capture
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testEmptyList() throws IOException {
        // Arrange
        when(mockFile.loadFile()).thenReturn(new HashMap<>());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            listCategory.execute(args, context);
        });

        assertEquals("La liste est vide", exception.getMessage());
        verify(mockFile).loadFile();
    }

    @Test
    public void testSingleCategory() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        GroceryItem item1 = new GroceryItem("pomme", 5);
        item1.setCategory("fruits");
        GroceryItem item2 = new GroceryItem("orange", 3);
        item2.setCategory("fruits");

        items.put("pomme", item1);
        items.put("orange", item2);

        when(mockFile.loadFile()).thenReturn(items);

        // Act
        listCategory.execute(args, context);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("# fruits:"));
        assertTrue(output.contains("pomme"));
        assertTrue(output.contains("orange"));
        verify(mockFile).loadFile();
    }

    @Test
    public void testMultipleCategories() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();

        // Create items in different categories
        GroceryItem item1 = new GroceryItem("pomme", 5);
        item1.setCategory("fruits");

        GroceryItem item2 = new GroceryItem("carotte", 2);
        item2.setCategory("légumes");

        GroceryItem item3 = new GroceryItem("pain", 1);
        item3.setCategory("boulangerie");

        items.put("pomme", item1);
        items.put("carotte", item2);
        items.put("pain", item3);

        when(mockFile.loadFile()).thenReturn(items);

        // Act
        listCategory.execute(args, context);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("# boulangerie:"));
        assertTrue(output.contains("# fruits:"));
        assertTrue(output.contains("# légumes:"));
        assertTrue(output.contains("pomme"));
        assertTrue(output.contains("carotte"));
        assertTrue(output.contains("pain"));
        verify(mockFile).loadFile();
    }

    @Test
    public void testSortedCategories() throws IOException {
        // Arrange - create items with categories that will be sorted alphabetically
        Map<String, GroceryItem> items = new HashMap<>();

        GroceryItem item1 = new GroceryItem("pomme", 5);
        item1.setCategory("fruits");

        GroceryItem item2 = new GroceryItem("carotte", 2);
        item2.setCategory("légumes");

        GroceryItem item3 = new GroceryItem("lait", 1);
        item3.setCategory("dairy");

        items.put("pomme", item1);
        items.put("carotte", item2);
        items.put("lait", item3);

        when(mockFile.loadFile()).thenReturn(items);

        // Act
        listCategory.execute(args, context);

        // Assert - verify categories are in alphabetical order
        String output = outputStream.toString();
        int dairyIndex = output.indexOf("dairy");
        int fruitsIndex = output.indexOf("fruits");
        int legumesIndex = output.indexOf("légumes");

        assertTrue(dairyIndex < fruitsIndex);
        assertTrue(fruitsIndex < legumesIndex);
        verify(mockFile).loadFile();
    }

    @Test
    public void testFileError() throws IOException {
        // Arrange
        when(mockFile.loadFile()).thenThrow(new IOException("Test error"));

        // Act & Assert
        assertThrows(IOException.class, () -> {
            listCategory.execute(args, context);
        });

        verify(mockFile).loadFile();
    }
}