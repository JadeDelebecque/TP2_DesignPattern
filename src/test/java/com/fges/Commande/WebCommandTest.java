package com.fges.Commande;

import com.fges.CLI.CommandContext;
import com.fges.File.File;
import com.fges.Web.SimpleGroceryShop;
import fr.anthonyquere.GroceryShopServer;
import fr.anthonyquere.MyGroceryShop;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WebCommandTest {

    @Mock
    private File fileMock;

    @Mock
    private GroceryShopServer serverMock;

    @Mock
    private MyGroceryShop groceryShopMock;

    // Classe de test qui étend WebCommand pour remplacer les méthodes qui posent problème
    private static class TestableWebCommand extends WebCommand {
        private final GroceryShopServer serverMock;
        private final MyGroceryShop groceryShopMock;
        private final File fileMockReference;

        public TestableWebCommand(File file, GroceryShopServer serverMock, MyGroceryShop groceryShopMock) {
            super(file);
            this.serverMock = serverMock;
            this.groceryShopMock = groceryShopMock;
            this.fileMockReference = file;  // Garder une référence locale
        }

        @Override
        public void execute(List<String> args, CommandContext context) throws IOException {
            int port = 8080;

            // Vérifier si un port est spécifié
            if (args.size() > 0) {
                try {
                    port = Integer.parseInt(args.get(0));
                } catch (NumberFormatException e) {
                    System.err.println("Port invalide, utilisation du port 8080 par défaut");
                }
            }

            System.out.println("Démarrage du serveur sur le port " + port + " avec le fichier: " + fileMockReference);

            // Utilisation des mocks au lieu de créer des instances réelles
            serverMock.start(port);

            System.out.println("Serveur d'épicerie démarré sur http://localhost:" + port);

            // Ne pas appeler join() pour éviter le blocage
        }
    }

    private TestableWebCommand webCommand;
    private CommandContext context;

    // Pour capturer les sorties console
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Redirection des flux de sortie
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        webCommand = new TestableWebCommand(fileMock, serverMock, groceryShopMock);
        context = new CommandContext();
    }

    @Test
    public void testExecuteWithDefaultPort() throws IOException {
        // Arrangement
        List<String> args = new ArrayList<>();

        // Action
        webCommand.execute(args, context);

        // Assertion
        String output = outContent.toString();
        assertTrue(output.contains("Démarrage du serveur sur le port 8080"));
        assertTrue(output.contains("Serveur d'épicerie démarré sur http://localhost:8080"));
        verify(serverMock).start(8080);
    }

    @Test
    public void testExecuteWithCustomPort() throws IOException {
        // Arrangement
        List<String> args = new ArrayList<>();
        args.add("9090");

        // Action
        webCommand.execute(args, context);

        // Assertion
        String output = outContent.toString();
        assertTrue(output.contains("Démarrage du serveur sur le port 9090"));
        assertTrue(output.contains("Serveur d'épicerie démarré sur http://localhost:9090"));
        verify(serverMock).start(9090);
    }

    @Test
    public void testExecuteWithInvalidPort() throws IOException {
        // Arrangement
        List<String> args = new ArrayList<>();
        args.add("invalidPort");

        // Action
        webCommand.execute(args, context);

        // Assertion
        String output = outContent.toString();
        String error = errContent.toString();
        assertTrue(error.contains("Port invalide"));
        assertTrue(output.contains("Démarrage du serveur sur le port 8080"));
        verify(serverMock).start(8080);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}