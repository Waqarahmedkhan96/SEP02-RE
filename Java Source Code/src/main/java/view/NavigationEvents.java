package view;

import javafx.event.Event;
import javafx.event.EventType;

public final class NavigationEvents {
    public static final EventType<Event> SHOW_DASHBOARD =
            new EventType<>(Event.ANY, "SHOW_DASHBOARD");

    public static final EventType<Event> SHOW_VIEW_VEHICLE =
            new EventType<>(Event.ANY, "SHOW_VIEW_VEHICLE");

    public static final EventType<Event> SHOW_BOOKING_MENU =
            new EventType<>(Event.ANY, "SHOW_BOOKING_MENU");

    public static final EventType<Event> SHOW_CREATE_BOOKING =
            new EventType<>(Event.ANY, "SHOW_CREATE_BOOKING");

    public static final EventType<Event> SHOW_UPDATE_BOOKING =
            new EventType<>(Event.ANY, "SHOW_UPDATE_BOOKING");

    public static final EventType<Event> SHOW_CANCEL_BOOKING =
            new EventType<>(Event.ANY, "SHOW_CANCEL_BOOKING");

    public static final EventType<Event> SHOW_COMPLETE_BOOKING =
            new EventType<>(Event.ANY, "SHOW_COMPLETE_BOOKING");

    public static final EventType<Event> SHOW_BOOKING_HISTORY =
            new EventType<>(Event.ANY, "SHOW_BOOKING_HISTORY");

    private NavigationEvents() {
    }
}
