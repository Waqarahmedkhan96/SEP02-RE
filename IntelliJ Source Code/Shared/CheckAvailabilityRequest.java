package shared;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CheckAvailabilityRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public CheckAvailabilityRequest(LocalDateTime startDateTime,
                                    LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
