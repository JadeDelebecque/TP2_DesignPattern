package com.fges.Commande;

import com.fges.File.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandRegistryTest {

    @Mock
    private File mockFile;

    @Mock
    private Command mockCommand;

    private CommandRegistry registry;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registry = new CommandRegistry(mockFile);
    }

    @Test
    public void testDefaultCommandsInitialized() {
        // Verify all default commands are registered
        assertTrue(registry.hasCommand("add"));
        assertTrue(registry.hasCommand("list"));
        assertTrue(registry.hasCommand("remove"));
        assertTrue(registry.hasCommand("info"));
        assertTrue(registry.hasCommand("display"));

        // Verify commands are case insensitive
        assertTrue(registry.hasCommand("ADD"));
        assertTrue(registry.hasCommand("List"));
        assertTrue(registry.hasCommand("REMOVE"));
    }

    @Test
    public void testGetCommandSuccess() {
        // Get existing commands
        assertNotNull(registry.getCommand("add"));
        assertNotNull(registry.getCommand("list"));
        assertNotNull(registry.getCommand("remove"));
        assertNotNull(registry.getCommand("info"));
        assertNotNull(registry.getCommand("display"));

        // Test case insensitivity
        assertNotNull(registry.getCommand("ADD"));
        assertNotNull(registry.getCommand("List"));
    }

    @Test
    public void testGetCommandNotFound() {
        // Verify exception is thrown for unknown command
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            registry.getCommand("unknown");
        });

        assertEquals("Commande inconnue: unknown", exception.getMessage());
    }

    @Test
    public void testRegisterNewCommand() {
        // Register a new custom command
        registry.register("custom", mockCommand);

        // Verify it was registered
        assertTrue(registry.hasCommand("custom"));
        assertEquals(mockCommand, registry.getCommand("custom"));

        // Verify case-insensitivity for custom commands
        assertTrue(registry.hasCommand("CUSTOM"));
        assertEquals(mockCommand, registry.getCommand("CUSTOM"));
    }

    @Test
    public void testOverrideExistingCommand() {
        Command originalAddCommand = registry.getCommand("add");

        registry.register("add", mockCommand);

        assertTrue(registry.hasCommand("add"));
        assertEquals(mockCommand, registry.getCommand("add"));
        assertNotEquals(originalAddCommand, registry.getCommand("add"));
    }

    @Test
    public void testHasCommandReturnsFalse() {
        // Verify hasCommand returns false for non-existing command
        assertFalse(registry.hasCommand("nonexistent"));
        assertFalse(registry.hasCommand(""));
    }

    @Test
    public void testHasCommandWithNullExpectNullPointerException() {
        // The current implementation throws NPE for null command name
        assertThrows(NullPointerException.class, () -> {
            registry.hasCommand(null);
        });
    }

    @Test
    public void testRegistryWithCustomFile() {
        CommandRegistry newRegistry = new CommandRegistry(mockFile);
        Command addCommand = newRegistry.getCommand("add");
        assertTrue(addCommand instanceof AddElement);
    }

    @Test
    public void testRegistryWithNullFile() {
        try {
            CommandRegistry nullFileRegistry = new CommandRegistry(null);
            assertTrue(nullFileRegistry.hasCommand("info"));
        } catch (NullPointerException e) {
            // This is an acceptable outcome if nulls aren't handled
        }
    }
}