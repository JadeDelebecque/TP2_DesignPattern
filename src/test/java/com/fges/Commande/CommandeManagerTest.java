package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandeManagerTest {

    @Mock
    private File mockFile;

    @Mock
    private Command mockCommand;

    @Mock
    private CommandRegistry mockRegistry;

    private CommandeManager manager;
    private List<String> testArgs;
    private CommandContext testContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new CommandeManager(mockFile);
        testArgs = Arrays.asList("item1", "5");
        testContext = new CommandContext("test");

        // Replace the real registry with our mock
        ReflectionUtils.setField(manager, "registry", mockRegistry);
    }

    @Test
    public void testExecuteCommandSuccess() throws IOException {
        // Arrange
        when(mockRegistry.getCommand("add")).thenReturn(mockCommand);

        // Act
        manager.executeCommand("add", testArgs, testContext);

        // Assert
        verify(mockRegistry).getCommand("add");
        verify(mockCommand).execute(testArgs, testContext);
    }

    @Test
    public void testExecuteCommandWithIllegalArgumentException() {
        // Arrange
        when(mockRegistry.getCommand("unknown")).thenThrow(new IllegalArgumentException("Command not found"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.executeCommand("unknown", testArgs, testContext);
        });

        assertEquals("Command not found", exception.getMessage());
        verify(mockRegistry).getCommand("unknown");
        verifyNoInteractions(mockCommand);
    }

    @Test
    public void testExecuteCommandWithIOException() throws IOException {
        // Arrange
        when(mockRegistry.getCommand("add")).thenReturn(mockCommand);
        doThrow(new IOException("File error")).when(mockCommand).execute(any(List.class), any(CommandContext.class));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.executeCommand("add", testArgs, testContext);
        });

        assertEquals("Erreur lors de l'écriture du fichier", exception.getMessage());
        verify(mockRegistry).getCommand("add");
        verify(mockCommand).execute(testArgs, testContext);
    }

    @Test
    public void testGetRegistry() {
        // Arrange & Act
        CommandRegistry actualRegistry = manager.getRegistry();

        // Assert
        assertNotNull(actualRegistry);
    }

    @Test
    public void testWithRealRegistry() throws IOException {
        // Create a manager with the real registry
        manager = new CommandeManager(mockFile);

        // Test that the registry is properly initialized with commands
        CommandRegistry registry = manager.getRegistry();

        assertNotNull(registry);
        assertNotNull(registry.getCommand("add"));
        assertNotNull(registry.getCommand("remove"));
        assertNotNull(registry.getCommand("info"));
    }

    /**
     * Helper class for reflection operations, to inject mocks into private fields
     */
    private static class ReflectionUtils {
        public static void setField(Object target, String fieldName, Object value) {
            try {
                java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
            } catch (Exception e) {
                throw new RuntimeException("Failed to set field: " + fieldName, e);
            }
        }
    }
}