package com.fges.CLI;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandContextTest {

    @Test
    public void testDefaultConstructor() {
        CommandContext context = new CommandContext();

        assertNull(context.getCategory());
        assertFalse(context.hasCategory());
    }

    @Test
    public void testParameterizedConstructor() {
        CommandContext context = new CommandContext("Fruits");

        assertEquals("Fruits", context.getCategory());
        assertTrue(context.hasCategory());
    }

    @Test
    public void testSetCategory() {
        CommandContext context = new CommandContext();

        context.setCategory("Légumes");
        assertEquals("Légumes", context.getCategory());
        assertTrue(context.hasCategory());
    }

    @Test
    public void testHasCategoryWithEmptyString() {
        CommandContext context = new CommandContext("");

        assertFalse(context.hasCategory());
    }

    @Test
    public void testHasCategoryWithNullValue() {
        CommandContext context = new CommandContext();

        assertFalse(context.hasCategory());
    }

    @Test
    public void testCategoryChangeAfterInitialization() {
        CommandContext context = new CommandContext("Fruits");
        assertEquals("Fruits", context.getCategory());

        context.setCategory("Produits Laitiers");
        assertEquals("Produits Laitiers", context.getCategory());
    }

    @Test
    public void testCategorySetToNull() {
        CommandContext context = new CommandContext("Fruits");
        assertTrue(context.hasCategory());

        context.setCategory(null);
        assertNull(context.getCategory());
        assertFalse(context.hasCategory());
    }

    @Test
    public void testCategorySetToEmptyString() {
        CommandContext context = new CommandContext("Fruits");
        assertTrue(context.hasCategory());

        context.setCategory("");
        assertFalse(context.hasCategory());
    }
}