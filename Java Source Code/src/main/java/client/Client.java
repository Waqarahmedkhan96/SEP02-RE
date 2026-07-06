package client;

import shared.CompleteBookingRequest;
import shared.CompleteBookingResponse;
import shared.CreateBookingRequest;
import shared.CreateBookingResponse;
import shared.CreateCustomerRequest;
import shared.CreateCustomerResponse;
import shared.GetCustomerBookingsRequest;
import shared.GetCustomerBookingsResponse;
import shared.HandleOverdueReturnsRequest;
import shared.HandleOverdueReturnsResponse;
import shared.UpdateBookingRequest;
import shared.UpdateBookingResponse;

import java.io.*;
import java.net.Socket;

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

    public HandleOverdueReturnsResponse handleOverdueReturns(HandleOverdueReturnsRequest request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (HandleOverdueReturnsResponse) in.readObject();
        }
    }
}
