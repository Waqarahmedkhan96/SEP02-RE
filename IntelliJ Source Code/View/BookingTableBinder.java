package view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Booking;

final class BookingTableBinder {
    private BookingTableBinder() {
    }

    static void wireBookingTable(TableView<Booking> table,
                                 TableColumn<Booking, Number> bookingColumn,
                                 TableColumn<Booking, String> startColumn,
                                 TableColumn<Booking, String> endColumn,
                                 TableColumn<Booking, String> bookingStatusColumn,
                                 TableColumn<Booking, Number> vehicleColumn) {
        bookingColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBookingId()));
        startColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStartDate())));
        endColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEndDate())));
        bookingStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookingStatus()));
        vehicleColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getVehicleId()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }
}
