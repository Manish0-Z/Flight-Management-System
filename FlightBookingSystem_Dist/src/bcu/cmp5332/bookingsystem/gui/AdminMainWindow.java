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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
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



public class AdminMainWindow extends JFrame implements ActionListener, GuiWindow {
    // Enhanced sidebar color scheme
    private static final Color SIDEBAR_COLOR = DesignConstants.SIDEBAR_BG;
    private static final Color SIDEBAR_TEXT_COLOR = DesignConstants.SIDEBAR_TEXT;
    private static final Color HOVER_BTN_COLOR = DesignConstants.SIDEBAR_HOVER;
    private static final Color ACTIVE_BTN_COLOR = DesignConstants.SIDEBAR_ACTIVE;
    private static final Color AIRLINE_ACCENT = DesignConstants.ACCENT;
    private static final Color ACCENT_BORDER = DesignConstants.ACCENT;
    private static final Color ICON_COLOR = DesignConstants.PRIMARY_LIGHTER;
    private static final Color ICON_ACTIVE_COLOR = DesignConstants.TEXT_ON_PRIMARY;

    private final FlightBookingSystem fbs;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JButton homeBtn, flightsBtn, customersBtn, bookingsBtn, exitBtn;
    private JPanel homePanel, flightsPanel, customersPanel, bookingsPanel;

    public AdminMainWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
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
        DesignConstants.applyDarkThemeDefaults();

        setTitle("Flight Booking System - Admin Dashboard");
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
        contentPanel.setBackground(DesignConstants.BACKGROUND);

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
        headerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Airplane icon
        AirplaneIcon planeIcon = new AirplaneIcon(60, 40);
        planeIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(planeIcon);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Management System");
        subtitleLabel.setForeground(AIRLINE_ACCENT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        sidebar.add(headerPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Navigation section label
        JLabel navLabel = new JLabel("NAVIGATION");
        navLabel.setForeground(DesignConstants.TEXT_DISABLED);
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
        separator.setForeground(DesignConstants.SIDEBAR_ACTIVE);
        sidebar.add(separator);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        exitBtn = createSidebarButton("Exit", SidebarIcon.IconType.EXIT);
        exitBtn.setBackground(DesignConstants.ERROR);
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
                    icon.setIconColor(DesignConstants.TEXT_ON_PRIMARY);
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
        panel.setBackground(DesignConstants.BACKGROUND);

        // Modern admin dashboard header
        AirplaneIcon headerIcon = new AirplaneIcon(40, 28);
        ModernHeader header = new ModernHeader(
            headerIcon,
            "Flight Management System",
            "Admin Dashboard - Complete system overview and management",
            ModernHeader.HeaderStyle.LIGHT
        );

        panel.add(header, BorderLayout.NORTH);

        // Main content area with stats and chart
        JPanel contentArea = new JPanel(new BorderLayout(0, 20));
        contentArea.setBackground(DesignConstants.BACKGROUND);
        contentArea.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(DesignConstants.BACKGROUND);

        // Flights stat
        JPanel flightsStat = createStatPanel("✈️ Total Flights", String.valueOf(fbs.getFlights().size()), DesignConstants.PRIMARY);
        statsPanel.add(flightsStat);

        // Customers stat
        JPanel customersStat = createStatPanel("👥 Total Customers", String.valueOf(fbs.getCustomers().size()), DesignConstants.SUCCESS);
        statsPanel.add(customersStat);

        // Bookings stat
        JPanel bookingsStat = createStatPanel("🎫 Total Bookings", String.valueOf(fbs.getBookings().size()), DesignConstants.WARNING);
        statsPanel.add(bookingsStat);

        // Revenue stat (placeholder)
        JPanel revenueStat = createStatPanel("💰 Revenue", "$0.00", DesignConstants.ACCENT_DARK);
        statsPanel.add(revenueStat);

        contentArea.add(statsPanel, BorderLayout.NORTH);

        // Placeholder for chart or additional content
        JPanel chartPanel = new JPanel();
        chartPanel.setBackground(DesignConstants.SURFACE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("System Overview"));
        JLabel chartPlaceholder = new JLabel("Chart visualization would go here", SwingConstants.CENTER);
        chartPlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        chartPlaceholder.setForeground(DesignConstants.TEXT_SECONDARY);
        chartPanel.add(chartPlaceholder);

        contentArea.add(chartPanel, BorderLayout.CENTER);

        panel.add(contentArea, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatPanel(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DesignConstants.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new DesignConstants.RoundedBorder(DesignConstants.BORDER_RADIUS, DesignConstants.BORDER),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(DesignConstants.TEXT_SECONDARY);

        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(titleLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DesignConstants.BACKGROUND);

        // Modern flight management header
        JLabel flightIcon = new JLabel("✈️");
        flightIcon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        ModernHeader header = new ModernHeader(
            flightIcon,
            "Flight Management",
            "Create, update, and manage all flights in the system",
            ModernHeader.HeaderStyle.LIGHT
        );

        panel.add(header, BorderLayout.NORTH);

        // Toolbar with search
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(DesignConstants.BACKGROUND);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton addBtn = new JButton("✈️ Add Flight");
        JButton deleteBtn = new JButton("🗑️ Delete Flight");

        styleButton(addBtn);
        styleButton(deleteBtn);

        addBtn.addActionListener(e -> {
            AddFlightWindow addFlightWindow = new AddFlightWindow(this);
            addFlightWindow.setVisible(true);
        });
        deleteBtn.addActionListener(e -> deleteSelectedFlight(panel));
        
        toolbar.add(addBtn);
        toolbar.add(deleteBtn);
        toolbar.add(Box.createHorizontalStrut(20));

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(DesignConstants.SURFACE);
        searchField.setForeground(DesignConstants.TEXT_PRIMARY);
        searchField.setCaretColor(DesignConstants.TEXT_PRIMARY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new DesignConstants.RoundedBorder(DesignConstants.BORDER_RADIUS_SMALL, DesignConstants.BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchLabel.setForeground(DesignConstants.TEXT_SECONDARY);

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

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DesignConstants.BACKGROUND);

        // Modern customer management header
        JLabel customerIcon = new JLabel("👥");
        customerIcon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        ModernHeader header = new ModernHeader(
            customerIcon,
            "Customer Management",
            "View and manage customer profiles, bookings, and contact information",
            ModernHeader.HeaderStyle.LIGHT
        );

        panel.add(header, BorderLayout.NORTH);

        // Toolbar with search
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(DesignConstants.BACKGROUND);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(DesignConstants.SURFACE);
        searchField.setForeground(DesignConstants.TEXT_PRIMARY);
        searchField.setCaretColor(DesignConstants.TEXT_PRIMARY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new DesignConstants.RoundedBorder(DesignConstants.BORDER_RADIUS_SMALL, DesignConstants.BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchLabel.setForeground(DesignConstants.TEXT_SECONDARY);

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

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DesignConstants.BACKGROUND);

        // Modern booking management header
        JLabel bookingIcon = new JLabel("🎫");
        bookingIcon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        ModernHeader header = new ModernHeader(
            bookingIcon,
            "Booking Management",
            "Monitor, search, and manage all flight reservations",
            ModernHeader.HeaderStyle.LIGHT
        );

        panel.add(header, BorderLayout.NORTH);

        // Toolbar with search
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(DesignConstants.BACKGROUND);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(DesignConstants.SURFACE);
        searchField.setForeground(DesignConstants.TEXT_PRIMARY);
        searchField.setCaretColor(DesignConstants.TEXT_PRIMARY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new DesignConstants.RoundedBorder(DesignConstants.BORDER_RADIUS_SMALL, DesignConstants.BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchLabel.setForeground(DesignConstants.TEXT_SECONDARY);

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
        button.setBackground(DesignConstants.PRIMARY);
        button.setForeground(DesignConstants.TEXT_ON_PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(new DesignConstants.RoundedBorder(DesignConstants.BORDER_RADIUS_SMALL, DesignConstants.BORDER));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(DesignConstants.PRIMARY_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(DesignConstants.PRIMARY);
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
        DesignConstants.styleTable(table);

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
        DesignConstants.styleScrollPane(scrollPane);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private void deleteSelectedFlight(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTable table = findTableInContainer(tableContainer);
        
        if (table == null) return;
        
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the flight number from the selected row
        int modelRow = table.convertRowIndexToModel(selectedRow);
        String flightNumber = (String) table.getModel().getValueAt(modelRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete flight " + flightNumber + "?\nThis will also cancel all associated bookings.", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Flight flight = null;
                for (Flight f : fbs.getFlights()) {
                    if (f.getFlightNumber().equals(flightNumber)) {
                        flight = f;
                        break;
                    }
                }
                if (flight == null) {
                    throw new FlightBookingSystemException("Flight not found");
                }
                
                // Cancel all bookings for this flight
                List<Customer> customers = fbs.getCustomers();
                int cancelledBookings = 0;
                for (Customer customer : customers) {
                    List<Booking> bookingsToRemove = new ArrayList<>();
                    for (Booking booking : customer.getBookings()) {
                        if (booking.getFlight().getId() == flight.getId()) {
                            bookingsToRemove.add(booking);
                        }
                    }
                    for (Booking booking : bookingsToRemove) {
                        customer.removeBooking(booking);
                        fbs.removeBooking(booking);
                        cancelledBookings++;
                    }
                }
                
                // Delete the flight
                fbs.removeFlight(flight);
                
                JOptionPane.showMessageDialog(this, 
                    "Flight " + flightNumber + " has been deleted.\n" + cancelledBookings + " booking(s) were cancelled.", 
                    "Flight Deleted", JOptionPane.INFORMATION_MESSAGE);
                
                refreshFlightsTable(panel);
                
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting flight: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JTable findTableInContainer(JPanel container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    return (JTable) view;
                }
            }
        }
        return null;
    }

    private void refreshCustomersTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTextField searchField = (JTextField) panel.getClientProperty("searchField");

        tableContainer.removeAll();

        String[] columnNames = {"ID", "Name", "Phone", "Email"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Customer> customers = fbs.getCustomers();
        for (Customer customer : customers) {
            if (!customer.isDeleted()) {
                Object[] row = {
                    customer.getId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getEmail()
                };
                model.addRow(row);
            }
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DesignConstants.styleTable(table);

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
        DesignConstants.styleScrollPane(scrollPane);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private void refreshBookingsTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTextField searchField = (JTextField) panel.getClientProperty("searchField");

        tableContainer.removeAll();

        String[] columnNames = {"ID", "Customer", "Flight", "Booking Date", "Seat", "Class", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Booking> bookings = fbs.getBookings();
        for (Booking booking : bookings) {
            if (!booking.isDeleted()) {
                Object[] row = {
                    booking.getId(),
                    booking.getCustomer().getName(),
                    booking.getFlight().getFlightNumber(),
                    booking.getBookingDate(),
                    booking.getSeatNumber(),
                    booking.getBookingClass(),
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
        DesignConstants.styleTable(table);

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
        } else if (e.getSource() == customersBtn) {
            setActiveButton(customersBtn);
            cardLayout.show(contentPanel, "Customers");
        } else if (e.getSource() == bookingsBtn) {
            setActiveButton(bookingsBtn);
            cardLayout.show(contentPanel, "Bookings");
        } else if (e.getSource() == exitBtn) {
            try {
                FlightBookingSystemData.store(fbs);
            } catch (IOException ex) {
                // Error saving data: " + ex.getMessage()
            }
            System.exit(0);
        }
    }
}