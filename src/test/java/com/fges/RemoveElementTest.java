package com.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class RemoveElementTest {

    private Map<String, GroceryItem> items;

    @BeforeEach
    public void setUp() {
        items = new HashMap<>();
        items.put("Milk", new GroceryItem("Milk", 5));
        items.put("Bread", new GroceryItem("Bread", 2));
    }

    @Test
    public void testRemoveExistingItem() {
        boolean result = RemoveElement.remove(items, "Milk");

        assertTrue(result);
        assertEquals(1, items.size());
        assertFalse(items.containsKey("Milk"));
    }

    @Test
    public void testRemoveNonExistingItem() {
        boolean result = RemoveElement.remove(items, "Eggs");

        assertFalse(result);
        assertEquals(2, items.size());
    }

    @Test
    public void testRemoveQuantityPartial() {
        boolean result = RemoveElement.removeQuantité(items, "Milk", 2);

        assertTrue(result);
        assertEquals(2, items.size());
        assertEquals(3, items.get("Milk").getQuantity());
    }

    @Test
    public void testRemoveQuantityExact() {
        boolean result = RemoveElement.removeQuantité(items, "Bread", 2);

        assertTrue(result);
        assertEquals(1, items.size());
        assertFalse(items.containsKey("Bread"));
    }

    @Test
    public void testRemoveQuantityMore() {
        boolean result = RemoveElement.removeQuantité(items, "Bread", 5);

        assertTrue(result);
        assertEquals(1, items.size());
        assertFalse(items.containsKey("Bread"));
    }

    @Test
    public void testRemoveQuantityNonExistingItem() {
        boolean result = RemoveElement.removeQuantité(items, "Cheese", 1);

        assertFalse(result);
        assertEquals(2, items.size());
    }

    @Test
    public void testRemoveQuantityNegative() {
        boolean result = RemoveElement.removeQuantité(items, "Milk", -1);

        assertFalse(result);
        assertEquals(2, items.size());
        assertEquals(5, items.get("Milk").getQuantity());
    }

    @Test
    public void testRemoveQuantityZero() {
        boolean result = RemoveElement.removeQuantité(items, "Milk", 0);

        assertFalse(result);
        assertEquals(2, items.size());
        assertEquals(5, items.get("Milk").getQuantity());
    }
}