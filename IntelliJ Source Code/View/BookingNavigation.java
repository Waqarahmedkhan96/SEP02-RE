package view;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

final class BookingNavigation {
    private BookingNavigation() {
    }

    static void fire(Node source, EventType<Event> eventType) {
        source.fireEvent(new Event(eventType));
    }
}
