import javax.swing.table.DefaultTableModel;
import java.util.Random;

/**
 * EventConsumer processes events from the broker
 * Runs in a background thread
 */
public class EventConsumer extends Thread {
    private final EventBroker broker;
    private final DefaultTableModel tableModel;
    private volatile boolean running = true;
    private static final long PROCESSING_DELAY = 1500; // 1.5 seconds
    private final Random random = new Random();

    public EventConsumer(EventBroker broker, DefaultTableModel tableModel) {
        this.broker = broker;
        this.tableModel = tableModel;
        this.setDaemon(true);
        this.setName("EventConsumer-Thread");
    }

    @Override
    public void run() {
        System.out.println("[CONSUMER] Consumer thread started");
        
        while (running) {
            try {
                Event event = broker.consume();
                
                if (event != null) {
                    System.out.println("[CONSUMER] Processing event: " + event.getEventId());
                    
                    // Simulate processing delay
                    Thread.sleep(PROCESSING_DELAY);
                    
                    // Randomly fail 15% of events
                    if (random.nextDouble() < 0.15) {
                        event.setStatus(EventStatus.FAILED);
                        broker.incrementFailed();
                        System.out.println("[CONSUMER] Event FAILED: " + event.getEventId());
                    } else {
                        event.setStatus(EventStatus.PROCESSED);
                        broker.incrementProcessed();
                        System.out.println("[CONSUMER] Event PROCESSED: " + event.getEventId());
                    }
                    
                    // Update the table model on the Event Dispatch Thread
                    updateTableModel(event);
                    
                } else {
                    // Sleep briefly if queue is empty
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("[CONSUMER] Consumer thread stopped");
    }

    /**
     * Update the table model with processed event
     */
    private void updateTableModel(Event event) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Find and update the row for this event
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String eventId = (String) tableModel.getValueAt(i, 0);
                if (eventId.equals(event.getEventId())) {
                    tableModel.setValueAt(event.getStatus().getDisplayName(), i, 2);
                    break;
                }
            }
        });
    }

    /**
     * Stop the consumer
     */
    public void stopConsumer() {
        running = false;
        System.out.println("[CONSUMER] Stop signal sent");
    }
}
