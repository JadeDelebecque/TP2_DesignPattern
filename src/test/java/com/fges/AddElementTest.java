package com.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class AddElementTest {

    private Map<String, GroceryItem> items;

    @BeforeEach
    public void setUp() {
        items = new HashMap<>();
        items.put("Milk", new GroceryItem("Milk", 5));
    }

    @Test
    public void testAjouterElementNewItem() {
        boolean result = AddElement.ajouterElement(items, "Bread", 2);

        assertTrue(result);
        assertEquals(2, items.size());
        assertTrue(items.containsKey("Bread"));
        assertEquals(2, items.get("Bread").getQuantity());
    }

    @Test
    public void testAjouterElementExistingItem() {
        boolean result = AddElement.ajouterElement(items, "Milk", 3);

        assertTrue(result);
        assertEquals(1, items.size());
        assertEquals(8, items.get("Milk").getQuantity());
    }

    @Test
    public void testAjouterElementNegativeQuantity() {
        boolean result = AddElement.ajouterElement(items, "Eggs", -1);

        assertFalse(result);
        assertEquals(1, items.size());
        assertFalse(items.containsKey("Eggs"));
    }

    @Test
    public void testAddSiNonExistantNewItem() {
        boolean result = AddElement.addSiNonExistant(items, "Cheese", 2);

        assertTrue(result);
        assertEquals(2, items.size());
        assertTrue(items.containsKey("Cheese"));
        assertEquals(2, items.get("Cheese").getQuantity());
    }

    @Test
    public void testAddSiNonExistantExistingItem() {
        boolean result = AddElement.addSiNonExistant(items, "Milk", 3);

        assertFalse(result);
        assertEquals(1, items.size());
        assertEquals(5, items.get("Milk").getQuantity(), "Quantity should not change");
    }

    @Test
    public void testAddSiNonExistantNegativeQuantity() {
        boolean result = AddElement.addSiNonExistant(items, "Yogurt", -1);

        assertFalse(result);
        assertEquals(1, items.size());
        assertFalse(items.containsKey("Yogurt"));
    }
}