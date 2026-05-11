import javax.swing.*;
import java.awt.*;

/**
 * Main entry point for the Event-Driven Messaging Framework
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Enable modern UI rendering
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            try {
    for (UIManager.LookAndFeelInfo info :
            UIManager.getInstalledLookAndFeels()) {

        if ("Nimbus".equals(info.getName())) {

            UIManager.setLookAndFeel(info.getClassName());

            break;
        }
    }
} catch (Exception e) {
    e.printStackTrace();
}

            // Global UI styling
            UIManager.put("Panel.background", new Color(18, 18, 24));
            UIManager.put("OptionPane.background", new Color(18, 18, 24));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);

            // Create broker
            EventBroker broker = new EventBroker();
            broker.setRunning(true);

            // Create dashboard
            DashboardFrame frame = new DashboardFrame(broker, null);

            // Create consumer
            EventConsumer consumer =
                    new EventConsumer(broker, frame.tableModel);

            consumer.start();

            frame.setEventConsumer(consumer);

            frame.setVisible(true);

            System.out.println("========================================");
            System.out.println("⚡ Event-Driven Messaging Framework");
            System.out.println("========================================");
            System.out.println("[MAIN] Application started");
            System.out.println("[MAIN] Producer thread: "
                    + Thread.currentThread().getName());
            System.out.println("[MAIN] Consumer thread: "
                    + consumer.getName());
            System.out.println("========================================");
        });
    }
}