package viewmodel;

import client.Client;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Booking;
import shared.CancelBookingRequest;
import shared.CancelBookingResponse;
import shared.CreateBookingRequest;
import shared.CreateBookingResponse;
import shared.GetCustomerBookingsRequest;
import shared.GetCustomerBookingsResponse;
import shared.SearchBookingsRequest;
import shared.SearchBookingsResponse;
import shared.UpdateBookingRequest;
import shared.UpdateBookingResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class BookingViewModel {

    private final Client client = new Client();

    public final IntegerProperty customerId = new SimpleIntegerProperty();
    public final IntegerProperty bookingId = new SimpleIntegerProperty();
    public final IntegerProperty vehicleId = new SimpleIntegerProperty();
    public final IntegerProperty employeeId = new SimpleIntegerProperty();
    public final StringProperty startDate = new SimpleStringProperty();
    public final StringProperty endDate = new SimpleStringProperty();
    public final StringProperty bookingStatus = new SimpleStringProperty("ACTIVE");
    public final StringProperty statusMessage = new SimpleStringProperty();
    public final ObservableList<Booking> customerBookings = FXCollections.observableArrayList();
    public final ObservableList<Booking> cancellableBookings = FXCollections.observableArrayList();
    public final ObservableList<Booking> archivedBookings = FXCollections.observableArrayList();

    public void submit() {
        try {
            CreateBookingRequest req = new CreateBookingRequest(
                    customerId.get(),
                    vehicleId.get(),
                    employeeId.get(),
                    parseDate(startDate.get()),
                    parseDate(endDate.get())
            );

            CreateBookingResponse res = client.createBooking(req);
            if (res.isSuccess()) {
                statusMessage.set("Created booking #" + res.getBookingId());
            } else {
                statusMessage.set("Failed: " + res.getMessage());
            }
        } catch (DateTimeParseException e) {
            statusMessage.set("Use date format yyyy-MM-ddTHH:mm");
        } catch (Exception e) {
            statusMessage.set("Connection error: " + e.getMessage());
        }
    }

    public void loadCustomerBookings() {
        loadCustomerBookings(false);
    }

    public void loadActiveCustomerBookings() {
        loadCustomerBookings(true);
    }

    public boolean updateBooking() {
        try {
            UpdateBookingRequest req = new UpdateBookingRequest(
                    bookingId.get(),
                    vehicleId.get(),
                    employeeId.get(),
                    parseDate(startDate.get()),
                    parseDate(endDate.get()),
                    bookingStatus.get()
            );

            UpdateBookingResponse res = client.updateBooking(req);
            if (res.isSuccess()) {
                statusMessage.set("Updated booking #" + bookingId.get());
                return true;
            } else {
                statusMessage.set("Failed: " + res.getMessage());
                return false;
            }
        } catch (DateTimeParseException e) {
            statusMessage.set("Select start and end dates");
            return false;
        } catch (Exception e) {
            statusMessage.set("Connection error: " + e.getMessage());
            return false;
        }
    }

    public boolean searchCancellableBookings(String query) {
        try {
            SearchBookingsResponse res = client.searchBookings(new SearchBookingsRequest(query, true));
            if (res.isSuccess()) {
                cancellableBookings.setAll(res.getBookings());
                statusMessage.set(res.getMessage() + " (" + res.getBookings().size() + ")");
                return true;
            } else {
                cancellableBookings.clear();
                statusMessage.set("Failed: " + res.getMessage());
                return false;
            }
        } catch (Exception e) {
            cancellableBookings.clear();
            statusMessage.set("Connection error: " + e.getMessage());
            return false;
        }
    }

    public void loadArchivedBookings(String customerQuery, String vehicleQuery, String dateQuery) {
        try {
            SearchBookingsResponse res = client.searchBookings(
                    new SearchBookingsRequest(customerQuery, vehicleQuery, dateQuery, true)
            );

            if (res.isSuccess()) {
                archivedBookings.setAll(res.getBookings());
                statusMessage.set(res.getMessage());
            } else {
                archivedBookings.clear();
                statusMessage.set("Failed: " + res.getMessage());
            }
        } catch (Exception e) {
            archivedBookings.clear();
            statusMessage.set("Connection error: " + e.getMessage());
        }
    }

    public boolean cancelBooking(int bookingId) {
        try {
            CancelBookingResponse res = client.cancelBooking(new CancelBookingRequest(bookingId));
            if (res.isSuccess()) {
                statusMessage.set(res.getMessage());
                return true;
            } else {
                statusMessage.set("Failed: " + res.getMessage());
                return false;
            }
        } catch (Exception e) {
            statusMessage.set("Connection error: " + e.getMessage());
            return false;
        }
    }

    private void loadCustomerBookings(boolean activeOnly) {
        try {
            GetCustomerBookingsResponse res = client.getCustomerBookings(
                    new GetCustomerBookingsRequest(customerId.get(), activeOnly)
            );

            if (res.isSuccess()) {
                customerBookings.setAll(res.getBookings());
                String type = activeOnly ? "active booking" : "booking record";
                statusMessage.set("Loaded " + res.getBookings().size() + " " + type + "(s)");
            } else {
                customerBookings.clear();
                statusMessage.set("Failed: " + res.getMessage());
            }
        } catch (Exception e) {
            customerBookings.clear();
            statusMessage.set("Connection error: " + e.getMessage());
        }
    }

    private LocalDateTime parseDate(String value) {
        return LocalDate.parse(value).atStartOfDay();
    }
}
