package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import application.DatabaseSeeder;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            DatabaseSeeder.seedTestData();
            System.out.println("Seed test data loaded.");
        } catch (IOException | SQLException e) {
            System.err.println("Could not seed test data. Server startup stopped.");
            e.printStackTrace();
            return;
        }

        ModelManager modelManager = new ModelManager();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, modelManager)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
