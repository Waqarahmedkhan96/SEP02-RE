package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.CheckAvailabilityRequest;
import shared.CheckAvailabilityResponse;
import shared.CancelBookingRequest;
import shared.CancelBookingResponse;
import shared.CompleteBookingRequest;
import shared.CompleteBookingResponse;
import shared.CreateBookingRequest;
import shared.CreateBookingResponse;
import shared.CreateCustomerRequest;
import shared.CreateCustomerResponse;
import shared.CreateVehicleRequest;
import shared.CreateVehicleResponse;
import shared.FilterVehiclesRequest;
import shared.FilterVehiclesResponse;
import shared.GetCustomerBookingsRequest;
import shared.GetCustomerBookingsResponse;
import shared.GetCustomersRequest;
import shared.GetCustomersResponse;
import shared.GetBookingsRequest;
import shared.GetBookingsResponse;
import shared.GetVehiclesRequest;
import shared.GetVehiclesResponse;
import shared.HandleOverdueReturnsRequest;
import shared.HandleOverdueReturnsResponse;
import shared.RemoveVehicleRequest;
import shared.RemoveVehicleResponse;
import shared.SearchBookingsRequest;
import shared.SearchBookingsResponse;
import shared.UpdateBookingRequest;
import shared.UpdateBookingResponse;
import shared.UpdateCustomerRequest;
import shared.UpdateCustomerResponse;
import shared.UpdateVehicleRequest;
import shared.UpdateVehicleResponse;

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
            } else if (requestObj instanceof GetBookingsRequest req) {
                GetBookingsResponse response = modelManager.getBookings(req);
                out.writeObject(response);
            } else if (requestObj instanceof GetCustomersRequest req) {
                GetCustomersResponse response = modelManager.getCustomers(req);
                out.writeObject(response);
            } else if (requestObj instanceof UpdateCustomerRequest req) {
                UpdateCustomerResponse response = modelManager.updateCustomer(req);
                out.writeObject(response);
            } else if (requestObj instanceof SearchBookingsRequest req) {
                SearchBookingsResponse response = modelManager.searchBookings(req);
                out.writeObject(response);
            } else if (requestObj instanceof UpdateBookingRequest req) {
                UpdateBookingResponse response = modelManager.updateBooking(req);
                out.writeObject(response);
            } else if (requestObj instanceof CompleteBookingRequest req) {
                CompleteBookingResponse response = modelManager.completeBooking(req);
                out.writeObject(response);
            } else if (requestObj instanceof CancelBookingRequest req) {
                CancelBookingResponse response = modelManager.cancelBooking(req);
                out.writeObject(response);
            } else if (requestObj instanceof HandleOverdueReturnsRequest req) {
                HandleOverdueReturnsResponse response = modelManager.handleOverdueReturns(req);
                out.writeObject(response);
            } else if (requestObj instanceof CreateVehicleRequest req) {
                CreateVehicleResponse response = modelManager.createVehicle(req);
                out.writeObject(response);
            } else if (requestObj instanceof UpdateVehicleRequest req) {
                UpdateVehicleResponse response = modelManager.updateVehicle(req);
                out.writeObject(response);
            } else if (requestObj instanceof RemoveVehicleRequest req) {
                RemoveVehicleResponse response = modelManager.removeVehicle(req);
                out.writeObject(response);
            } else if (requestObj instanceof GetVehiclesRequest req) {
              GetVehiclesResponse response = modelManager.getVehicles(req);
              out.writeObject(response);
            } else if (requestObj instanceof FilterVehiclesRequest req) {
              FilterVehiclesResponse response = modelManager.filterVehicles(req);
              out.writeObject(response);
            } else if (requestObj instanceof CheckAvailabilityRequest req) {
              CheckAvailabilityResponse response = modelManager.checkAvailability(req);
              out.writeObject(response);
            }
            
            

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }

    }
}
