package bcu.cmp5332.bookingsystem.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
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

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.User;

class AirplaneIcon extends JPanel {
    private final Color planeColor = new Color(255, 193, 7);

    public AirplaneIcon(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw airplane body (fuselage)
        g2d.setColor(planeColor);
        g2d.fillRoundRect(width / 4, height / 2 - 8, width / 2, 16, 8, 8);

        // Draw wings
        g2d.fillRoundRect(width / 3, height / 2 - 20, width / 3, 8, 4, 4);
        g2d.fillRoundRect(width / 3, height / 2 + 12, width / 3, 8, 4, 4);

        // Draw tail
        g2d.fillRoundRect(width - width / 4 - 6, height / 2 - 25, 8, 20, 4, 4);

        // Draw windows
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            g2d.fillOval(width / 3 + 15 + i * 12, height / 2 - 4, 6, 8);
        }

        g2d.dispose();
    }
}

// Toast Notification Class
class ToastNotification extends JWindow {
    private static final int TOAST_WIDTH = 350;
    private static final int TOAST_HEIGHT = 80;
    private static final int DISPLAY_TIME = 3000; // 3 seconds

    public enum ToastType {
        SUCCESS, ERROR, INFO, WARNING
    }

    public ToastNotification(JFrame parent, String message, ToastType type) {
        setSize(TOAST_WIDTH, TOAST_HEIGHT);
        setAlwaysOnTop(true);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Set colors based on type
        Color bgColor, textColor;
        String icon;
        switch (type) {
            case SUCCESS:
                bgColor = new Color(76, 175, 80);
                textColor = Color.WHITE;
                icon = "✓";
                break;
            case ERROR:
                bgColor = new Color(244, 67, 54);
                textColor = Color.WHITE;
                icon = "✗";
                break;
            case WARNING:
                bgColor = new Color(255, 152, 0);
                textColor = Color.WHITE;
                icon = "⚠";
                break;
            default: // INFO
                bgColor = new Color(33, 150, 243);
                textColor = Color.WHITE;
                icon = "ℹ";
                break;
        }

        panel.setBackground(bgColor);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(textColor);

        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageArea.setForeground(textColor);
        messageArea.setBackground(bgColor);
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setOpaque(false);

        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(messageArea, BorderLayout.CENTER);

        add(panel);

        // Position the toast
        if (parent != null) {
            Point location = parent.getLocationOnScreen();
            setLocation(location.x + parent.getWidth() / 2 - TOAST_WIDTH / 2,
                       location.y + parent.getHeight() / 2 - TOAST_HEIGHT / 2);
        } else {
            setLocationRelativeTo(null);
        }

        // Auto-hide after display time
        Timer timer = new Timer(DISPLAY_TIME, e -> setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }

    public static void showToast(JFrame parent, String message, ToastType type) {
        ToastNotification toast = new ToastNotification(parent, message, type);
        toast.setVisible(true);
    }
}

public class CustomerMainWindow extends JFrame implements ActionListener, GuiWindow {
    private static final Color SIDEBAR_COLOR = new Color(30, 58, 138);
    private static final Color SIDEBAR_TEXT_COLOR = Color.WHITE;
    private static final Color HOVER_BTN_COLOR = new Color(50, 78, 158);
    private static final Color ACTIVE_BTN_COLOR = new Color(60, 98, 178);
    private static final Color AIRLINE_ACCENT = new Color(255, 193, 7);

    private final FlightBookingSystem fbs;
    private final User loggedInUser;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JButton homeBtn, flightsBtn, bookingsBtn, exitBtn;
    private JPanel homePanel, flightsPanel, bookingsPanel;

    public CustomerMainWindow(FlightBookingSystem fbs, User loggedInUser) {
        this.fbs = fbs;
        this.loggedInUser = loggedInUser;
        initialize();
    }

    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Log or ignore
        }

        setTitle("Flight Booking System - Customer Dashboard");
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
        bookingsPanel = createBookingsPanel();

        contentPanel.add(homePanel, "Home");
        contentPanel.add(flightsPanel, "Flights");
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
        headerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Airplane icon
        AirplaneIcon planeIcon = new AirplaneIcon(60, 40);
        planeIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(planeIcon);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Customer Portal");
        subtitleLabel.setForeground(AIRLINE_ACCENT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        sidebar.add(headerPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        homeBtn = createSidebarButton("Dashboard", "🏠");
        flightsBtn = createSidebarButton("Flights", "✈️");
        bookingsBtn = createSidebarButton("My Bookings", "🎫");

        sidebar.add(homeBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(flightsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(bookingsBtn);

        // Spacer to push exit button to bottom
        sidebar.add(Box.createVerticalGlue());

        exitBtn = createSidebarButton("Exit", "🚪");
        exitBtn.setBackground(new Color(180, 50, 50));
        sidebar.add(exitBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        return sidebar;
    }

    private JButton createSidebarButton(String text, String icon) {
        JButton btn = new JButton(icon + " " + text);
        btn.setMaximumSize(new Dimension(240, 50));
        btn.setPreferredSize(new Dimension(240, 50));
        btn.setForeground(SIDEBAR_TEXT_COLOR);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.getBackground() != ACTIVE_BTN_COLOR && btn != exitBtn)
                    btn.setBackground(HOVER_BTN_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.getBackground() != ACTIVE_BTN_COLOR && btn != exitBtn)
                    btn.setBackground(SIDEBAR_COLOR);
            }
        });

        btn.addActionListener(this);
        return btn;
    }

    private void setActiveButton(JButton active) {
        JButton[] btns = { homeBtn, flightsBtn, bookingsBtn };
        for (JButton btn : btns) {
            btn.setBackground(SIDEBAR_COLOR);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }
        active.setBackground(ACTIVE_BTN_COLOR);
        active.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    // --- Content Panels ---

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Welcome header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255)); // Light blue background
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel welcomeLabel = new JLabel("Welcome to Flight Booking System");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(30, 58, 138)); // Airline blue

        AirplaneIcon headerPlane = new AirplaneIcon(40, 30);
        headerPanel.add(headerPlane);
        headerPanel.add(Box.createHorizontalStrut(15));
        headerPanel.add(welcomeLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Main content area
        JPanel contentArea = new JPanel(new BorderLayout(0, 20));
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Quick actions
        JPanel actionsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        actionsPanel.setBackground(Color.WHITE);

        JButton viewFlightsBtn = new JButton("✈️ View Flights");
        JButton myBookingsBtn = new JButton("🎫 My Bookings");
        JButton bookFlightBtn = new JButton("📅 Book Flight");

        styleButton(viewFlightsBtn);
        styleButton(myBookingsBtn);
        styleButton(bookFlightBtn);

        viewFlightsBtn.addActionListener(e -> {
            setActiveButton(flightsBtn);
            cardLayout.show(contentPanel, "Flights");
        });

        myBookingsBtn.addActionListener(e -> {
            setActiveButton(bookingsBtn);
            cardLayout.show(contentPanel, "Bookings");
        });

        bookFlightBtn.addActionListener(e -> {
            AddBookingWindow addBookingWindow = new AddBookingWindow(this);
            addBookingWindow.setVisible(true);
        });

        actionsPanel.add(viewFlightsBtn);
        actionsPanel.add(myBookingsBtn);
        actionsPanel.add(bookFlightBtn);

        contentArea.add(actionsPanel, BorderLayout.CENTER);

        panel.add(contentArea, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel flightIcon = new JLabel("✈️");
        flightIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        JLabel title = new JLabel("Available Flights");
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

        JButton bookBtn = new JButton("📅 Book Flight");
        JButton refreshBtn = new JButton("🔄 Refresh");

        styleButton(bookBtn);
        styleButton(refreshBtn);

        bookBtn.addActionListener(e -> {
            AddBookingWindow addBookingWindow = new AddBookingWindow(this);
            addBookingWindow.setVisible(true);
        });
        refreshBtn.addActionListener(e -> {
            refreshFlightsTable(panel);
            ToastNotification.showToast(this, "Flights refreshed successfully!", ToastNotification.ToastType.SUCCESS);
        });
        toolbar.add(bookBtn);
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

        JPanel tableContainer = new JPanel(new BorderLayout());
        panel.add(tableContainer, BorderLayout.CENTER);

        panel.putClientProperty("tableContainer", tableContainer);
        panel.putClientProperty("searchField", searchField);

        refreshFlightsTable(panel);

        return panel;
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
        JLabel title = new JLabel("My Bookings");
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

        JButton bookBtn = new JButton("📅 New Booking");
        JButton cancelBtn = new JButton("❌ Cancel Booking");
        JButton refreshBtn = new JButton("🔄 Refresh");

        styleButton(bookBtn);
        styleButton(cancelBtn);
        styleButton(refreshBtn);

        bookBtn.addActionListener(e -> {
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

        toolbar.add(bookBtn);
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

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(30, 58, 138));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(50, 78, 158));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 58, 138));
            }
        });
    }

    // --- Table Refresh Methods ---

    private void refreshFlightsTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTextField searchField = (JTextField) panel.getClientProperty("searchField");

        tableContainer.removeAll();

        String[] columnNames = {"Flight No", "Origin", "Destination", "Departure Date", "Capacity", "Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Flight> flights = fbs.getFlights();
        for (Flight flight : flights) {
            if (!flight.isDeleted()) {
                Object[] row = {
                    flight.getFlightNumber(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getDepartureDate(),
                    flight.getCapacity(),
                    "$" + flight.getPrice()
                };
                model.addRow(row);
            }
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }

            private void filterTable() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private void refreshBookingsTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTextField searchField = (JTextField) panel.getClientProperty("searchField");

        tableContainer.removeAll();

        String[] columnNames = {"Booking ID", "Flight", "Date Booked", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            Customer customer = fbs.getCustomerByEmail(loggedInUser.getUsername());
            List<Booking> bookings = customer.getBookings();
            for (Booking booking : bookings) {
                if (!booking.isDeleted()) {
                    Object[] row = {
                        booking.getId(),
                        booking.getFlight().getFlightNumber(),
                        booking.getBookingDate(),
                        booking.getStatus()
                    };
                    model.addRow(row);
                }
            }
        } catch (NumberFormatException | FlightBookingSystemException e) {
            // If can't parse or find customer, show no bookings
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }

            private void filterTable() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == homeBtn) {
            setActiveButton(homeBtn);
            cardLayout.show(contentPanel, "Home");
        } else if (e.getSource() == flightsBtn) {
            setActiveButton(flightsBtn);
            cardLayout.show(contentPanel, "Flights");
        } else if (e.getSource() == bookingsBtn) {
            setActiveButton(bookingsBtn);
            cardLayout.show(contentPanel, "Bookings");
        } else if (e.getSource() == exitBtn) {
            try {
                FlightBookingSystemData.store(fbs);
                ToastNotification.showToast(this, "Data saved successfully!", ToastNotification.ToastType.SUCCESS);
            } catch (IOException ex) {
                ToastNotification.showToast(this, "Error saving data: " + ex.getMessage(), ToastNotification.ToastType.ERROR);
            }
            System.exit(0);
        }
    }
}