package com.fges.Commande;

import com.fges.CLI.CommandContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CommandTest {

    // Create a concrete implementation of Command for testing
    private static class TestCommand implements Command {
        private boolean executed = false;
        private List<String> lastArgs;
        private CommandContext lastContext;

        @Override
        public void execute(List<String> args, CommandContext context) throws IOException {
            this.executed = true;
            this.lastArgs = args;
            this.lastContext = context;
        }

        public boolean isExecuted() {
            return executed;
        }

        public List<String> getLastArgs() {
            return lastArgs;
        }

        public CommandContext getLastContext() {
            return lastContext;
        }
    }

    private TestCommand command;
    private List<String> testArgs;

    @BeforeEach
    public void setUp() {
        command = new TestCommand();
        testArgs = Arrays.asList("arg1", "arg2");
    }

    @Test
    public void testExecuteWithContextDirectly() throws IOException {
        // Prepare
        CommandContext context = new CommandContext("TestCategory");

        // Execute
        command.execute(testArgs, context);

        // Verify
        assertTrue(command.isExecuted());
        assertEquals(testArgs, command.getLastArgs());
        assertEquals(context, command.getLastContext());
        assertEquals("TestCategory", command.getLastContext().getCategory());
    }

    @Test
    public void testExecuteWithCategoryString() throws IOException {
        // Execute
        command.execute(testArgs, "TestCategory");

        // Verify
        assertTrue(command.isExecuted());
        assertEquals(testArgs, command.getLastArgs());
        assertNotNull(command.getLastContext());
        assertEquals("TestCategory", command.getLastContext().getCategory());
    }

    @Test
    public void testExecuteWithNullCategory() throws IOException {
        // Execute
        command.execute(testArgs, (String)null);

        // Verify
        assertTrue(command.isExecuted());
        assertEquals(testArgs, command.getLastArgs());
        assertNotNull(command.getLastContext());
        assertNull(command.getLastContext().getCategory());
        assertFalse(command.getLastContext().hasCategory());
    }

    @Test
    public void testExecuteWithEmptyCategory() throws IOException {
        // Execute
        command.execute(testArgs, "");

        // Verify
        assertTrue(command.isExecuted());
        assertEquals(testArgs, command.getLastArgs());
        assertNotNull(command.getLastContext());
        assertEquals("", command.getLastContext().getCategory());
        assertFalse(command.getLastContext().hasCategory());
    }

    @Test
    public void testExecuteWithMockedCommand() throws IOException {
        // Create a mock instead of a concrete implementation
        Command mockedCommand = mock(Command.class);

        // Set up the mock to call the real method for the default method
        doCallRealMethod().when(mockedCommand).execute(any(List.class), anyString());

        // Execute
        mockedCommand.execute(testArgs, "MockCategory");

        // Verify the concrete method was called with the right context
        verify(mockedCommand).execute(eq(testArgs), any(CommandContext.class));
    }

    @Test
    public void testExecuteWithMockedCommandVerifyContext() throws IOException {
        // Create a mock instead of a concrete implementation
        Command mockedCommand = spy(new Command() {
            @Override
            public void execute(List<String> args, CommandContext context) throws IOException {
                // Just a spy implementation
            }
        });

        // Execute
        mockedCommand.execute(testArgs, "MockCategory");

        // Verify the concrete method was called with a context containing the right category
        verify(mockedCommand).execute(eq(testArgs), argThat(new ArgumentMatcher<CommandContext>() {
            @Override
            public boolean matches(CommandContext context) {
                return "MockCategory".equals(context.getCategory()) && context.hasCategory();
            }
        }));
    }

    @Test
    public void testExceptionPropagation() {
        // Create a command that throws an exception
        Command exceptionCommand = new Command() {
            @Override
            public void execute(List<String> args, CommandContext context) throws IOException {
                throw new IOException("Test exception");
            }
        };

        // Execute and verify exception propagation
        Exception exception = assertThrows(IOException.class, () -> {
            exceptionCommand.execute(testArgs, "TestCategory");
        });

        assertEquals("Test exception", exception.getMessage());
    }
}