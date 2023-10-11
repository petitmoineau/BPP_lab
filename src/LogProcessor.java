import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class LogProcessor {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        List<Event> events = readEventsFromFile("src\\log.txt");

        // Divide events into fragments for concurrent processing.
        List<List<Event>> fragments = fragmentEvents(events, 2);

        // Create a thread pool with 2 threads.
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Submit tasks to process log fragments.
        List<Future<Map<String, List<Long>>>> futures = new ArrayList<>();
        for (List<Event> fragment : fragments) {
            futures.add(executorService.submit(new LogFragmentProcessor(fragment)));
        }

        // Aggregate results from all threads.
        Map<String, List<Long>> aggregatedResults = new HashMap<>();
        for (Future<Map<String, List<Long>>> future : futures) {
            Map<String, List<Long>> result = future.get();
            for (Map.Entry<String, List<Long>> entry : result.entrySet()) {
                aggregatedResults.merge(entry.getKey(), entry.getValue(), (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                });
            }
        }

        outputResults(aggregatedResults);

        executorService.shutdown();
    }

    private static List<Event> readEventsFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.lines()
                    .map(line -> line.split(" "))
                    .map(parts -> new Event(parts[0], Long.parseLong(parts[1])))
                    .collect(Collectors.toList());
        }
    }

    private static List<List<Event>> fragmentEvents(List<Event> events, int numberOfFragments) {
        int fragmentSize = (int) Math.ceil((double) events.size() / numberOfFragments);
        List<List<Event>> fragments = new ArrayList<>(numberOfFragments);
        for (int i = 0; i < numberOfFragments; i++) {
            int startIndex = i * fragmentSize;
            int endIndex = Math.min(startIndex + fragmentSize, events.size());
            fragments.add(events.subList(startIndex, endIndex));
        }
        return fragments;
    }

    private static void outputResults(Map<String, List<Long>> aggregatedResults) {
        for (Map.Entry<String, List<Long>> entry : aggregatedResults.entrySet()) {
            String eventType = entry.getKey();
            List<Long> intervals = entry.getValue();
            long sum = intervals.stream().mapToLong(Long::longValue).sum();
            double average = sum / (double) intervals.size();
            System.out.println("Event Type: " + eventType + ", Count: " + (intervals.size() + 1) + ", Average Interval: " + average);
        }
    }
    //бонус
    public static List<Event> searchEvents(List<Event> events, String eventType) {
        return events.stream()
                .filter(event -> event.getEventType().equals(eventType))
                .collect(Collectors.toList());
    }

}
