package client;

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

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (CreateCustomerResponse) in.readObject();
        }
    }

    public CreateBookingResponse createBooking(CreateBookingRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (CreateBookingResponse) in.readObject();
        }
    }

    public GetCustomerBookingsResponse getCustomerBookings(GetCustomerBookingsRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (GetCustomerBookingsResponse) in.readObject();
        }
    }

    public GetBookingsResponse getBookings(GetBookingsRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (GetBookingsResponse) in.readObject();
        }
    }

    public GetCustomersResponse getCustomers(GetCustomersRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (GetCustomersResponse) in.readObject();
        }
    }

    public UpdateCustomerResponse updateCustomer(UpdateCustomerRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (UpdateCustomerResponse) in.readObject();
        }
    }

    public SearchBookingsResponse searchBookings(SearchBookingsRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (SearchBookingsResponse) in.readObject();
        }
    }

    public UpdateBookingResponse updateBooking(UpdateBookingRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (UpdateBookingResponse) in.readObject();
        }
    }

    public CompleteBookingResponse completeBooking(CompleteBookingRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (CompleteBookingResponse) in.readObject();
        }
    }

    public CancelBookingResponse cancelBooking(CancelBookingRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (CancelBookingResponse) in.readObject();
        }
    }

    public HandleOverdueReturnsResponse handleOverdueReturns(HandleOverdueReturnsRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (HandleOverdueReturnsResponse) in.readObject();
        }
    }

    public GetVehiclesResponse getVehicles(GetVehiclesRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (GetVehiclesResponse) in.readObject();
        }
    }

    public CreateVehicleResponse createVehicle(CreateVehicleRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (CreateVehicleResponse) in.readObject();
        }
    }

    public UpdateVehicleResponse updateVehicle(UpdateVehicleRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (UpdateVehicleResponse) in.readObject();
        }
    }

    public RemoveVehicleResponse removeVehicle(RemoveVehicleRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (RemoveVehicleResponse) in.readObject();
        }
    }

    public FilterVehiclesResponse filterVehicles(FilterVehiclesRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (FilterVehiclesResponse) in.readObject();
        }
    }

    public CheckAvailabilityResponse checkAvailability(CheckAvailabilityRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (CheckAvailabilityResponse) in.readObject();
        }
    }
}
