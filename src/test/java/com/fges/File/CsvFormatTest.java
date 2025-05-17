package com.fges.File;

import com.fges.GroceryItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

public class CsvFormatTest {

    private CsvFormat csvFormat;

    @TempDir
    Path tempDir;

    private Path tempFile;

    @BeforeEach
    public void setUp() {
        csvFormat = new CsvFormat();
        tempFile = tempDir.resolve("test.csv");
    }

    @Test
    public void testReadEmptyFile() throws IOException {
        Files.createFile(tempFile);
        Map<String, GroceryItem> items = csvFormat.read(tempFile.toString());
        assertTrue(items.isEmpty());
    }

    @Test
    public void testReadWithHeader() throws IOException {
        List<String> lines = Arrays.asList("Nom,Quantité", "Milk,10", "Bread,2");
        Files.write(tempFile, lines);

        Map<String, GroceryItem> items = csvFormat.read(tempFile.toString());

        assertEquals(2, items.size());
        assertTrue(items.containsKey("Milk"));
        assertTrue(items.containsKey("Bread"));
        assertEquals(10, items.get("Milk").getQuantity());
        assertEquals(2, items.get("Bread").getQuantity());
    }

    @Test
    public void testReadWithoutHeader() throws IOException {
        List<String> lines = Arrays.asList("Milk,10", "Bread,2");
        Files.write(tempFile, lines);

        Map<String, GroceryItem> items = csvFormat.read(tempFile.toString());

        assertEquals(2, items.size());
        assertTrue(items.containsKey("Milk"));
        assertEquals(10, items.get("Milk").getQuantity());
    }

    @Test
    public void testWrite() throws IOException {
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("Milk", new GroceryItem("Milk", 10));
        items.put("Eggs", new GroceryItem("Eggs", 12));

        csvFormat.write(tempFile.toString(), items);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals(3, lines.size());
        assertEquals("Nom,Quantité,Catégorie", lines.get(0));

        // Vérifier que chaque ligne contient les informations correctes
        boolean foundMilk = false;
        boolean foundEggs = false;

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("Milk,10,")) {
                foundMilk = true;
            } else if (line.startsWith("Eggs,12,")) {
                foundEggs = true;
            }
        }

        assertTrue(foundMilk && foundEggs, "Les lignes attendues n'ont pas été trouvées");
    }

    @Test
    public void testReadNonExistentFile() throws IOException {
        Path nonExistentFile = tempDir.resolve("nonexistent.csv");
        Map<String, GroceryItem> items = csvFormat.read(nonExistentFile.toString());
        assertTrue(items.isEmpty());
    }

    @Test
    public void testReadWithCategories() throws IOException {
        // Create test file with categories
        List<String> lines = Arrays.asList(
                "Nom,Quantité,Catégorie",
                "Lait,2,produits laitiers",
                "Pomme,5,fruits",
                "Pain,1,default"
        );
        Files.write(tempFile, lines);

        // Read the file
        Map<String, GroceryItem> items = csvFormat.read(tempFile.toString());

        // Verify items and categories
        assertEquals(3, items.size());
        assertEquals("produits laitiers", items.get("Lait").getCategory());
        assertEquals("fruits", items.get("Pomme").getCategory());
        assertEquals("default", items.get("Pain").getCategory());
    }
}
