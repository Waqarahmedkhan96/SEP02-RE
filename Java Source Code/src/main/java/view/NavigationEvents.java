package view;

import javafx.event.Event;
import javafx.event.EventType;

public final class NavigationEvents {
    public static final EventType<Event> SHOW_DASHBOARD =
            new EventType<>(Event.ANY, "SHOW_DASHBOARD");

    private NavigationEvents() {
    }
}
