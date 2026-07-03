package viewmodel;

import client.Client;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.CreateBookingRequest;
import shared.CreateBookingResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class BookingViewModel {

    private final Client client = new Client();

    public final IntegerProperty customerId = new SimpleIntegerProperty();
    public final IntegerProperty vehicleId = new SimpleIntegerProperty();
    public final IntegerProperty employeeId = new SimpleIntegerProperty();
    public final StringProperty startDate = new SimpleStringProperty();
    public final StringProperty endDate = new SimpleStringProperty();
    public final StringProperty statusMessage = new SimpleStringProperty();

    public void submit() {
        try {
            CreateBookingRequest req = new CreateBookingRequest(
                    customerId.get(),
                    vehicleId.get(),
                    employeeId.get(),
                    LocalDateTime.parse(startDate.get()),
                    LocalDateTime.parse(endDate.get())
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
}
