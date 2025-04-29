package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import com.fges.GroceryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddElementTest {

    @Mock
    private File mockFile;

    private AddElement command;
    private CommandContext context;
    private Map<String, GroceryItem> groceryItems;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        command = new AddElement(mockFile);
        context = new CommandContext();
        groceryItems = new HashMap<>();

        try {
            when(mockFile.loadFile()).thenReturn(groceryItems);
        } catch (IOException e) {
            fail("Exception during test setup: " + e.getMessage());
        }
    }

    @Test
    public void testAddNewItem() throws IOException {
        // Arrange
        List<String> args = Arrays.asList("Apples", "3");

        // Act
        command.execute(args, context);

        // Assert
        verify(mockFile).loadFile();
        verify(mockFile).saveFile(groceryItems);
        assertTrue(groceryItems.containsKey("Apples"));
        assertEquals(3, groceryItems.get("Apples").getQuantity());
        assertEquals("default", groceryItems.get("Apples").getCategory());
    }

    @Test
    public void testAddExistingItem() throws IOException {
        // Arrange
        groceryItems.put("Milk", new GroceryItem("Milk", 2, "dairy"));
        List<String> args = Arrays.asList("Milk", "1");

        // Act
        command.execute(args, context);

        // Assert
        verify(mockFile).loadFile();
        verify(mockFile).saveFile(groceryItems);
        assertEquals(3, groceryItems.get("Milk").getQuantity());
        assertEquals("dairy", groceryItems.get("Milk").getCategory());
    }

    @Test
    public void testAddWithCategory() throws IOException {
        // Arrange
        List<String> args = Arrays.asList("Cheese", "2");
        context.setCategory("dairy");

        // Act
        command.execute(args, context);

        // Assert
        verify(mockFile).saveFile(groceryItems);
        assertEquals("dairy", groceryItems.get("Cheese").getCategory());
    }

    @Test
    public void testAddWithEmptyCategory() throws IOException {
        // Arrange
        List<String> args = Arrays.asList("Bread", "1");
        context.setCategory("");

        // Act
        command.execute(args, context);

        // Assert
        assertEquals("default", groceryItems.get("Bread").getCategory());
    }

    @Test
    public void testInsufficientArguments() {
        // Arrange
        List<String> args = Arrays.asList("Carrots");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> command.execute(args, context));
        assertEquals("Arguments manquants pour la commande add", exception.getMessage());
    }

    @Test
    public void testNonNumericQuantity() {
        // Arrange
        List<String> args = Arrays.asList("Onions", "abc");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> command.execute(args, context));
        assertEquals("La quantité doit être un nombre", exception.getMessage());
    }

    @Test
    public void testZeroQuantity() {
        // Arrange
        List<String> args = Arrays.asList("Potatoes", "0");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> command.execute(args, context));
        assertEquals("La quantité doit être positive", exception.getMessage());
    }

    @Test
    public void testNegativeQuantity() {
        // Arrange
        List<String> args = Arrays.asList("Tomatoes", "-1");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> command.execute(args, context));
        assertEquals("La quantité doit être positive", exception.getMessage());
    }

    @Test
    public void testFileIOException() throws IOException {
        // Arrange
        List<String> args = Arrays.asList("Pears", "5");
        when(mockFile.loadFile()).thenThrow(new IOException("Test IO error"));

        // Act & Assert
        assertThrows(IOException.class, () -> command.execute(args, context));
    }
}