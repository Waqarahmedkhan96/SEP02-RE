package view;

import javafx.event.Event;
import javafx.event.EventType;

public final class NavigationEvents {
    public static final EventType<Event> SHOW_DASHBOARD =
            new EventType<>(Event.ANY, "SHOW_DASHBOARD");

     public static final EventType<Event> SHOW_VIEW_VEHICLE =
            new EventType<>(Event.ANY, "SHOW_VIEW_VEHICLE");

    private NavigationEvents() {
    }
}
