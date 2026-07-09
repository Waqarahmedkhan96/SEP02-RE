package model.state;

public class BookingStateFactory {
    public static BookingState fromStatus(String status) {
        if (status == null) throw new IllegalArgumentException("Booking status is null");

        return switch (status.toUpperCase()) {
            case "PENDING" -> new PendingState();
            case "ACTIVE" -> new ActiveState();
            case "OVERDUE" -> new OverdueState();
            case "COMPLETED" -> new CompletedState();
            case "CANCELLED" -> new CancelledState();
            default -> throw new IllegalArgumentException("Unknown booking status: " + status);
        };
    }
}