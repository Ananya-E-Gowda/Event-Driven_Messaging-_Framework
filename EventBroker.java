import java.util.LinkedList;
import java.util.Queue;

/**
 * EventBroker manages the event queue
 * Acts as a message broker for the event-driven system
 */
public class EventBroker {
    private final Queue<Event> eventQueue;
    private volatile boolean running = false;
    private int totalEventsProcessed = 0;
    private int totalEventsFailed = 0;

    public EventBroker() {
        this.eventQueue = new LinkedList<>();
    }

    /**
     * Add an event to the queue
     */
    public synchronized void publish(Event event) {
        eventQueue.offer(event);
        System.out.println("[BROKER] Event published: " + event.getEventId() + " - " + event.getEventType());
        notifyAll();
    }

    /**
     * Retrieve the next event from the queue
     */
    public synchronized Event consume() {
        return eventQueue.poll();
    }

    /**
     * Peek at the next event without removing it
     */
    public synchronized Event peek() {
        return eventQueue.peek();
    }

    /**
     * Get queue size
     */
    public synchronized int getQueueSize() {
        return eventQueue.size();
    }

    /**
     * Check if queue is empty
     */
    public synchronized boolean isEmpty() {
        return eventQueue.isEmpty();
    }

    /**
     * Clear the entire queue
     */
    public synchronized void clear() {
        eventQueue.clear();
        System.out.println("[BROKER] Queue cleared");
    }

    /**
     * Set running state
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Check if broker is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Increment processed count
     */
    public synchronized void incrementProcessed() {
        totalEventsProcessed++;
    }

    /**
     * Increment failed count
     */
    public synchronized void incrementFailed() {
        totalEventsFailed++;
    }

    /**
     * Get total events processed
     */
    public int getTotalEventsProcessed() {
        return totalEventsProcessed;
    }

    /**
     * Get total events failed
     */
    public int getTotalEventsFailed() {
        return totalEventsFailed;
    }

    /**
     * Get total events in the system
     */
    public synchronized int getTotalEvents() {
        return totalEventsProcessed + totalEventsFailed + eventQueue.size();
    }
}
