package com.fges.CLI;

import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CommandOptionsTest {

    private CommandOptions commandOptions;

    @BeforeEach
    public void setUp() {
        commandOptions = new CommandOptions();
    }

    @Test
    public void testDefaultOptionsConfiguration() {
        Options options = commandOptions.getOptions();

        // Verify source option exists and is required by default
        assertTrue(options.hasOption("s"));
        assertTrue(options.getOption("s").isRequired());

        // Verify category option exists and is not required
        assertTrue(options.hasOption("c"));
        assertFalse(options.getOption("c").isRequired());
    }

    @Test
    public void testConfigureForInfoCommand() {
        // Configure for info command
        commandOptions.configureForCommand("info");
        Options options = commandOptions.getOptions();

        // Verify source is not required for info command
        assertTrue(options.hasOption("s"));
        assertFalse(options.getOption("s").isRequired());
    }

    @Test
    public void testConfigureForAddCommand() {
        // Configure for add command
        commandOptions.configureForCommand("add");
        Options options = commandOptions.getOptions();

        // Verify source is required for add command
        assertTrue(options.hasOption("s"));
        assertTrue(options.getOption("s").isRequired());
    }

    @Test
    public void testSetSourceRequired() {
        // Test setting source to not required
        commandOptions.setSourceRequired(false);
        Options options = commandOptions.getOptions();
        assertFalse(options.getOption("s").isRequired());

        // Test setting source back to required
        commandOptions.setSourceRequired(true);
        options = commandOptions.getOptions();
        assertTrue(options.getOption("s").isRequired());
    }

    @Test
    public void testOptionDescriptions() {
        Options options = commandOptions.getOptions();

        // Verify descriptions are set correctly
        assertEquals("File containing the grocery list", options.getOption("s").getDescription());
        assertEquals("Category of the item", options.getOption("c").getDescription());
    }

    @Test
    public void testLongOptionNames() {
        Options options = commandOptions.getOptions();

        // Verify long option names are set correctly
        assertEquals("source", options.getOption("s").getLongOpt());
        assertEquals("category", options.getOption("c").getLongOpt());
    }
}