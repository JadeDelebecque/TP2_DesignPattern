package com.fges;

import com.fges.File.FileFormat;
import com.fges.File.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonFormatTest {

    private JsonFormat jsonFormat;
    @TempDir
    Path tempDir;
    private String testFilePath;

    @BeforeEach
    void setUp() {
        jsonFormat = new JsonFormat();
        testFilePath = tempDir.resolve("test.json").toString();
    }

    @Test
    void testReadEmptyFile() throws IOException {
        // Create empty file
        Files.createFile(Path.of(testFilePath));
        // Write valid empty JSON array to the file
        Files.writeString(Path.of(testFilePath), "[]");

        // Test reading empty file
        Map<String, GroceryItem> result = jsonFormat.read(testFilePath);

        assertTrue(result.isEmpty());
    }

    @Test
    void testReadNonexistentFile() throws IOException {
        // Test reading nonexistent file
        Map<String, GroceryItem> result = jsonFormat.read(tempDir.resolve("nonexistent.json").toString());

        assertTrue(result.isEmpty());
    }

    @Test
    void testReadValidFile() throws IOException {
        // Create test file with valid content
        String content = "[\"Apple: 5\", \"Banana: 3\"]";
        Files.writeString(Path.of(testFilePath), content);

        // Test reading valid file
        Map<String, GroceryItem> result = jsonFormat.read(testFilePath);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("Apple"));
        assertTrue(result.containsKey("Banana"));
        assertEquals(5, result.get("Apple").getQuantity());
        assertEquals(3, result.get("Banana").getQuantity());
    }

    @Test
    void testReadInvalidJson() throws IOException {
        // Create file with invalid JSON
        Files.writeString(Path.of(testFilePath), "This is not JSON");

        // Should handle the exception and return empty map
        Map<String, GroceryItem> result = jsonFormat.read(testFilePath);

        assertTrue(result.isEmpty());
    }

    // Vu que on a des categories on a le meme test que TestWriteCategory
    @Test
    void testWrite() throws IOException {
        // Create test data
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Apple", new GroceryItem("Apple", 5));
        items.put("Banana", new GroceryItem("Banana", 3));

        // Write to file
        jsonFormat.write(testFilePath, items);

        // Verify file was created
        assertTrue(Files.exists(Path.of(testFilePath)));

        // Read the content to verify the structure includes all required fields
        String content = Files.readString(Path.of(testFilePath));
        assertTrue(content.contains("\"name\":\"Apple\""));
        assertTrue(content.contains("\"quantity\":5"));
        assertTrue(content.contains("\"name\":\"Banana\""));
        assertTrue(content.contains("\"quantity\":3"));
        assertTrue(content.contains("\"category\":\"default\""));

        // Test round-trip by reading back
        Map<String, GroceryItem> readItems = jsonFormat.read(testFilePath);
        assertEquals(2, readItems.size());
        assertEquals(5, readItems.get("Apple").getQuantity());
        assertEquals(3, readItems.get("Banana").getQuantity());
        assertEquals("default", readItems.get("Apple").getCategory());
        assertEquals("default", readItems.get("Banana").getCategory());
    }

    @Test
    void testWriteEmptyMap() throws IOException {
        // Create test data
        Map<String, GroceryItem> items = new HashMap<>();

        // Write to file
        jsonFormat.write(testFilePath, items);

        // Verify file was created with empty list
        assertTrue(Files.exists(Path.of(testFilePath)));
        String content = Files.readString(Path.of(testFilePath));
        assertEquals("[]", content);
    }

    @Test
    void testWithMockito() throws IOException {
        // Create a mock FileFormat
        FileFormat mockFormat = mock(FileFormat.class);

        // Setup expected data
        Map<String, GroceryItem> expectedItems = new HashMap<>();
        expectedItems.put("Apple", new GroceryItem("Apple", 5));

        // Configure mock behavior
        when(mockFormat.read(anyString())).thenReturn(expectedItems);

        // Use the mock
        Map<String, GroceryItem> result = mockFormat.read("any-file.json");

        // Verify results and interactions
        assertEquals(expectedItems, result);
        verify(mockFormat).read("any-file.json");
    }

    @Test
    void testWriteWithCategories() throws IOException {
        // Create test data with categories
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Lait", new GroceryItem("Lait", 2, "produits laitiers"));
        items.put("Pomme", new GroceryItem("Pomme", 5, "fruits"));
        items.put("Pain", new GroceryItem("Pain", 1, "default"));

        // Write to file
        jsonFormat.write(testFilePath, items);

        // Verify file was created
        assertTrue(Files.exists(Path.of(testFilePath)));

        // Read the content to verify the structure includes categories
        String content = Files.readString(Path.of(testFilePath));
        assertTrue(content.contains("\"name\""));
        assertTrue(content.contains("\"quantity\""));
        assertTrue(content.contains("\"category\""));
        assertTrue(content.contains("\"produits laitiers\""));
        assertTrue(content.contains("\"fruits\""));
        assertTrue(content.contains("\"default\""));

        // Test round-trip by reading back
        Map<String, GroceryItem> readItems = jsonFormat.read(testFilePath);
        assertEquals(3, readItems.size());
        assertEquals("produits laitiers", readItems.get("Lait").getCategory());
        assertEquals("fruits", readItems.get("Pomme").getCategory());
        assertEquals("default", readItems.get("Pain").getCategory());
    }



}