package com.fges;

import com.fges.File.File;
import com.fges.File.FileFormat;
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
        file.formatAFile(jsonFile.toString());

        // This test is limited because format is private and there's no getter
        // We can only ensure no exception is thrown
        assertDoesNotThrow(() -> file.formatAFile(jsonFile.toString()));
    }

    @Test
    public void testConstructorWithCsvFile() {
        File file = new File();
        file.formatAFile(csvFile.toString());

        assertDoesNotThrow(() -> file.formatAFile(csvFile.toString()));
    }

    @Test
    public void testConstructorWithCustomFormat() {
        File file = new File();
        file.formatAFile("anyfile.txt", mockFormat);

        assertDoesNotThrow(() -> file.formatAFile("anyfile.txt", mockFormat));
    }

    @Test
    public void testLoadFile() throws IOException {
        Map<String, GroceryItem> expectedItems = new HashMap<>();
        expectedItems.put("Milk", new GroceryItem("Milk", 10));

        when(mockFormat.read(anyString())).thenReturn(expectedItems);

        File file = new File();
        file.formatAFile("test.txt", mockFormat);

        Map<String, GroceryItem> result = file.loadFile();

        assertEquals(expectedItems, result);
        verify(mockFormat).read("test.txt");
    }

    @Test
    public void testSaveFile() throws IOException {
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Bread", new GroceryItem("Bread", 5));

        File file = new File();
        file.formatAFile("test.txt", mockFormat);
        file.saveFile(items);

        verify(mockFormat).write("test.txt", items);
    }

    @Test
    public void testModification() throws IOException {
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Eggs", new GroceryItem("Eggs", 12));

        File file = new File();
        file.formatAFile("test.txt", mockFormat);
        file.modification(items);

        verify(mockFormat).write("test.txt", items);
    }

    @Test
    public void testIntegration() throws IOException {
        // Create actual file for integration test
        File file = new File();
        file.formatAFile(jsonFile.toString());

        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Apple", new GroceryItem("Apple", 7));

        // Write and read back
        file.saveFile(items);
        Map<String, GroceryItem> readItems = file.loadFile();

        assertFalse(readItems.isEmpty());
        assertTrue(readItems.containsKey("Apple"));
        assertEquals(7, readItems.get("Apple").getQuantity());
    }
}