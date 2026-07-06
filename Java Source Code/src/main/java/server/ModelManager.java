package server;

import dao.BookingDAO;
import dao.CustomerDAO;
import dao.DAOFactory;
import model.Booking;
import model.Customer;
import model.DrivingLicense;
import model.state.BookingState;
import model.state.BookingStateFactory;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            List<Booking> bookings = req.isActiveOnly()
                    ? dao.findActiveByCustomerId(req.getCustomerId())
                    : dao.findByCustomerId(req.getCustomerId());
            String message = req.isActiveOnly() ? "Active bookings loaded" : "Bookings loaded";
            return new GetCustomerBookingsResponse(true, message, bookings);
        } catch (Exception e) {
            e.printStackTrace();
            return new GetCustomerBookingsResponse(false, "Database error: " + e.getMessage(), Collections.emptyList());
        }
    }

    public UpdateBookingResponse updateBooking(UpdateBookingRequest req) {
        if (req.getBookingId() <= 0) {
            return new UpdateBookingResponse(false, "Booking is required");
        }
        if (req.getVehicleId() <= 0) {
            return new UpdateBookingResponse(false, "Vehicle is required");
        }
        if (req.getEmployeeId() <= 0) {
            return new UpdateBookingResponse(false, "Employee is required");
        }
        if (req.getStartDate() == null || req.getEndDate() == null) {
            return new UpdateBookingResponse(false, "Rental period is required");
        }
        if (!req.getStartDate().isBefore(req.getEndDate())) {
            return new UpdateBookingResponse(false, "Start date must be before end date");
        }

        String status = req.getBookingStatus() == null || req.getBookingStatus().isBlank()
                ? "ACTIVE"
                : req.getBookingStatus().trim().toUpperCase();

        if (!"ACTIVE".equals(status)) {
            return new UpdateBookingResponse(false, "Update Booking can only keep ACTIVE bookings active");
        }

        try {
            BookingDAO dao = DAOFactory.getBookingDAO();
            Booking existing = dao.findById(req.getBookingId());
            if (existing == null) {
                return new UpdateBookingResponse(false, "Booking cannot be found");
            }

            BookingState currentState = BookingStateFactory.fromStatus(existing.getBookingStatus());
            try {
                currentState.update(existing);
            } catch (IllegalStateException e) {
                return new UpdateBookingResponse(false, e.getMessage());
            }

            Booking updatedBooking = new Booking(req.getStartDate(), req.getEndDate(), status,
                    existing.getCustomerId(), req.getVehicleId(), req.getEmployeeId());
            updatedBooking.setBookingId(existing.getBookingId());
            updatedBooking.setActualReturnDate(existing.getActualReturnDate());

            dao.updateActiveBooking(updatedBooking);
            return new UpdateBookingResponse(true, "Booking updated");
        } catch (Exception e) {
            e.printStackTrace();
            return new UpdateBookingResponse(false, "Database error: " + e.getMessage());
        }
    }

    public CompleteBookingResponse completeBooking(CompleteBookingRequest req) {
        if (req.getBookingId() <= 0) {
            return new CompleteBookingResponse(false, "Booking is required", 0);
        }

        try {
            BookingDAO dao = DAOFactory.getBookingDAO();

            Booking booking = dao.findById(req.getBookingId());
            if (booking == null) {
                return new CompleteBookingResponse(false, "Booking not found", 0);
            }

            BookingState currentState = BookingStateFactory.fromStatus(booking.getBookingStatus());
            BookingState nextState;
            try {
                nextState = currentState.complete(booking); // throws if transition is invalid
            } catch (IllegalStateException e) {
                return new CompleteBookingResponse(false, e.getMessage(), 0);
            }

            LocalDateTime now = LocalDateTime.now();
            double lateFee = calculateLateFee(dao, booking, now);

            dao.completeBooking(booking.getBookingId(), booking.getVehicleId(), now, nextState.getStatusName());

            String message = lateFee > 0
                    ? "Booking completed with a late fee of " + lateFee
                    : "Booking completed on time";

            return new CompleteBookingResponse(true, message, lateFee);

        } catch (Exception e) {
            e.printStackTrace();
            return new CompleteBookingResponse(false, "Database error: " + e.getMessage(), 0);
        }
    }

    public HandleOverdueReturnsResponse handleOverdueReturns(HandleOverdueReturnsRequest req) {
        try {
            BookingDAO dao = DAOFactory.getBookingDAO();
            List<Booking> candidates = dao.findOverdueCandidates();
            List<Integer> markedOverdue = new ArrayList<>();

            for (Booking booking : candidates) {
                BookingState currentState = BookingStateFactory.fromStatus(booking.getBookingStatus());
                try {
                    currentState.markOverdue(booking); // validates transition
                    dao.markOverdue(booking.getBookingId());
                    markedOverdue.add(booking.getBookingId());
                } catch (IllegalStateException ignored) {
                    // shouldn't happen since findOverdueCandidates only returns ACTIVE bookings, but stay defensive
                }
            }

            return new HandleOverdueReturnsResponse(true,
                    "Marked " + markedOverdue.size() + " booking(s) as overdue", markedOverdue);

        } catch (Exception e) {
            e.printStackTrace();
            return new HandleOverdueReturnsResponse(false, "Database error: " + e.getMessage(), List.of());
        }
    }

    private double calculateLateFee(BookingDAO dao, Booking booking, LocalDateTime actualReturnDate) throws Exception {
        if (!actualReturnDate.isAfter(booking.getEndDate())) {
            return 0; // returned on time
        }

        long minutesLate = Duration.between(booking.getEndDate(), actualReturnDate).toMinutes();
        double hoursLate = Math.ceil(minutesLate / 60.0); // round up any partial hour

        double rate = dao.getVehicleLateFeeRate(booking.getVehicleId());
        return hoursLate * rate;
    }
}
