package com.fges.Commande;

import com.fges.CLI.CommandContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InfoCommandTest {

    private InfoCommand infoCommand;
    private List<String> args;
    private CommandContext context;

    // For capturing System.out
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        infoCommand = new InfoCommand();
        args = new ArrayList<>();
        context = new CommandContext("test");

        // Set up System.out capture
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testExecute() throws IOException {
        // Execute the command
        infoCommand.execute(args, context);

        // Verify output contains expected information
        String output = outputStream.toString();
        assertTrue(output.contains("Today's date:"));
        assertTrue(output.contains("Operating System:"));
        assertTrue(output.contains("Java version:"));
    }

    @Test
    public void testDisplaySystemInfo() {
        // Call the method directly
        infoCommand.displaySystemInfo();

        // Verify output contains expected information
        String output = outputStream.toString();
        assertTrue(output.contains("Today's date:"));
        assertTrue(output.contains("Operating System:"));
        assertTrue(output.contains("Java version:"));
    }

    @Test
    public void testSystemProperties() {
        infoCommand.displaySystemInfo();

        String output = outputStream.toString();

        String osName = System.getProperty("os.name");
        assertTrue(output.contains(osName));

        String javaVersion = System.getProperty("java.version");
        assertTrue(output.contains(javaVersion));
    }
}