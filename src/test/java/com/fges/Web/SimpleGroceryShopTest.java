package com.fges.Web;

import com.fges.File.File;
import com.fges.GroceryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SimpleGroceryShopTest {

    @Mock
    private File fileMock;

    private SimpleGroceryShop groceryShop;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        groceryShop = new SimpleGroceryShop(fileMock);
    }

    @Test
    public void testGetGroceries() throws IOException {
        // Préparation
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 10, "fruit"));
        items.put("carotte", new GroceryItem("carotte", 5, "légume"));

        when(fileMock.loadFile()).thenReturn(items);

        // Exécution
        List<?> result = groceryShop.getGroceries();

        // Vérification
        assertEquals(2, result.size());

        // Utilisation de matchers plus précis avec des objets génériques
        Object pomme = result.stream()
                .filter(item -> {
                    try {
                        return "pomme".equals(item.getClass().getMethod("getName").invoke(item));
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
        assertNotNull(pomme);

        // Vérification par réflexion
        assertEquals(10, getPropertyValue(pomme, "getQuantity"));
        assertEquals("fruit", getPropertyValue(pomme, "getCategory"));

        Object carotte = result.stream()
                .filter(item -> {
                    try {
                        return "carotte".equals(item.getClass().getMethod("getName").invoke(item));
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
        assertNotNull(carotte);
        assertEquals(5, getPropertyValue(carotte, "getQuantity"));
        assertEquals("légume", getPropertyValue(carotte, "getCategory"));
    }

    private Object getPropertyValue(Object obj, String methodName) {
        try {
            return obj.getClass().getMethod(methodName).invoke(obj);
        } catch (Exception e) {
            fail("Impossible d'accéder à la méthode " + methodName + ": " + e.getMessage());
            return null;
        }
    }

    @Test
    public void testGetGroceriesHandlesIOException() throws IOException {
        // Préparation
        when(fileMock.loadFile()).thenThrow(new IOException("Erreur test"));

        // Exécution
        List<?> result = groceryShop.getGroceries();

        // Vérification
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAddGroceryItem() throws IOException {
        // Préparation
        Map<String, GroceryItem> existingItems = new HashMap<>();
        when(fileMock.loadFile()).thenReturn(existingItems);

        ArgumentCaptor<Map<String, GroceryItem>> mapCaptor = ArgumentCaptor.forClass(Map.class);

        // Exécution
        groceryShop.addGroceryItem("banane", 8, "fruit");

        // Vérification
        verify(fileMock).saveFile(mapCaptor.capture());
        Map<String, GroceryItem> savedItems = mapCaptor.getValue();

        assertTrue(savedItems.containsKey("banane"));
        GroceryItem banane = savedItems.get("banane");
        assertEquals("banane", banane.getName());
        assertEquals(8, banane.getQuantity());
        assertEquals("fruit", banane.getCategory());
    }

    @Test
    public void testAddGroceryItemHandlesIOException() throws IOException {
        // Préparation
        when(fileMock.loadFile()).thenReturn(new HashMap<>());
        doThrow(new IOException("Erreur test")).when(fileMock).saveFile(any());

        // Exécution - ne devrait pas lever d'exception
        groceryShop.addGroceryItem("banane", 8, "fruit");
    }

    @Test
    public void testRemoveGroceryItem() throws IOException {
        // Préparation
        Map<String, GroceryItem> items = new HashMap<>();
        items.put("pomme", new GroceryItem("pomme", 10, "fruit"));
        items.put("carotte", new GroceryItem("carotte", 5, "légume"));
        when(fileMock.loadFile()).thenReturn(items);

        ArgumentCaptor<Map<String, GroceryItem>> mapCaptor = ArgumentCaptor.forClass(Map.class);

        // Exécution
        groceryShop.removeGroceryItem("pomme");

        // Vérification
        verify(fileMock).saveFile(mapCaptor.capture());
        Map<String, GroceryItem> savedItems = mapCaptor.getValue();

        assertFalse(savedItems.containsKey("pomme"));
        assertTrue(savedItems.containsKey("carotte"));
    }

    @Test
    public void testRemoveGroceryItemHandlesIOException() throws IOException {
        // Préparation
        when(fileMock.loadFile()).thenReturn(new HashMap<>());
        doThrow(new IOException("Erreur test")).when(fileMock).saveFile(any());

        // Exécution - ne devrait pas lever d'exception
        groceryShop.removeGroceryItem("pomme");
    }

    @Test
    public void testGetRuntime() {
        // Exécution
        Object runtime = groceryShop.getRuntime();

        // Vérification
        assertNotNull(runtime);

        // Utilisation de la réflexion pour accéder aux méthodes
        try {
            assertEquals(LocalDate.now(), runtime.getClass().getMethod("date").invoke(runtime));
            assertEquals(System.getProperty("java.version"),
                    runtime.getClass().getMethod("javaVersion").invoke(runtime));
            assertEquals(System.getProperty("os.name"),
                    runtime.getClass().getMethod("osName").invoke(runtime));
        } catch (Exception e) {
            fail("Erreur lors de l'accès aux méthodes de Runtime: " + e.getMessage());
        }
    }
}