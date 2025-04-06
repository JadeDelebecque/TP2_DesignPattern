package com.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GroceryItemTest {

    @Test
    public void testConstructor() {
        GroceryItem item = new GroceryItem("Milk", 10);
        assertEquals("Milk", item.getName());
        assertEquals(10, item.getQuantity());
    }

    @Test
    public void testDefaultConstructor() {
        GroceryItem item = new GroceryItem();
        assertNull(item.getName());
        assertEquals(0, item.getQuantity());
    }

    @Test
    public void testSettersGetters() {
        GroceryItem item = new GroceryItem();
        item.setName("Eggs");
        item.setQuantity(12);
        assertEquals("Eggs", item.getName());
        assertEquals(12, item.getQuantity());
    }

    @Test
    public void testAddQuantity() {
        GroceryItem item = new GroceryItem("Bread", 2);
        item.addQuantity(3);
        assertEquals(5, item.getQuantity());
    }

    @Test
    public void testAddNegativeQuantity() {
        GroceryItem item = new GroceryItem("Bread", 2);
        item.addQuantity(-1);
        assertEquals(2, item.getQuantity(), "Quantity should not change when adding negative value");
    }

    @Test
    public void testToString() {
        GroceryItem item = new GroceryItem("Cheese", 5);
        assertEquals("Cheese: 5", item.toString());
    }

    @Test
    public void testConstructorWithCategory() {
        GroceryItem item = new GroceryItem("Milk", 10, "dairy");
        assertEquals("Milk", item.getName());
        assertEquals(10, item.getQuantity());
        assertEquals("dairy", item.getCategory());
    }

    @Test
    public void testDefaultCategory() {
        GroceryItem item = new GroceryItem("Bread", 2);
        assertEquals("default", item.getCategory());
    }

    @Test
    public void testSetGetCategory() {
        GroceryItem item = new GroceryItem();
        item.setCategory("fruits");
        assertEquals("fruits", item.getCategory());
    }

    @Test
    public void testNullCategory() {
        GroceryItem item = new GroceryItem("Apple", 5, null);
        assertEquals("default", item.getCategory());
    }

    @Test
    public void testEmptyCategory() {
        GroceryItem item = new GroceryItem("Banana", 3, "");
        assertEquals("default", item.getCategory());

        item.setCategory("  ");
        assertEquals("default", item.getCategory());
    }
}