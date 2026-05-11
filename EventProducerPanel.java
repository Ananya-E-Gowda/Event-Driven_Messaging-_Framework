import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Modern Event Producer Panel
 */
public class EventProducerPanel extends JPanel {

    private final EventBroker broker;

    private final DefaultTableModel tableModel;

    private final JComboBox<String> eventTypeCombo;

    private final JTextArea payloadTextArea;

    private final JButton sendButton;

    private int eventCounter = 1;

    // Theme Colors
    private final Color BG = new Color(24, 26, 32);

    private final Color CARD = new Color(30, 32, 40);

    private final Color INPUT = new Color(40, 42, 52);

    private final Color ACCENT = new Color(0, 173, 181);

    private final Color TEXT = new Color(235, 235, 235);

    private final Color MUTED = new Color(160, 160, 160);

    public EventProducerPanel(
            EventBroker broker,
            DefaultTableModel tableModel
    ) {

        this.broker = broker;

        this.tableModel = tableModel;

        setLayout(new BorderLayout());

        setBackground(BG);

        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel content = new JPanel();

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.setBackground(BG);

        // Header
        JLabel title = new JLabel("\uD83D\uDCE4 Event Producer");

        title.setForeground(Color.WHITE);

        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));

        JLabel subtitle = new JLabel(
                "Create and publish events in real time"
        );

        subtitle.setForeground(MUTED);

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        content.add(title);

        content.add(Box.createVerticalStrut(6));

        content.add(subtitle);

        content.add(Box.createVerticalStrut(30));

        // Event Type Card
        JPanel typeCard = createCard();

        typeCard.setLayout(new BoxLayout(typeCard, BoxLayout.Y_AXIS));

        JLabel typeLabel = createLabel("Event Type");

        eventTypeCombo = new JComboBox<>(
                new String[]{
                        "USER_REGISTERED",
                        "ORDER_CREATED"
                }
        );

        styleComboBox(eventTypeCombo);

        typeCard.add(typeLabel);

        typeCard.add(Box.createVerticalStrut(10));

        typeCard.add(eventTypeCombo);

        content.add(typeCard);

        content.add(Box.createVerticalStrut(20));

        // Payload Card
        JPanel payloadCard = createCard();

        payloadCard.setLayout(new BorderLayout(10, 10));

        JLabel payloadLabel = createLabel("Payload (JSON)");

        payloadCard.add(payloadLabel, BorderLayout.NORTH);

        payloadTextArea = new JTextArea();

        payloadTextArea.setText(
                "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"Sample Data\"\n" +
                "}"
        );

        payloadTextArea.setBackground(INPUT);

        payloadTextArea.setForeground(TEXT);

        payloadTextArea.setCaretColor(Color.WHITE);

        payloadTextArea.setFont(
                new Font("Consolas", Font.PLAIN, 13)
        );

        payloadTextArea.setLineWrap(true);

        payloadTextArea.setWrapStyleWord(true);

        payloadTextArea.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JScrollPane scrollPane =
                new JScrollPane(payloadTextArea);

        scrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(60, 60, 70),
                1
        ));

        scrollPane.getViewport().setBackground(INPUT);

        payloadCard.add(scrollPane, BorderLayout.CENTER);

        content.add(payloadCard);

        content.add(Box.createVerticalStrut(25));

        // Send Button
        sendButton = createButton("Send Event");

        sendButton.addActionListener(this::handleSendEvent);

        content.add(sendButton);

        content.add(Box.createVerticalGlue());

        add(content, BorderLayout.CENTER);
    }

    /**
     * HANDLE SEND EVENT
     */
    private void handleSendEvent(ActionEvent e) {

        String eventType =
                (String) eventTypeCombo.getSelectedItem();

        String payload = payloadTextArea.getText();

        if (
                eventType == null
                        || eventType.isEmpty()
                        || payload.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Create Event
        Event event = new Event(eventType, payload);

        broker.publish(event);

        // Table Row
        Object[] rowData = {
                event.getEventId(),
                event.getEventType(),
                event.getStatus().getDisplayName(),
                event.getFormattedTimestamp()
        };

        tableModel.addRow(rowData);

        eventCounter++;

        payloadTextArea.setText(
                "{\n" +
                "  \"id\": " + eventCounter + ",\n" +
                "  \"name\": \"Data\"\n" +
                "}"
        );

        System.out.println(
                "[PRODUCER] Event sent: "
                        + event.getEventId()
        );
    }

    /**
     * CREATE CARD
     */
    private JPanel createCard() {

        JPanel panel = new JPanel();

        panel.setBackground(CARD);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        return panel;
    }

    /**
     * LABEL
     */
    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setForeground(TEXT);

        label.setFont(new Font("Segoe UI", Font.BOLD, 13));

        return label;
    }

    /**
     * STYLE COMBOBOX
     */
    private void styleComboBox(JComboBox<String> combo) {

        combo.setBackground(INPUT);

        combo.setForeground(new Color(230, 230, 230));

        combo.setFocusable(false);

        combo.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                42
        ));

        combo.setPreferredSize(new Dimension(
                250,
                42
        ));

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        combo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(70, 70, 80),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );
        combo.setRenderer(new DefaultListCellRenderer() {

    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {

        Component c = super.getListCellRendererComponent(
                list,
                value,
                index,
                isSelected,
                cellHasFocus
        );

        c.setBackground(new Color(40, 42, 52));

        c.setForeground(Color.WHITE);

        return c;
    }
});
    }

    /**
     * BUTTON
     */
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setBackground(ACCENT);

        button.setForeground(Color.WHITE);

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));

        button.setFocusPainted(false);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(BorderFactory.createEmptyBorder());

        button.setPreferredSize(new Dimension(200, 48));

        button.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                48
        ));

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    public void mouseEntered(
                            java.awt.event.MouseEvent evt
                    ) {

                        button.setBackground(
                                new Color(0, 190, 200)
                        );
                    }

                    public void mouseExited(
                            java.awt.event.MouseEvent evt
                    ) {

                        button.setBackground(ACCENT);
                    }
                }
        );

        return button;
    }
}