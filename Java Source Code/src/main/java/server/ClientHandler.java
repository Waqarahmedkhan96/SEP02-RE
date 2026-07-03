package server;

import shared.CreateBookingRequest;
import shared.CreateBookingResponse;
import shared.CreateCustomerRequest;
import shared.CreateCustomerResponse;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ModelManager modelManager;

    public ClientHandler(Socket socket, ModelManager modelManager) {
        this.socket = socket;
        this.modelManager = modelManager;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            Object requestObj = in.readObject();

            if (requestObj instanceof CreateCustomerRequest req) {
                CreateCustomerResponse response = modelManager.createCustomer(req);
                out.writeObject(response);
            } else if (requestObj instanceof CreateBookingRequest req) {
                CreateBookingResponse response = modelManager.createBooking(req);
                out.writeObject(response);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
