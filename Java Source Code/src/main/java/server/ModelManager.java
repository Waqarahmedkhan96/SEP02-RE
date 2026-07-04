package server;

import dao.BookingDAO;
import dao.CustomerDAO;
import dao.DAOFactory;
import model.Booking;
import model.Customer;
import model.DrivingLicense;
import shared.CreateBookingRequest;
import shared.CreateBookingResponse;
import shared.CreateCustomerRequest;
import shared.CreateCustomerResponse;
import shared.GetCustomerBookingsRequest;
import shared.GetCustomerBookingsResponse;

import java.util.Collections;

public class ModelManager {

    public CreateCustomerResponse createCustomer(CreateCustomerRequest req) {
        // basic validation — expand as your team defines actual rules
        if (req.getName() == null || req.getName().isBlank()) {
            return new CreateCustomerResponse(false, "Name is required", -1);
        }
        if (req.getLicenseNo() == null || req.getLicenseNo().isBlank()) {
            return new CreateCustomerResponse(false, "License number is required", -1);
        }

        Customer customer = new Customer(req.getName(), req.getPhoneNo(), req.getEmail(),
                req.getCpr(), req.getPassportNo(), req.getLicenseNo());

        DrivingLicense license = new DrivingLicense(req.getLicenseNo(),
                req.isCategoryA(), req.isCategoryB(), req.isCategoryC(), req.isCategoryD());

        try {
            CustomerDAO dao = DAOFactory.getCustomerDAO();
            int newId = dao.create(customer, license);
            return new CreateCustomerResponse(true, "Customer created", newId);
        } catch (Exception e) {
            e.printStackTrace();
            return new CreateCustomerResponse(false, "Database error: " + e.getMessage(), -1);
        }
    }

    public CreateBookingResponse createBooking(CreateBookingRequest req) {
        if (req.getCustomerId() <= 0) {
            return new CreateBookingResponse(false, "Customer is required", -1);
        }
        if (req.getVehicleId() <= 0) {
            return new CreateBookingResponse(false, "Vehicle is required", -1);
        }
        if (req.getEmployeeId() <= 0) {
            return new CreateBookingResponse(false, "Employee is required", -1);
        }
        if (req.getStartDate() == null || req.getEndDate() == null) {
            return new CreateBookingResponse(false, "Rental period is required", -1);
        }
        if (!req.getStartDate().isBefore(req.getEndDate())) {
            return new CreateBookingResponse(false, "Start date must be before end date", -1);
        }

        Booking booking = new Booking(req.getStartDate(), req.getEndDate(), "ACTIVE",
                req.getCustomerId(), req.getVehicleId(), req.getEmployeeId());

        try {
            BookingDAO dao = DAOFactory.getBookingDAO();
            int newId = dao.create(booking);
            return new CreateBookingResponse(true, "Booking created", newId);
        } catch (Exception e) {
            e.printStackTrace();
            return new CreateBookingResponse(false, "Database error: " + e.getMessage(), -1);
        }
    }

    public GetCustomerBookingsResponse getCustomerBookings(GetCustomerBookingsRequest req) {
        if (req.getCustomerId() <= 0) {
            return new GetCustomerBookingsResponse(false, "Customer is required", Collections.emptyList());
        }

        try {
            BookingDAO dao = DAOFactory.getBookingDAO();
            return new GetCustomerBookingsResponse(true, "Bookings loaded", dao.findByCustomerId(req.getCustomerId()));
        } catch (Exception e) {
            e.printStackTrace();
            return new GetCustomerBookingsResponse(false, "Database error: " + e.getMessage(), Collections.emptyList());
        }
    }
}
