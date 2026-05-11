import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents an Event in the Event-Driven system
 */
public class Event {
    private final String eventId;
    private final String eventType;
    private final String payload;
    private EventStatus status;
    private final LocalDateTime timestamp;

    public Event(String eventType, String payload) {
        this.eventId = UUID.randomUUID().toString().substring(0, 8);
        this.eventType = eventType;
        this.payload = payload;
        this.status = EventStatus.PENDING;
        this.timestamp = LocalDateTime.now();
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
