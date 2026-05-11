/**
 * Enumeration for Event Status
 * Represents the lifecycle of an event in the broker
 */
public enum EventStatus {
    PENDING("Pending", "#FFD700"),      // Yellow
    PROCESSED("Processed", "#00FF00"),   // Green
    FAILED("Failed", "#FF0000");         // Red

    private final String displayName;
    private final String color;

    EventStatus(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }
}
