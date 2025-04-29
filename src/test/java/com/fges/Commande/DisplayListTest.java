package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import com.fges.GroceryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
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

public class DisplayListTest {

    @Mock
    private File mockFile;

    private DisplayList displayList;
    private List<String> args;
    private CommandContext context;

    // For capturing System.out
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        displayList = new DisplayList(mockFile);
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
        when(mockFile.loadFile()).thenReturn(new HashMap<>());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            displayList.execute(args, context);
        });

        assertEquals("La liste est vide", exception.getMessage());
    }

    @Test
    public void testSingleItem() throws IOException {
        // Create test data
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 5));
        when(mockFile.loadFile()).thenReturn(items);

        // Execute command
        displayList.execute(args, context);

        // Verify output
        String output = outputStream.toString();
        assertTrue(output.contains("pomme"));
        assertTrue(output.contains("5"));
    }

    @Test
    public void testMultipleItems() throws IOException {
        // Create test data with multiple items
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 5));
        items.put("banane", new GroceryItem("banane", 3));
        items.put("orange", new GroceryItem("orange", 2));
        when(mockFile.loadFile()).thenReturn(items);

        // Execute command
        displayList.execute(args, context);

        // Verify output
        String output = outputStream.toString();
        assertTrue(output.contains("pomme"));
        assertTrue(output.contains("banane"));
        assertTrue(output.contains("orange"));
    }

    @Test
    public void testFileError() throws IOException {
        when(mockFile.loadFile()).thenThrow(new IOException("Test error"));

        assertThrows(IOException.class, () -> {
            displayList.execute(args, context);
        });
    }
}