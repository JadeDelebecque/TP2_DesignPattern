package com.fges;

import com.fges.format.File;
import com.fges.format.FileFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileTest {

    @TempDir
    Path tempDir;

    private Path jsonFile;
    private Path csvFile;
    private FileFormat mockFormat;

    @BeforeEach
    public void setUp() {
        jsonFile = tempDir.resolve("test.json");
        csvFile = tempDir.resolve("test.csv");
        mockFormat = mock(FileFormat.class);
    }

    @Test
    public void testConstructorWithJsonFile() {
        File file = new File();
        file.Fichier(jsonFile.toString());

        // This test is limited because format is private and there's no getter
        // We can only ensure no exception is thrown
        assertDoesNotThrow(() -> file.Fichier(jsonFile.toString()));
    }

    @Test
    public void testConstructorWithCsvFile() {
        File file = new File();
        file.Fichier(csvFile.toString());

        assertDoesNotThrow(() -> file.Fichier(csvFile.toString()));
    }

    @Test
    public void testConstructorWithCustomFormat() {
        File file = new File();
        file.Fichier("anyfile.txt", mockFormat);

        assertDoesNotThrow(() -> file.Fichier("anyfile.txt", mockFormat));
    }

    @Test
    public void testEntrée() throws IOException {
        Map<String, GroceryItem> expectedItems = new HashMap<>();
        expectedItems.put("Milk", new GroceryItem("Milk", 10));

        when(mockFormat.read(anyString())).thenReturn(expectedItems);

        File file = new File();
        file.Fichier("test.txt", mockFormat);

        Map<String, GroceryItem> result = file.entrée();

        assertEquals(expectedItems, result);
        verify(mockFormat).read("test.txt");
    }

    @Test
    public void testSortie() throws IOException {
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Bread", new GroceryItem("Bread", 5));

        File file = new File();
        file.Fichier("test.txt", mockFormat);
        file.sortie(items);

        verify(mockFormat).write("test.txt", items);
    }

    @Test
    public void testModification() throws IOException {
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Eggs", new GroceryItem("Eggs", 12));

        File file = new File();
        file.Fichier("test.txt", mockFormat);
        file.modification(items);

        verify(mockFormat).write("test.txt", items);
    }

    @Test
    public void testIntegration() throws IOException {
        // Create actual file for integration test
        File file = new File();
        file.Fichier(jsonFile.toString());

        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Apple", new GroceryItem("Apple", 7));

        // Write and read back
        file.sortie(items);
        Map<String, GroceryItem> readItems = file.entrée();

        assertFalse(readItems.isEmpty());
        assertTrue(readItems.containsKey("Apple"));
        assertEquals(7, readItems.get("Apple").getQuantity());
    }
}