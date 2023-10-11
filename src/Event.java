public class Event {
    private final String eventType;
    private final long timestamp;

    public Event(String eventType, long timestamp) {
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
