import java.util.*;
import java.util.concurrent.Callable;

public class LogFragmentProcessor implements Callable<Map<String, List<Long>>> {

    private final List<Event> events;

    public LogFragmentProcessor(List<Event> events) {
        this.events = events;
    }

    @Override
    public Map<String, List<Long>> call() {
        // Map to store event type and list of time intervals between events of the same type.
        Map<String, List<Long>> eventTypeIntervals = new HashMap<>();

        // Store the previous event of each type to calculate the interval.
        Map<String, Event> previousEvents = new HashMap<>();

        for (Event event : events) {
            String eventType = event.getEventType();

            // Compute intervals between events of the same type
            if (previousEvents.containsKey(eventType)) {
                eventTypeIntervals
                        .computeIfAbsent(eventType, k -> new ArrayList<>())
                        .add(event.getTimestamp() - previousEvents.get(eventType).getTimestamp());
            }
            previousEvents.put(eventType, event);
        }

        return eventTypeIntervals;
    }
}
