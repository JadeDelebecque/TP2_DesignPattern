package com.fges.Commande;

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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DisplayListTest {

    private DisplayList displayList;

    @Mock
    private File mockFile;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        displayList = new DisplayList(mockFile);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testDisplayGroceryList() throws IOException {
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Apple", new GroceryItem("Apple", 5, "Fruit"));
        items.put("Milk", new GroceryItem("Milk", 2, "Dairy"));

        when(mockFile.loadFile()).thenReturn(items);

        // Execute
        displayList.displayGroceryList();

        // Verify
        String output = outContent.toString();
        assertTrue(output.contains("Apple: 5"));
        assertTrue(output.contains("Milk: 2"));
        verify(mockFile).loadFile();
    }

    @Test
    void testDisplayEmptyList() throws IOException {
        // Prepare empty list
        Map<String, GroceryItem> emptyItems = new HashMap<>();
        when(mockFile.loadFile()).thenReturn(emptyItems);

        // Execute and verify exception
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> displayList.displayGroceryList()
        );
        assertEquals("La liste est vide", exception.getMessage());
        verify(mockFile).loadFile();
    }
}