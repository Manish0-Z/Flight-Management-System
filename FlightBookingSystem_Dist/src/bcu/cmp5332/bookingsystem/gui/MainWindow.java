package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class MainWindow extends JFrame implements ActionListener, GuiWindow {

    static class ToastNotification extends JWindow {

        enum ToastType {
            SUCCESS, ERROR, INFO
        }

        public static void showToast(Component parent, String message, ToastType type) {
            ToastNotification toast = new ToastNotification(message, type);
            toast.setLocationRelativeTo(parent);
            toast.setVisible(true);
            Timer timer = new Timer(3000, e -> toast.setVisible(false));
            timer.setRepeats(false);
            timer.start();
        }

        private ToastNotification(String message, ToastType type) {
            setSize(300, 50);
            setLayout(new BorderLayout());
            setAlwaysOnTop(true);

            JPanel panel = new JPanel();
            panel.setBackground(getColorForType(type));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel label = new JLabel(message);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            panel.add(label);
            add(panel, BorderLayout.CENTER);
        }

        private Color getColorForType(ToastType type) {
            switch (type) {
                case SUCCESS: return new Color(34, 197, 94);
                case ERROR: return new Color(239, 68, 68);
                case INFO: return new Color(59, 130, 246);
                default: return Color.GRAY;
            }
        }

    }

    private final FlightBookingSystem fbs;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Sidebar buttons
    private JButton homeBtn;
    private JButton flightsBtn;
    private JButton customersBtn;
    private JButton bookingsBtn;
    private JButton exitBtn;

    // Panels
    private JPanel homePanel;
    private JPanel flightsPanel;
    private JPanel customersPanel;
    private JPanel bookingsPanel;
    private JPanel statsPanel;

    // Enhanced sidebar color scheme
    private static final Color SIDEBAR_COLOR = new Color(30, 58, 138);
    private static final Color SIDEBAR_TEXT_COLOR = Color.WHITE;
    private static final Color ACTIVE_BTN_COLOR = new Color(52, 73, 154);
    private static final Color HOVER_BTN_COLOR = new Color(69, 90, 171);
    private static final Color AIRLINE_ACCENT = new Color(255, 193, 7);
    private static final Color ACCENT_BORDER = new Color(255, 193, 7);
    private static final Color ICON_COLOR = new Color(200, 215, 255);
    private static final Color ICON_ACTIVE_COLOR = Color.WHITE;

    private boolean isAdmin;

    public MainWindow(FlightBookingSystem fbs, boolean isAdmin) {
        this.fbs = fbs;
        this.isAdmin = isAdmin;
        initialize();
    }

    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Log or ignore
        }

        setTitle(isAdmin ? "Flight Booking System - Admin Dashboard" : "Flight Booking System - Customer Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Create Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Initialize Panels
        homePanel = createHomePanel();
        flightsPanel = createFlightsPanel();
        customersPanel = createCustomersPanel();
        bookingsPanel = createBookingsPanel();

        contentPanel.add(homePanel, "Home");
        contentPanel.add(flightsPanel, "Flights");
        contentPanel.add(customersPanel, "Customers");
        contentPanel.add(bookingsPanel, "Bookings");

        add(contentPanel, BorderLayout.CENTER);

        // Default view
        setActiveButton(homeBtn);
        cardLayout.show(contentPanel, "Home");

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(240, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Header with logo and airplane icon
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(SIDEBAR_COLOR);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Airline logo
        AirlineLogo logo = new AirlineLogo(200, 80);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(logo);

        sidebar.add(headerPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Navigation section label
        JLabel navLabel = new JLabel("NAVIGATION");
        navLabel.setForeground(new Color(150, 170, 220));
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        navLabel.setBorder(new EmptyBorder(0, 20, 8, 0));
        sidebar.add(navLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));

        // Buttons with custom icons
        homeBtn = createSidebarButton("Dashboard", SidebarIcon.IconType.DASHBOARD);
        flightsBtn = createSidebarButton("Flights", SidebarIcon.IconType.FLIGHTS);
        customersBtn = createSidebarButton("Customers", SidebarIcon.IconType.CUSTOMERS);
        bookingsBtn = createSidebarButton("Bookings", SidebarIcon.IconType.BOOKINGS);

        sidebar.add(homeBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(flightsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(customersBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(bookingsBtn);

        // Spacer to push exit button to bottom
        sidebar.add(Box.createVerticalGlue());

        // System section divider
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(200, 1));
        separator.setForeground(new Color(60, 88, 168));
        sidebar.add(separator);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        exitBtn = createSidebarButton("Exit", SidebarIcon.IconType.EXIT);
        exitBtn.setBackground(new Color(180, 50, 50));
        sidebar.add(exitBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        return sidebar;
    }

    // Enhanced sidebar button with custom icon components
    private JButton createSidebarButton(String text, SidebarIcon.IconType iconType) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setMaximumSize(new Dimension(240, 48));
        buttonPanel.setBackground(SIDEBAR_COLOR);
        
        // Left accent border (visible when active)
        JPanel accentBorder = new JPanel();
        accentBorder.setPreferredSize(new Dimension(4, 48));
        accentBorder.setBackground(SIDEBAR_COLOR);
        accentBorder.setOpaque(true);
        buttonPanel.add(accentBorder, BorderLayout.WEST);
        
        // Create button
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setPreferredSize(new Dimension(236, 48));
        btn.setForeground(SIDEBAR_TEXT_COLOR);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add icon on left with padding
        SidebarIcon icon = new SidebarIcon(iconType, 20);
        icon.setIconColor(ICON_COLOR);
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 14));
        iconPanel.setOpaque(false);
        iconPanel.add(icon);
        btn.add(iconPanel, BorderLayout.WEST);
        
        // Add label centered
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(SIDEBAR_TEXT_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        btn.add(label, BorderLayout.CENTER);
        
        buttonPanel.add(btn, BorderLayout.CENTER);
        
        // Store references for later access
        btn.putClientProperty("panel", buttonPanel);
        btn.putClientProperty("accentBorder", accentBorder);
        btn.putClientProperty("icon", icon);
        btn.putClientProperty("label", label);
        
        // Enhanced hover effects with smooth transitions
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.getBackground() != ACTIVE_BTN_COLOR && btn != exitBtn) {
                    btn.setBackground(HOVER_BTN_COLOR);
                    icon.setIconColor(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.getBackground() != ACTIVE_BTN_COLOR && btn != exitBtn) {
                    btn.setBackground(SIDEBAR_COLOR);
                    icon.setIconColor(ICON_COLOR);
                }
            }
        });
        
        btn.addActionListener(this);
        return btn;
    }

    // Enhanced active state with accent border and icon color
    private void setActiveButton(JButton active) {
        JButton[] btns = { homeBtn, flightsBtn, customersBtn, bookingsBtn };
        for (JButton btn : btns) {
            btn.setBackground(SIDEBAR_COLOR);
            JLabel label = (JLabel) btn.getClientProperty("label");
            if (label != null) {
                label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            }
            // Reset accent border
            JPanel accentBorder = (JPanel) btn.getClientProperty("accentBorder");
            if (accentBorder != null) {
                accentBorder.setBackground(SIDEBAR_COLOR);
            }
            // Reset icon color
            SidebarIcon icon = (SidebarIcon) btn.getClientProperty("icon");
            if (icon != null) {
                icon.setIconColor(ICON_COLOR);
            }
        }
        
        // Set active state
        active.setBackground(ACTIVE_BTN_COLOR);
        JLabel activeLabel = (JLabel) active.getClientProperty("label");
        if (activeLabel != null) {
            activeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        }
        // Show accent border
        JPanel accentBorder = (JPanel) active.getClientProperty("accentBorder");
        if (accentBorder != null) {
            accentBorder.setBackground(ACCENT_BORDER);
        }
        // Highlight icon
        SidebarIcon icon = (SidebarIcon) active.getClientProperty("icon");
        if (icon != null) {
            icon.setIconColor(ICON_ACTIVE_COLOR);
        }
    }

    // --- Content Panels ---

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Welcome header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255)); // Light blue background
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel welcomeLabel = new JLabel("Flight Management System");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(30, 58, 138)); // Airline blue

        AirplaneIcon headerPlane = new AirplaneIcon(40, 30);
        headerPanel.add(headerPlane);
        headerPanel.add(Box.createHorizontalStrut(15));
        headerPanel.add(welcomeLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Main content area with stats and chart
        JPanel contentArea = new JPanel(new BorderLayout(0, 20));
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(new EmptyBorder(20, 30, 30, 30));

        // Stats panel
        statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        statsPanel.setBackground(Color.WHITE);
        refreshHomeStats(statsPanel);
        contentArea.add(statsPanel, BorderLayout.NORTH);

        // Chart panel
        JPanel chartPanel = createBookingChart();
        contentArea.add(chartPanel, BorderLayout.CENTER);

        panel.add(contentArea, BorderLayout.CENTER);

        return panel;
    }

    private void refreshHomeStats(JPanel panel) {
        panel.removeAll();
        int flightCount = fbs.getFlights().size();
        int customerCount = fbs.getCustomers().size();
        int bookingCount = fbs.getCustomers().stream().mapToInt(c -> c.getBookings().size()).sum();

        panel.add(createStatCard("Total Flights", String.valueOf(flightCount), new Color(30, 144, 255), "✈️"));
        panel.add(createStatCard("Total Customers", String.valueOf(customerCount), new Color(50, 205, 50), "👥"));
        panel.add(createStatCard("Active Bookings", String.valueOf(bookingCount), new Color(255, 140, 0), "🎫"));

        panel.revalidate();
        panel.repaint();
    }

    private JPanel createStatCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(color);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));

        titlePanel.add(iconLabel);
        titlePanel.add(titleLbl);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setForeground(Color.WHITE);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(titlePanel, BorderLayout.NORTH);
        card.add(valueLbl, BorderLayout.CENTER);

        return card;
    }

    private JPanel createBookingChart() {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int height = getHeight();
                int padding = 40;
                int barWidth = 60;
                int maxBarHeight = height - 2 * padding;

                // Get booking counts per flight (top 5)
                List<Flight> flights = fbs.getFlights();
                int maxCount = 0;
                int displayCount = Math.min(5, flights.size());

                for (int i = 0; i < displayCount; i++) {
                    int count = flights.get(i).getPassengers().size();
                    if (count > maxCount)
                        maxCount = count;
                }

                if (maxCount == 0)
                    maxCount = 1; // Avoid division by zero

                // Draw bars
                Color[] colors = {
                        new Color(30, 144, 255),
                        new Color(50, 205, 50),
                        new Color(255, 140, 0),
                        new Color(255, 193, 7),
                        new Color(156, 39, 176)
                };

                for (int i = 0; i < displayCount; i++) {
                    Flight flight = flights.get(i);
                    int count = flight.getPassengers().size();
                    int barHeight = (int) ((double) count / maxCount * maxBarHeight);
                    int x = padding + i * (barWidth + 30);
                    int y = height - padding - barHeight;

                    // Draw bar
                    g2d.setColor(colors[i % colors.length]);
                    g2d.fillRoundRect(x, y, barWidth, barHeight, 10, 10);

                    // Draw count on top
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    String countStr = String.valueOf(count);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(countStr);
                    g2d.drawString(countStr, x + (barWidth - textWidth) / 2, y - 5);

                    // Draw flight number at bottom
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    String flightNum = flight.getFlightNumber();
                    if (flightNum.length() > 8)
                        flightNum = flightNum.substring(0, 8);
                    textWidth = fm.stringWidth(flightNum);
                    g2d.drawString(flightNum, x + (barWidth - textWidth) / 2, height - padding + 20);
                }

                // Draw title
                g2d.setColor(new Color(30, 58, 138));
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                g2d.drawString("Bookings per Flight (Top 5)", padding, 25);

                g2d.dispose();
            }
        };

        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 10, 10, 10)));
        chartPanel.setPreferredSize(new Dimension(0, 200));

        return chartPanel;
    }

    /**
     * @return
     */
    private JPanel createFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        AirplaneIcon flightIcon = new AirplaneIcon(30, 20);
        JLabel title = new JLabel("Flight Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 58, 138));

        headerPanel.add(flightIcon);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(title);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Toolbar with search
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton addBtn = new JButton("✈️ Add Flight");
        JButton refreshBtn = new JButton("🔄 Refresh");

        styleButton(addBtn);
        styleButton(refreshBtn);

        if (isAdmin) {
            addBtn.addActionListener(e -> {
                AddFlightWindow addFlightWindow = new AddFlightWindow(this);
                addFlightWindow.setVisible(true);
            });
            toolbar.add(addBtn);
        }
        refreshBtn.addActionListener(e -> {
            refreshFlightsTable(panel);
            ToastNotification.showToast(this, "Flights refreshed successfully!", ToastNotification.ToastType.SUCCESS);
        });
        toolbar.add(refreshBtn);
        toolbar.add(Box.createHorizontalStrut(20));

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        toolbar.add(searchLabel);
        toolbar.add(searchField);

        panel.add(toolbar, BorderLayout.SOUTH);

        // Table container
        JPanel tableContainer = new JPanel(new BorderLayout());
        panel.add(tableContainer, BorderLayout.CENTER);

        // Store references
        panel.putClientProperty("tableContainer", tableContainer);
        panel.putClientProperty("searchField", searchField);

        refreshFlightsTable(panel);

        return panel;
    }

    private void refreshFlightsTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTextField searchField = (JTextField) panel.getClientProperty("searchField");
        tableContainer.removeAll();

        List<Flight> flightsList = fbs.getFlights();
        String[] columns = new String[] { "ID", "Flight No", "Origin", "Destination", "Departure Date", "Passengers" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        for (Flight flight : flightsList) {
            model.addRow(new Object[] {
                    flight.getId(),
                    flight.getFlightNumber(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getDepartureDate(),
                    flight.getPassengers().size()
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(184, 207, 229));

        // Add TableRowSorter for search functionality
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Add search listener
        if (searchField != null) {
            // Remove old listeners
            for (DocumentListener dl : searchField.getListeners(DocumentListener.class)) {
                searchField.getDocument().removeDocumentListener(dl);
            }

            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filter();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filter();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filter();
                }

                private void filter() {
                    String text = searchField.getText();
                    if (text.trim().length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                }
            });
        }

        // Add double-click listener to view details
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        int modelRow = table.convertRowIndexToModel(row);
                        int flightId = (int) model.getValueAt(modelRow, 0);
                        showFlightDetails(flightId);
                    }
                }
            }
        });

        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);

        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private void showFlightDetails(int flightId) {
        try {
            Flight flight = fbs.getFlightByID(flightId);

            StringBuilder details = new StringBuilder();
            details.append("<html><body style='width: 300px; padding: 10px;'>");
            details.append("<h2 style='color: #1e3a8a;'>Flight Details</h2>");
            details.append("<table>");
            details.append("<tr><td><b>ID:</b></td><td>").append(flight.getId()).append("</td></tr>");
            details.append("<tr><td><b>Flight Number:</b></td><td>").append(flight.getFlightNumber())
                    .append("</td></tr>");
            details.append("<tr><td><b>Origin:</b></td><td>").append(flight.getOrigin()).append("</td></tr>");
            details.append("<tr><td><b>Destination:</b></td><td>").append(flight.getDestination()).append("</td></tr>");
            details.append("<tr><td><b>Departure Date:</b></td><td>").append(flight.getDepartureDate())
                    .append("</td></tr>");
            details.append("<tr><td><b>Total Passengers:</b></td><td>").append(flight.getPassengers().size())
                    .append("</td></tr>");
            details.append("</table>");
            details.append("</body></html>");

            JOptionPane.showMessageDialog(this, details.toString(), "Flight Information",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NullPointerException | IllegalArgumentException | FlightBookingSystemException ex) {
            ToastNotification.showToast(this, "Flight not found!", ToastNotification.ToastType.ERROR);
        }
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel customerIcon = new JLabel("👥");
        customerIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        JLabel title = new JLabel("Customer Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 58, 138));

        headerPanel.add(customerIcon);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(title);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Toolbar with search
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton refreshBtn = new JButton("🔄 Refresh");

        styleButton(refreshBtn);

        refreshBtn.addActionListener(e -> {
            refreshCustomersTable(panel);
            ToastNotification.showToast(this, "Customers refreshed successfully!", ToastNotification.ToastType.SUCCESS);
        });
        toolbar.add(refreshBtn);
        toolbar.add(Box.createHorizontalStrut(20));

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        toolbar.add(searchLabel);
        toolbar.add(searchField);
        toolbar.add(searchLabel);
        toolbar.add(searchField);

        panel.add(toolbar, BorderLayout.SOUTH);

        JPanel tableContainer = new JPanel(new BorderLayout());
        panel.add(tableContainer, BorderLayout.CENTER);

        panel.putClientProperty("tableContainer", tableContainer);
        panel.putClientProperty("searchField", searchField);

        refreshCustomersTable(panel);

        return panel;
    }

    private void refreshCustomersTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTextField searchField = (JTextField) panel.getClientProperty("searchField");
        tableContainer.removeAll();

        List<Customer> customersList = fbs.getCustomers();
        String[] columns = new String[] { "ID", "Name", "Phone", "Email", "Address", "Bookings" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Customer customer : customersList) {
            model.addRow(new Object[] {
                    customer.getId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAddress(),
                    customer.getBookings().size()
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(184, 207, 229));

        // Add TableRowSorter for search
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Add search listener
        if (searchField != null) {
            for (DocumentListener dl : searchField.getListeners(DocumentListener.class)) {
                searchField.getDocument().removeDocumentListener(dl);
            }

            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filter();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filter();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filter();
                }

                private void filter() {
                    String text = searchField.getText();
                    if (text.trim().length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                }
            });
        }

        // Add double-click listener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        int modelRow = table.convertRowIndexToModel(row);
                        int customerId = (int) model.getValueAt(modelRow, 0);
                        showCustomerDetails(customerId);
                    }
                }
            }
        });

        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);

        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private void showCustomerDetails(int customerId) {
        try {
            Customer customer = fbs.getCustomerByID(customerId);

            StringBuilder details = new StringBuilder();
            details.append("<html><body style='width: 350px; padding: 10px;'>");
            details.append("<h2 style='color: #1e3a8a;'>Customer Details</h2>");
            details.append("<table>");
            details.append("<tr><td><b>ID:</b></td><td>").append(customer.getId()).append("</td></tr>");
            details.append("<tr><td><b>Name:</b></td><td>").append(customer.getName()).append("</td></tr>");
            details.append("<tr><td><b>Phone:</b></td><td>").append(customer.getPhone()).append("</td></tr>");
            details.append("<tr><td><b>Email:</b></td><td>").append(customer.getEmail()).append("</td></tr>");
            details.append("<tr><td><b>Address:</b></td><td>").append(customer.getAddress()).append("</td></tr>");
            details.append("<tr><td><b>Total Bookings:</b></td><td>").append(customer.getBookings().size())
                    .append("</td></tr>");
            details.append("</table>");

            if (!customer.getBookings().isEmpty()) {
                details.append("<h3 style='color: #1e3a8a; margin-top: 15px;'>Booking History</h3>");
                details.append("<ul>");
                for (Booking booking : customer.getBookings()) {
                    details.append("<li>").append(booking.getFlight().getFlightNumber())
                            .append(" - Seat: ").append(booking.getSeatNumber())
                            .append(" (").append(booking.getBookingClass()).append(")</li>");
                }
                details.append("</ul>");
            }

            details.append("</body></html>");

            JOptionPane.showMessageDialog(this, details.toString(), "Customer Information",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NullPointerException | IllegalArgumentException | FlightBookingSystemException ex) {
            ToastNotification.showToast(this, "Customer not found!", ToastNotification.ToastType.ERROR);
        }
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel bookingIcon = new JLabel("🎫");
        bookingIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        JLabel title = new JLabel("Booking Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 58, 138));

        headerPanel.add(bookingIcon);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(title);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Toolbar with search
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton addBtn = new JButton("🎫 Add Booking");
        JButton cancelBtn = new JButton("❌ Cancel Booking");
        JButton refreshBtn = new JButton("🔄 Refresh");

        styleButton(addBtn);
        styleButton(cancelBtn);
        styleButton(refreshBtn);

        addBtn.addActionListener(e -> {
            AddBookingWindow addBookingWindow = new AddBookingWindow(this);
            addBookingWindow.setVisible(true);
        });

        cancelBtn.addActionListener(e -> {
            ToastNotification.showToast(this, "Cancel Booking feature coming soon!", ToastNotification.ToastType.INFO);
        });
        refreshBtn.addActionListener(e -> {
            refreshBookingsTable(panel);
            ToastNotification.showToast(this, "Bookings refreshed successfully!", ToastNotification.ToastType.SUCCESS);
        });

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        if (!isAdmin) {  // Only add Add Booking button for customers
            toolbar.add(addBtn);
        }
        toolbar.add(cancelBtn);
        toolbar.add(refreshBtn);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(searchLabel);
        toolbar.add(searchField);

        panel.add(toolbar, BorderLayout.SOUTH);

        JPanel tableContainer = new JPanel(new BorderLayout());
        panel.add(tableContainer, BorderLayout.CENTER);

        panel.putClientProperty("tableContainer", tableContainer);
        panel.putClientProperty("searchField", searchField);
        refreshBookingsTable(panel);

        return panel;
    }

    private void refreshBookingsTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTextField searchField = (JTextField) panel.getClientProperty("searchField");
        tableContainer.removeAll();

        // Flatten bookings
        String[] columns = new String[] { "Customer ID", "Customer Name", "Flight No", "Seat", "Class", "Date",
                "Special Requests" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Customer> customers = fbs.getCustomers();
        for (Customer c : customers) {
            for (Booking b : c.getBookings()) {
                model.addRow(new Object[] {
                        c.getId(),
                        c.getName(),
                        b.getFlight().getFlightNumber(),
                        b.getSeatNumber(),
                        b.getBookingClass(),
                        b.getBookingDate(),
                        b.getSpecialRequests()
                });
            }
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(184, 207, 229));

        // Add TableRowSorter for search
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Add search listener
        if (searchField != null) {
            for (DocumentListener dl : searchField.getListeners(DocumentListener.class)) {
                searchField.getDocument().removeDocumentListener(dl);
            }

            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filter();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filter();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filter();
                }

                private void filter() {
                    String text = searchField.getText();
                    if (text.trim().length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                }
            });
        }

        // Add double-click listener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        int modelRow = table.convertRowIndexToModel(row);
                        showBookingDetails(model, modelRow);
                    }
                }
            }
        });

        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);

        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private void showBookingDetails(DefaultTableModel model, int row) {
        StringBuilder details = new StringBuilder();
        details.append("<html><body style='width: 350px; padding: 10px;'>");
        details.append("<h2 style='color: #1e3a8a;'>Booking Details</h2>");
        details.append("<table>");
        details.append("<tr><td><b>Customer ID:</b></td><td>").append(model.getValueAt(row, 0)).append("</td></tr>");
        details.append("<tr><td><b>Customer Name:</b></td><td>").append(model.getValueAt(row, 1)).append("</td></tr>");
        details.append("<tr><td><b>Flight Number:</b></td><td>").append(model.getValueAt(row, 2)).append("</td></tr>");
        details.append("<tr><td><b>Seat Number:</b></td><td>").append(model.getValueAt(row, 3)).append("</td></tr>");
        details.append("<tr><td><b>Class:</b></td><td>").append(model.getValueAt(row, 4)).append("</td></tr>");
        details.append("<tr><td><b>Booking Date:</b></td><td>").append(model.getValueAt(row, 5)).append("</td></tr>");
        details.append("<tr><td><b>Special Requests:</b></td><td>").append(model.getValueAt(row, 6))
                .append("</td></tr>");
        details.append("</table>");
        details.append("</body></html>");

        JOptionPane.showMessageDialog(this, details.toString(), "Booking Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(240, 240, 240));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == homeBtn) {
            setActiveButton(homeBtn);
            refreshHomeStats(statsPanel); // Refresh stats on view
            cardLayout.show(contentPanel, "Home");
        } else if (source == flightsBtn) {
            setActiveButton(flightsBtn);
            refreshFlightsTable(flightsPanel);
            cardLayout.show(contentPanel, "Flights");
        } else if (source == customersBtn) {
            setActiveButton(customersBtn);
            refreshCustomersTable(customersPanel);
            cardLayout.show(contentPanel, "Customers");
        } else if (source == bookingsBtn) {
            setActiveButton(bookingsBtn);
            refreshBookingsTable(bookingsPanel);
            cardLayout.show(contentPanel, "Bookings");
        } else if (source == exitBtn) {
            exitApp();
        }
    }

    private void exitApp() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?\nAll data will be saved automatically.",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                FlightBookingSystemData.store(fbs);
                ToastNotification.showToast(this, "Data saved successfully! Goodbye!",
                        ToastNotification.ToastType.SUCCESS);
                // Give toast time to display
                Timer timer = new Timer(1000, e -> System.exit(0));
                timer.setRepeats(false);
                timer.start();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                int exitAnyway = JOptionPane.showConfirmDialog(
                        this,
                        "Failed to save data. Exit anyway?",
                        "Save Error",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (exitAnyway == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        }
    }

    // Public methods called by other windows to update views
    public void displayFlights() {
        // Switch to flights view and refresh
        setActiveButton(flightsBtn);
        refreshFlightsTable(flightsPanel);
        cardLayout.show(contentPanel, "Flights");
    }

    public void displayCustomers() {
        // Switch to customers view and refresh
        setActiveButton(customersBtn);
        refreshCustomersTable(customersPanel);
        cardLayout.show(contentPanel, "Customers");
    }
}



