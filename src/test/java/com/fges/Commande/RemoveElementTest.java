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

public class RemoveElementTest {

    @Mock
    private File mockFile;

    private RemoveElement removeElement;
    private List<String> args;
    private CommandContext context;

    // For capturing System.out
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        removeElement = new RemoveElement(mockFile);
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
    public void testExecuteWithoutArgs() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            removeElement.execute(args, context);
        });

        assertEquals("Nom du produit requis pour la commande remove", exception.getMessage());
    }

    @Test
    public void testExecuteCompleteRemove() throws IOException {
        // Arrange
        args.add("pomme");

        // Create and configure mock data
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 5));
        when(mockFile.loadFile()).thenReturn(items);

        // Act
        removeElement.execute(args, context);

        // Assert
        verify(mockFile).loadFile();
        verify(mockFile).saveFile(argThat(map -> !map.containsKey("pomme")));
    }

    @Test
    public void testExecutePartialRemove() throws IOException {
        // Arrange
        args.add("pomme");
        args.add("2");

        // Create and configure mock data
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 5));
        when(mockFile.loadFile()).thenReturn(items);

        // Act
        removeElement.execute(args, context);

        // Assert
        verify(mockFile).loadFile();
        verify(mockFile).saveFile(argThat(map ->
                map.containsKey("pomme") && map.get("pomme").getQuantity() == 3));
    }

    @Test
    public void testExecuteInvalidQuantity() {
        // Arrange
        args.add("pomme");
        args.add("abc");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            removeElement.execute(args, context);
        });

        assertEquals("La quantité doit être un nombre", exception.getMessage());
    }

    @Test
    public void testRemoveNonExistentProduct() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        when(mockFile.loadFile()).thenReturn(items);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            removeElement.remove("pomme");
        });

        assertEquals("Le produit 'pomme' n'existe pas", exception.getMessage());
        verify(mockFile).loadFile();
        verify(mockFile, never()).saveFile(any());
    }

    @Test
    public void testRemoveExistingProduct() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 5));
        when(mockFile.loadFile()).thenReturn(items);

        // Act
        removeElement.remove("pomme");

        // Assert
        verify(mockFile).loadFile();
        verify(mockFile).saveFile(argThat(map -> !map.containsKey("pomme")));
    }

    @Test
    public void testRemoveQuantityNonExistentProduct() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        when(mockFile.loadFile()).thenReturn(items);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            removeElement.removeQuantity("pomme", 2);
        });

        assertEquals("Le produit 'pomme' n'existe pas", exception.getMessage());
        verify(mockFile).loadFile();
        verify(mockFile, never()).saveFile(any());
    }

    @Test
    public void testRemoveNegativeQuantity() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 5));
        when(mockFile.loadFile()).thenReturn(items);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            removeElement.removeQuantity("pomme", -2);
        });

        assertEquals("La quantité à retirer doit être positive", exception.getMessage());
        verify(mockFile).loadFile();
        verify(mockFile, never()).saveFile(any());
    }

    @Test
    public void testRemoveQuantityCompleteRemoval() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 5));
        when(mockFile.loadFile()).thenReturn(items);

        // Act
        removeElement.removeQuantity("pomme", 5);

        // Assert
        verify(mockFile).loadFile();
        verify(mockFile).saveFile(argThat(map -> !map.containsKey("pomme")));
    }

    @Test
    public void testRemoveQuantityPartialRemoval() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        GroceryItem item = new GroceryItem("pomme", 5);
        items.put("pomme", item);
        when(mockFile.loadFile()).thenReturn(items);

        // Act
        removeElement.removeQuantity("pomme", 2);

        // Assert
        verify(mockFile).loadFile();
        verify(mockFile).saveFile(argThat(map ->
                map.containsKey("pomme") && map.get("pomme").getQuantity() == 3));
    }

    @Test
    public void testRemoveQuantityMoreThanAvailable() throws IOException {
        // Arrange
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 5));
        when(mockFile.loadFile()).thenReturn(items);

        // Act
        removeElement.removeQuantity("pomme", 10);

        // Assert
        verify(mockFile).loadFile();
        verify(mockFile).saveFile(argThat(map -> !map.containsKey("pomme")));
    }
}