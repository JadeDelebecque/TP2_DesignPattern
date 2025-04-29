package com.fges.CLI;

import org.apache.commons.cli.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CommandLineHandlerTest {

    private CommandOptions options;

    @BeforeEach
    public void setUp() {
        options = new CommandOptions();
    }

    @Test
    public void testHasCommand() {
        String[] args = {"add", "Pommes", "5", "-s", "file.json"};
        CommandLineHandler handler = new CommandLineHandler(args);

        assertTrue(handler.hasCommand("add"));
        assertFalse(handler.hasCommand("remove"));
    }

    @Test
    public void testParse() {
        String[] args = {"add", "Pommes", "5", "-s", "file.json"};
        CommandLineHandler handler = new CommandLineHandler(args);

        assertTrue(handler.parse(options));
        assertEquals("file.json", handler.getOptionValue("s"));
    }

    @Test
    public void testParseWithMissingRequiredOption() {
        String[] args = {"add", "Pommes", "5"};
        CommandLineHandler handler = new CommandLineHandler(args);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            handler.parse(options);
        });

        assertTrue(exception.getMessage().contains("Erreur de parsing"));
    }

    @Test
    public void testParseWithInfoCommandNoSource() {
        String[] args = {"info"};
        CommandLineHandler handler = new CommandLineHandler(args);

        options.configureForCommand("info");
        assertTrue(handler.parse(options));
    }

    @Test
    public void testGetCommand() {
        String[] args = {"add", "Pommes", "5", "-s", "file.json"};
        CommandLineHandler handler = new CommandLineHandler(args);

        handler.parse(options);
        assertEquals("add", handler.getCommand());
    }

    @Test
    public void testGetCommandWithMissingCommand() {
        String[] args = {"-s", "file.json"};
        CommandLineHandler handler = new CommandLineHandler(args);

        handler.parse(options);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            handler.getCommand();
        });

        assertEquals("Commande manquante", exception.getMessage());
    }

    @Test
    public void testGetCommandArgs() {
        String[] args = {"add", "Pommes", "5", "-s", "file.json"};
        CommandLineHandler handler = new CommandLineHandler(args);

        handler.parse(options);
        List<String> commandArgs = handler.getCommandArgs();

        assertEquals(2, commandArgs.size());
        assertEquals("Pommes", commandArgs.get(0));
        assertEquals("5", commandArgs.get(1));
    }

    @Test
    public void testGetOptionValue() {
        String[] args = {"add", "Pommes", "5", "-s", "file.json", "--category", "Fruits"};
        CommandLineHandler handler = new CommandLineHandler(args);

        handler.parse(options);

        assertEquals("file.json", handler.getOptionValue("s"));
        assertEquals("file.json", handler.getOptionValue("source"));
        assertEquals("Fruits", handler.getOptionValue("c"));
        assertEquals("Fruits", handler.getOptionValue("category"));
        assertNull(handler.getOptionValue("nonexistent"));
    }

    @Test
    public void testHasOption() {
        String[] args = {"add", "Pommes", "5", "-s", "file.json"};
        CommandLineHandler handler = new CommandLineHandler(args);

        handler.parse(options);

        assertTrue(handler.hasOption("s"));
        assertTrue(handler.hasOption("source"));
        assertFalse(handler.hasOption("c"));
        assertFalse(handler.hasOption("category"));
    }

    @Test
    public void testCommandWithNoArgs() {
        String[] args = {"info", "-s", "file.json"};
        CommandLineHandler handler = new CommandLineHandler(args);

        options.configureForCommand("info");
        handler.parse(options);

        assertEquals("info", handler.getCommand());
        assertTrue(handler.getCommandArgs().isEmpty());
    }
}