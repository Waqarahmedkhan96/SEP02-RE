package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.CompleteBookingRequest;
import shared.CompleteBookingResponse;
import shared.CreateBookingRequest;
import shared.CreateBookingResponse;
import shared.CreateCustomerRequest;
import shared.CreateCustomerResponse;
import shared.FilterVehiclesRequest;
import shared.FilterVehiclesResponse;
import shared.GetCustomerBookingsRequest;
import shared.GetCustomerBookingsResponse;
import shared.GetVehiclesRequest;
import shared.GetVehiclesResponse;
import shared.HandleOverdueReturnsRequest;
import shared.HandleOverdueReturnsResponse;

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
            } else if (requestObj instanceof GetCustomerBookingsRequest req) {
                GetCustomerBookingsResponse response = modelManager.getCustomerBookings(req);
                out.writeObject(response);
            } else if (requestObj instanceof CompleteBookingRequest req) {
                CompleteBookingResponse response = modelManager.completeBooking(req);
                out.writeObject(response);
            } else if (requestObj instanceof HandleOverdueReturnsRequest req) {
                HandleOverdueReturnsResponse response = modelManager.handleOverdueReturns(req);
                out.writeObject(response);
            } else if (requestObj instanceof GetVehiclesRequest req) {
              GetVehiclesResponse response = modelManager.getVehicles(req);
              out.writeObject(response);
            } else if (requestObj instanceof FilterVehiclesRequest req) {
              FilterVehiclesResponse response = modelManager.filterVehicles(req);
              out.writeObject(response);
            }
            

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}