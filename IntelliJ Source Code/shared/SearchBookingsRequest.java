package shared;

import java.io.Serializable;

public class SearchBookingsRequest implements Serializable {
    private final String query;
    private final String bookingQuery;
    private final String customerQuery;
    private final String vehicleQuery;
    private final String dateQuery;
    private final boolean cancellableOnly;
    private final boolean archivedOnly;

    public SearchBookingsRequest(String query, boolean cancellableOnly) {
        this.query = query;
        this.bookingQuery = query;
        this.customerQuery = query;
        this.vehicleQuery = query;
        this.dateQuery = "";
        this.cancellableOnly = cancellableOnly;
        this.archivedOnly = false;
    }

    public SearchBookingsRequest(String customerQuery, String vehicleQuery, String dateQuery, boolean archivedOnly) {
        this.query = "";
        this.bookingQuery = "";
        this.customerQuery = customerQuery;
        this.vehicleQuery = vehicleQuery;
        this.dateQuery = dateQuery;
        this.cancellableOnly = false;
        this.archivedOnly = archivedOnly;
    }

    public SearchBookingsRequest(String bookingQuery, String customerQuery, String vehicleQuery, String dateQuery, boolean archivedOnly) {
        this.query = "";
        this.bookingQuery = bookingQuery;
        this.customerQuery = customerQuery;
        this.vehicleQuery = vehicleQuery;
        this.dateQuery = dateQuery;
        this.cancellableOnly = false;
        this.archivedOnly = archivedOnly;
    }

    public String getQuery() {
        return query;
    }

    public String getBookingQuery() {
        return bookingQuery;
    }

    public String getCustomerQuery() {
        return customerQuery;
    }

    public String getVehicleQuery() {
        return vehicleQuery;
    }

    public String getDateQuery() {
        return dateQuery;
    }

    public boolean isCancellableOnly() {
        return cancellableOnly;
    }

    public boolean isArchivedOnly() {
        return archivedOnly;
    }
}
