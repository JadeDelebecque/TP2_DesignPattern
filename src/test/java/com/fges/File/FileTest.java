package com.fges.File;

import com.fges.GroceryItem;
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

    @Test
    public void testFormatWithSpecifiedFormat() throws IOException {
        // Test JSON format specified for a file with non-JSON extension
        File jsonFormatFile = new File();
        jsonFormatFile.formatAFileWithSpecifiedFormat(csvFile.toString(), "json");

        // Save data using the JSON format
        Map<String, GroceryItem> jsonItems = new HashMap<>();
        jsonItems.put("Chocolate", new GroceryItem("Chocolate", 3));
        jsonFormatFile.saveFile(jsonItems);

        // Test CSV format specified for a file with non-CSV extension
        File csvFormatFile = new File();
        csvFormatFile.formatAFileWithSpecifiedFormat(jsonFile.toString(), "csv");

        // Save data using the CSV format
        Map<String, GroceryItem> csvItems = new HashMap<>();
        csvItems.put("Banana", new GroceryItem("Banana", 5));
        csvFormatFile.saveFile(csvItems);

        // Verify that the files can be read back with the correct format
        File jsonReader = new File();
        jsonReader.formatAFileWithSpecifiedFormat(csvFile.toString(), "json");
        Map<String, GroceryItem> readJsonItems = jsonReader.loadFile();

        File csvReader = new File();
        csvReader.formatAFileWithSpecifiedFormat(jsonFile.toString(), "csv");
        Map<String, GroceryItem> readCsvItems = csvReader.loadFile();

        // Assert that the items were correctly saved and loaded
        assertTrue(readJsonItems.containsKey("Chocolate"));
        assertEquals(3, readJsonItems.get("Chocolate").getQuantity());

        assertTrue(readCsvItems.containsKey("Banana"));
        assertEquals(5, readCsvItems.get("Banana").getQuantity());
    }

    @Test
    public void testFormatWithInvalidSpecifiedFormat() {
        File file = new File();

        // Test with an invalid format - should default to JSON
        file.formatAFileWithSpecifiedFormat("test.dat", "unknown");

        // Since we can't directly test the private format field,
        // we can verify the file path was set correctly
        assertEquals("test.dat", file.getFilePath());

        // The test passes if no exception is thrown
        assertDoesNotThrow(() -> file.formatAFileWithSpecifiedFormat("test.dat", "unknown"));
    }
}