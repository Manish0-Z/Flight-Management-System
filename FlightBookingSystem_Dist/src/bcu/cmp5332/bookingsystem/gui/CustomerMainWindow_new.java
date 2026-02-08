package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.User;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CustomerMainWindow_new extends JFrame implements ActionListener, GuiWindow {
    // Enhanced sidebar color scheme
    private static final Color SIDEBAR_COLOR = new Color(30, 58, 138);
    private static final Color SIDEBAR_TEXT_COLOR = Color.WHITE;
    private static final Color HOVER_BTN_COLOR = new Color(50, 78, 158);
    private static final Color ACTIVE_BTN_COLOR = new Color(60, 98, 178);
    private static final Color AIRLINE_ACCENT = new Color(255, 193, 7);
    private static final Color ACCENT_BORDER = new Color(255, 193, 7);
    private static final Color ICON_COLOR = new Color(200, 215, 255);
    private static final Color ICON_ACTIVE_COLOR = Color.WHITE;

    private final FlightBookingSystem fbs;
    private final User loggedInUser;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JButton homeBtn, flightsBtn, bookingsBtn, profileBtn, exitBtn;
    private JPanel homePanel, flightsPanel, bookingsPanel;

    public CustomerMainWindow_new(FlightBookingSystem fbs, User loggedInUser) {
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
        setIconImage(DesignConstants.getAppIconImage());
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
        bookingsBtn = createSidebarButton("My Bookings", SidebarIcon.IconType.BOOKINGS);
        profileBtn = createSidebarButton("Profile", SidebarIcon.IconType.PROFILE);


        sidebar.add(homeBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(flightsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(bookingsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(profileBtn);


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
        btn.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 12));
        btn.setPreferredSize(new Dimension(236, 48));
        btn.setForeground(SIDEBAR_TEXT_COLOR);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add icon
        SidebarIcon icon = new SidebarIcon(iconType, 20);
        icon.setIconColor(ICON_COLOR);
        btn.add(icon);
        
        // Add label
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(SIDEBAR_TEXT_COLOR);
        btn.add(label);
        
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
        JButton[] btns = { homeBtn, flightsBtn, bookingsBtn, profileBtn };
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
        JPanel actionsPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        actionsPanel.setBackground(Color.WHITE);

        JButton viewFlightsBtn = new JButton("✈️ View Flights");
        JButton viewBookingsBtn = new JButton("🎫 My Bookings");

        styleButton(viewFlightsBtn);
        styleButton(viewBookingsBtn);

        viewFlightsBtn.addActionListener(e -> {
            setActiveButton(flightsBtn);
            cardLayout.show(contentPanel, "Flights");
        });

        viewBookingsBtn.addActionListener(e -> {
            setActiveButton(bookingsBtn);
            cardLayout.show(contentPanel, "Bookings");
        });

        actionsPanel.add(viewFlightsBtn);
        actionsPanel.add(viewBookingsBtn);

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

        JButton refreshBtn = new JButton("🔄 Refresh");
        // Removed per-flight "Book Flight" button as requested

        styleButton(refreshBtn);

        refreshBtn.addActionListener(e -> {
            refreshFlightsTable(panel);
            ToastNotification.showToast(this, "Flights refreshed successfully!", ToastNotification.ToastType.SUCCESS);
        });

        toolbar.add(refreshBtn);
        toolbar.add(Box.createHorizontalStrut(10));
        // ...existing code...

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        toolbar.add(Box.createHorizontalStrut(20));
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

        JButton refreshBtn = new JButton("🔄 Refresh");
        JButton cancelBtn = new JButton("❌ Cancel Booking");

        styleButton(refreshBtn);
        styleButton(cancelBtn);
        cancelBtn.setForeground(Color.BLACK);

        refreshBtn.addActionListener(e -> {
            refreshBookingsTable(panel);
            ToastNotification.showToast(this, "Bookings refreshed successfully!", ToastNotification.ToastType.SUCCESS);
        });

        cancelBtn.addActionListener(e -> {
            int selectedRow = getSelectedBookingRow(panel);
            if (selectedRow >= 0) {
                cancelSelectedBooking(selectedRow);
            } else {
                ToastNotification.showToast(this, "Please select a booking to cancel!", ToastNotification.ToastType.WARNING);
            }
        });

        toolbar.add(refreshBtn);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(cancelBtn);

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

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

    public void refreshBookingsIfVisible() {
        // If bookings panel is currently visible, refresh it
        if ("Bookings".equals(getCurrentCard())) {
            refreshBookingsTable(bookingsPanel);
        }
    }

    private String getCurrentCard() {
        for (Component comp : contentPanel.getComponents()) {
            if (comp.isVisible()) {
                return comp.getName();
            }
        }
        return null;
    }

    private int getSelectedBookingRow(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        if (tableContainer.getComponentCount() > 0) {
            JScrollPane scrollPane = (JScrollPane) tableContainer.getComponent(0);
            JTable table = (JTable) scrollPane.getViewport().getView();
            return table.getSelectedRow();
        }
        return -1;
    }

    private void cancelSelectedBooking(int selectedRow) {
        try {
            Customer customer = fbs.getCustomerByEmail(loggedInUser.getUsername());
            
            // Get all non-deleted bookings
            List<Booking> allBookings = fbs.getBookings().stream()
                .filter(booking -> !booking.isDeleted())
                .collect(Collectors.toList());
            
            if (selectedRow >= 0 && selectedRow < allBookings.size()) {
                Booking booking = allBookings.get(selectedRow);
                
                // Check if the booking belongs to the logged-in customer
                if (booking.getCustomer().getId() != customer.getId()) {
                    ToastNotification.showToast(this, "You can only cancel your own bookings!", ToastNotification.ToastType.WARNING);
                    return;
                }
                
                int result = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to cancel booking for flight " + booking.getFlight().getFlightNumber() + "?",
                    "Cancel Booking", JOptionPane.YES_NO_OPTION);
                
                if (result == JOptionPane.YES_OPTION) {
                    // Create cancel booking command
                    bcu.cmp5332.bookingsystem.commands.CancelBooking cancelCmd = 
                        new bcu.cmp5332.bookingsystem.commands.CancelBooking(customer.getId(), booking.getFlight().getId());
                    cancelCmd.execute(fbs);
                    
                    ToastNotification.showToast(this, "Booking cancelled successfully!", ToastNotification.ToastType.SUCCESS);
                    refreshBookingsTable(bookingsPanel);
                }
            }
        } catch (FlightBookingSystemException ex) {
            ToastNotification.showToast(this, "Error cancelling booking: " + ex.getMessage(), ToastNotification.ToastType.ERROR);
        }
    }

    private void refreshBookingsTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTextField searchField = (JTextField) panel.getClientProperty("searchField");

        tableContainer.removeAll();

        String[] columnNames = {"Booking ID", "Customer", "Flight", "Seat No", "Date Booked", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Booking> allBookings = fbs.getBookings();
        for (Booking booking : allBookings) {
            if (!booking.isDeleted()) {
                Object[] row = {
                    booking.getId(),
                    booking.getCustomer().getName(),
                    booking.getFlight().getFlightNumber(),
                    booking.getSeatNumber(),
                    booking.getBookingDate(),
                    booking.getStatus()
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
        } else if (e.getSource() == profileBtn) {
            setActiveButton(profileBtn);
            cardLayout.show(contentPanel, "Profile");
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