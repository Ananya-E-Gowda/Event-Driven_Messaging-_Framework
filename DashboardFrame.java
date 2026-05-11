import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Modern Dashboard Frame
 */
public class DashboardFrame extends JFrame {

    private final EventBroker broker;
    private EventConsumer consumer;

    public final DefaultTableModel tableModel;

    private JLabel statsLabel;
    private Timer updateTimer;

    // Theme Colors
    private final Color BG = new Color(15, 17, 22);
    private final Color PANEL = new Color(24, 26, 32);
    private final Color PANEL_2 = new Color(30, 32, 40);
    private final Color ACCENT = new Color(0, 173, 181);
    private final Color TEXT = new Color(230, 230, 230);
    private final Color MUTED = new Color(150, 150, 150);

    public DashboardFrame(EventBroker broker, EventConsumer consumer) {

        this.broker = broker;
        this.consumer = consumer;

        String[] columnNames = {
                "Event ID",
                "Event Type",
                "Status",
                "Timestamp"
        };

        this.tableModel = new DefaultTableModel(columnNames, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        setTitle("\u26A1 Event-Driven Messaging Framework");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1350, 760);

        setLocationRelativeTo(null);

        setMinimumSize(new Dimension(1100, 650));

        JPanel root = new JPanel(new BorderLayout(15, 15));

        root.setBackground(BG);

        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        // Top Section
        root.add(createTopBar(), BorderLayout.NORTH);

        // Center Split Layout
        JSplitPane splitPane = new JSplitPane();

        splitPane.setDividerLocation(370);

        splitPane.setBorder(null);

        splitPane.setBackground(BG);

        splitPane.setDividerSize(3);

        splitPane.setLeftComponent(
                new EventProducerPanel(broker, tableModel)
        );

        splitPane.setRightComponent(createDashboardPanel());

        root.add(splitPane, BorderLayout.CENTER);

        // Bottom
        root.add(createBottomPanel(), BorderLayout.SOUTH);

        setContentPane(root);

        startStatsUpdate();
    }

    /**
     * TOP BAR
     */
    private JPanel createTopBar() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        // Title Section
        JPanel left = new JPanel();

        left.setOpaque(false);

        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Real-Time Event Dashboard");

        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "Monitor and manage distributed messaging events"
        );

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        subtitle.setForeground(MUTED);

        left.add(title);

        left.add(Box.createVerticalStrut(5));

        left.add(subtitle);

        // Stats Card
        JPanel statsCard = new JPanel();

        statsCard.setBackground(PANEL);

        statsCard.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        statsLabel = new JLabel();

        statsLabel.setForeground(TEXT);

        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        updateStats();

        statsCard.add(statsLabel);

        panel.add(left, BorderLayout.WEST);

        panel.add(statsCard, BorderLayout.EAST);

        return panel;
    }

    /**
     * DASHBOARD PANEL
     */
    private JPanel createDashboardPanel() {

        JPanel wrapper = new JPanel(new BorderLayout(15, 15));

        wrapper.setBackground(PANEL);

        wrapper.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Header
        JLabel title = new JLabel("\uD83D\uDCCA Event Monitor");

        title.setForeground(Color.WHITE);

        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));

        wrapper.add(title, BorderLayout.NORTH);

        // Table
        JTable table = new JTable(tableModel);

        table.setFillsViewportHeight(true);

        table.setBackground(PANEL_2);

        table.setForeground(TEXT);

        table.setRowHeight(38);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        table.setGridColor(new Color(50, 50, 60));

        table.setShowVerticalLines(false);

        table.setIntercellSpacing(new Dimension(0, 1));

        table.setSelectionBackground(new Color(45, 55, 72));

        table.setSelectionForeground(Color.WHITE);

        // Header
        JTableHeader header = table.getTableHeader();

        header.setOpaque(false);

header.setDefaultRenderer(new DefaultTableCellRenderer() {

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
    ) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table,
                value,
                isSelected,
                hasFocus,
                row,
                column
        );

        label.setBackground(new Color(35, 38, 48));

        label.setForeground(Color.WHITE);

        label.setFont(new Font("Segoe UI", Font.BOLD, 13));

        label.setHorizontalAlignment(SwingConstants.LEFT);

        label.setBorder(BorderFactory.createEmptyBorder(
                0,
                10,
                0,
                0
        ));

        return label;
    }
});

        header.setBackground(new Color(45, 48, 58));

        header.setReorderingAllowed(false);

        header.setForeground(Color.WHITE);

        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        header.setBorder(BorderFactory.createEmptyBorder());

        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Status Renderer
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column
                    ) {

                        Component c =
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );

                        c.setFont(new Font("Segoe UI", Font.PLAIN, 12));

                        if (!isSelected) {

                            c.setBackground(PANEL_2);

                            c.setForeground(TEXT);
                        }

                        if (column == 2) {

                            String status = value.toString();

                            if (status.equalsIgnoreCase("Processed")) {

                                c.setForeground(
                                        new Color(80, 220, 120)
                                );

                            } else if (
                                    status.equalsIgnoreCase("Failed")
                            ) {

                                c.setForeground(
                                        new Color(255, 90, 90)
                                );

                            } else {

                                c.setForeground(
                                        new Color(255, 210, 90)
                                );
                            }

                            setHorizontalAlignment(CENTER);
                        }

                        return c;
                    }
                };

        table.getColumnModel()
                .getColumn(2)
                .setCellRenderer(renderer);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        scrollPane.getViewport().setBackground(PANEL_2);

        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    /**
     * BOTTOM CONTROLS
     */
    private JPanel createBottomPanel() {

        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                15,
                5
        ));

        panel.setOpaque(false);

        JButton clearBtn =
                createButton("Clear Logs", new Color(255, 140, 70));

        clearBtn.addActionListener(e -> {

            tableModel.setRowCount(0);

            broker.clear();
        });

        JButton exitBtn =
                createButton("Exit", new Color(220, 70, 70));

        exitBtn.addActionListener(e -> {

            consumer.stopConsumer();

            broker.setRunning(false);

            System.exit(0);
        });

        panel.add(clearBtn);

        panel.add(exitBtn);

        return panel;
    }

    /**
     * MODERN BUTTON
     */
    private JButton createButton(String text, Color color) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setForeground(Color.WHITE);

        btn.setBackground(color);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btn.setPreferredSize(new Dimension(140, 42));

        btn.setBorder(BorderFactory.createEmptyBorder());

        btn.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {

                btn.setBackground(color.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {

                btn.setBackground(color);
            }
        });

        return btn;
    }

    /**
     * UPDATE STATS
     */
    private void updateStats() {

        int processed = broker.getTotalEventsProcessed();

        int failed = broker.getTotalEventsFailed();

        int pending = broker.getQueueSize();

        int total = broker.getTotalEvents();

        statsLabel.setText(
                "Total: "
                        + total
                        + "   |   Processed: "
                        + processed
                        + "   |   Failed: "
                        + failed
                        + "   |   Pending: "
                        + pending
        );
    }

    /**
     * TIMER
     */
    private void startStatsUpdate() {

        updateTimer = new Timer(500, e -> updateStats());

        updateTimer.start();
    }

    public void stopStatsUpdate() {

        if (updateTimer != null) {

            updateTimer.stop();
        }
    }

    public void setEventConsumer(EventConsumer consumer) {

        this.consumer = consumer;
    }
}