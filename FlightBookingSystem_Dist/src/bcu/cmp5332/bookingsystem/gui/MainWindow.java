package bcu.cmp5332.bookingsystem.gui;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bcu.cmp5332.bookingsystem.commands.EditBooking;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.User;

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
    private JButton bookFlightBtn;
    private JButton bookingsBtn;
    private JButton exitBtn;

    // Panels
    private JPanel homePanel;
    private JPanel flightsPanel;
    private JPanel customersPanel;
    private JPanel bookFlightPanel;
    private JPanel bookingsPanel;
    private JPanel statsPanel;

    // Enhanced sidebar color scheme
    private static final Color SIDEBAR_COLOR = new Color(30, 58, 138);
    private static final Color SIDEBAR_TEXT_COLOR = Color.WHITE;
    private static final Color ACTIVE_BTN_COLOR = new Color(52, 73, 154);
    private static final Color HOVER_BTN_COLOR = new Color(69, 90, 171);
    private static final Color ACCENT_BORDER = new Color(255, 193, 7);
    private static final Color ICON_COLOR = new Color(200, 215, 255);
    private static final Color ICON_ACTIVE_COLOR = Color.WHITE;

    private boolean isAdmin;

    public MainWindow(FlightBookingSystem fbs, boolean isAdmin, User loggedInUser) {
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
        if (isAdmin) {
            customersPanel = createCustomersPanel();
            contentPanel.add(customersPanel, "Customers");
        } else {
            bookFlightPanel = createBookFlightPanel();
            contentPanel.add(bookFlightPanel, "BookFlight");
        }
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
        
        if (isAdmin) {
            customersBtn = createSidebarButton("Customers", SidebarIcon.IconType.CUSTOMERS);
        } else {
            bookFlightBtn = createSidebarButton("Book Flight", SidebarIcon.IconType.FLIGHTS);
        }
        
        bookingsBtn = createSidebarButton("Bookings", SidebarIcon.IconType.BOOKINGS);

        sidebar.add(homeBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(flightsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        
        if (isAdmin) {
            sidebar.add(customersBtn);
        } else {
            sidebar.add(bookFlightBtn);
        }
        
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
        JButton[] btns = isAdmin ? 
            new JButton[]{ homeBtn, flightsBtn, customersBtn, bookingsBtn } :
            new JButton[]{ homeBtn, flightsBtn, bookFlightBtn, bookingsBtn };
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

        // Modern header with icon and subtitle
        AirplaneIcon headerIcon = new AirplaneIcon(40, 28);
        ModernHeader header = new ModernHeader(
            headerIcon,
            "Flight Management System",
            "Welcome to your dashboard - Your complete flight management solution",
            ModernHeader.HeaderStyle.LIGHT
        );

        panel.add(header, BorderLayout.NORTH);

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

        // Modern header with flight icon
        AirplaneIcon flightIcon = new AirplaneIcon(32, 22);
        ModernHeader header = new ModernHeader(
            flightIcon,
            "Flight Management",
            "View, manage, and track all flights in the system",
            ModernHeader.HeaderStyle.LIGHT
        );

        panel.add(header, BorderLayout.NORTH);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        if (isAdmin) {
            JButton addBtn = new JButton("✈️ Add Flight");
            JButton updateBtn = new JButton("✏️ Update Flight");
            JButton deleteBtn = new JButton("🗑️ Delete Flight");
            styleButton(addBtn);
            styleButton(updateBtn);
            styleButton(deleteBtn);
            addBtn.addActionListener(e -> {
                System.out.println("Add Flight button clicked!");
                AddFlightWindow addFlightWindow = new AddFlightWindow(this);
                addFlightWindow.setVisible(true);
            });
            updateBtn.addActionListener(e -> updateSelectedFlight(panel));
            deleteBtn.addActionListener(e -> deleteSelectedFlight(panel));
            toolbar.add(addBtn);
            toolbar.add(updateBtn);
            toolbar.add(deleteBtn);
            panel.add(toolbar, BorderLayout.SOUTH);
        }

        // Table container
        JPanel tableContainer = new JPanel(new BorderLayout());
        panel.add(tableContainer, BorderLayout.CENTER);

        // Store references
        panel.putClientProperty("tableContainer", tableContainer);

        refreshFlightsTable(panel);

        return panel;
    }

    private void refreshFlightsTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        tableContainer.removeAll();

        List<Flight> flightsList = fbs.getFlights();
        String[] columns = new String[] { "ID", "Flight No", "Origin", "Destination", "Departure Date", "Capacity", "Price" };
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
                    flight.getCapacity(),
                    "$" + flight.getPrice()
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(184, 207, 229));

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

    private void deleteSelectedFlight(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTable table = null;
        for (Component comp : tableContainer.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    table = (JTable) view;
                    break;
                }
            }
        }
        
        if (table == null) return;
        
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the flight number from the selected row (column 1)
        int modelRow = table.convertRowIndexToModel(selectedRow);
        String flightNumber = (String) table.getModel().getValueAt(modelRow, 1);
        
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

    private void updateSelectedFlight(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
        JTable table = null;
        for (Component comp : tableContainer.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    table = (JTable) view;
                    break;
                }
            }
        }
        
        if (table == null) return;
        
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the flight ID from the selected row (column 0)
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int flightId = (int) table.getModel().getValueAt(modelRow, 0);
        
        try {
            Flight flight = fbs.getFlightByID(flightId);
            UpdateFlightWindow updateFlightWindow = new UpdateFlightWindow(this, flight);
            updateFlightWindow.setVisible(true);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Modern header with customer icon
        JLabel customerIcon = new JLabel("👥");
        customerIcon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        ModernHeader header = new ModernHeader(
            customerIcon,
            "Customer Management",
            "Manage customer profiles, bookings, and information",
            ModernHeader.HeaderStyle.LIGHT
        );

        panel.add(header, BorderLayout.NORTH);

        JPanel tableContainer = new JPanel(new BorderLayout());
        panel.add(tableContainer, BorderLayout.CENTER);

        panel.putClientProperty("tableContainer", tableContainer);

        refreshCustomersTable(panel);

        return panel;
    }

    private void refreshCustomersTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
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

    private JPanel createBookFlightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Modern header with booking accent
        AirplaneIcon flightIcon = new AirplaneIcon(32, 22);
        ModernHeader header = new ModernHeader(
            flightIcon,
            "Book Flight",
            "Reserve seats on available flights quickly and easily",
            ModernHeader.HeaderStyle.ACCENT
        );

        panel.add(header, BorderLayout.NORTH);

        // Center content with button
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JButton bookBtn = new JButton("Book a Flight");
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bookBtn.setBackground(new Color(30, 58, 138));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFocusPainted(false);
        bookBtn.setBorderPainted(false);
        bookBtn.setPreferredSize(new Dimension(200, 50));
        bookBtn.setMaximumSize(new Dimension(200, 50));
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.addActionListener(e -> bookFlight());
        
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(bookBtn);
        centerPanel.add(Box.createVerticalGlue());

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Modern header with booking icon
        JLabel bookingIcon = new JLabel("🎫");
        bookingIcon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        ModernHeader header = new ModernHeader(
            bookingIcon,
            "Booking Management",
            "View, create, and manage flight reservations",
            ModernHeader.HeaderStyle.LIGHT
        );

        panel.add(header, BorderLayout.NORTH);

        // Toolbar - centered for customers
        JPanel toolbar = new JPanel(new FlowLayout(isAdmin ? FlowLayout.LEFT : FlowLayout.CENTER));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton cancelBtn = new JButton("❌ Cancel Booking");
        styleButton(cancelBtn);

        cancelBtn.addActionListener(e -> {
            cancelBooking();
        });

        if (isAdmin) {
            JButton addBtn = new JButton("🎫 Add Booking");
            styleButton(addBtn);
            addBtn.addActionListener(e -> {
                AddBookingWindow addBookingWindow = new AddBookingWindow(this);
                addBookingWindow.setVisible(true);
            });
            toolbar.add(addBtn);
        }
        
        if (!isAdmin) {
            JButton rescheduleBtn = new JButton("🔄 Reschedule Booking");
            styleButton(rescheduleBtn);

            rescheduleBtn.addActionListener(e -> {
                rescheduleBooking();
            });
            
            toolbar.add(rescheduleBtn);
            toolbar.add(Box.createHorizontalStrut(10));
        }

        toolbar.add(cancelBtn);

        panel.add(toolbar, BorderLayout.SOUTH);

        JPanel tableContainer = new JPanel(new BorderLayout());
        panel.add(tableContainer, BorderLayout.CENTER);

        panel.putClientProperty("tableContainer", tableContainer);
        refreshBookingsTable(panel);

        return panel;
    }

    private void refreshBookingsTable(JPanel panel) {
        JPanel tableContainer = (JPanel) panel.getClientProperty("tableContainer");
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
        } else if (source == customersBtn && isAdmin) {
            setActiveButton(customersBtn);
            refreshCustomersTable(customersPanel);
            cardLayout.show(contentPanel, "Customers");
        } else if (source == bookFlightBtn && !isAdmin) {
            setActiveButton(bookFlightBtn);
            cardLayout.show(contentPanel, "BookFlight");
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
                ToastNotification.showToast(this, "Data saved successfully!",
                        ToastNotification.ToastType.SUCCESS);
                // Give toast time to display then return to role selection
                Timer timer = new Timer(1000, e -> {
                    dispose();
                    new RoleSelectionWindow(fbs).setVisible(true);
                });
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
                    dispose();
                    new RoleSelectionWindow(fbs).setVisible(true);
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

    private void bookFlight() {
        JDialog dialog = new JDialog(this, "Book a Flight", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Customer Name
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Customer Name:"));
        JTextField nameField = new JTextField(20);
        namePanel.add(nameField);
        panel.add(namePanel);

        // Flight ID
        JPanel flightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flightPanel.add(new JLabel("Flight ID:"));
        JTextField flightField = new JTextField(20);
        flightPanel.add(flightField);
        panel.add(flightPanel);

        // Email
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(20);
        emailPanel.add(emailField);
        panel.add(emailPanel);

        // Phone Number
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel.add(new JLabel("Phone Number:"));
        JTextField phoneField = new JTextField(20);
        phonePanel.add(phoneField);
        panel.add(phonePanel);

        // Seat Type
        JPanel seatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        seatPanel.add(new JLabel("Seat Type:"));
        JComboBox<String> seatCombo = new JComboBox<>(new String[]{"Economy", "Business", "First Class"});
        seatPanel.add(seatCombo);
        panel.add(seatPanel);

        // Food Type
        JPanel foodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        foodPanel.add(new JLabel("Food Type:"));
        JComboBox<String> foodCombo = new JComboBox<>(new String[]{"Veg", "Non-Veg"});
        foodPanel.add(foodCombo);
        panel.add(foodPanel);

        // Any Request
        JPanel requestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        requestPanel.add(new JLabel("Any Request:"));
        JTextArea requestArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(requestArea);
        requestPanel.add(scrollPane);
        panel.add(requestPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton bookButton = new JButton("Book");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel);

        dialog.add(panel, BorderLayout.CENTER);

        bookButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String flightIdStr = flightField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String seatType = (String) seatCombo.getSelectedItem();
            String foodType = (String) foodCombo.getSelectedItem();
            String request = requestArea.getText().trim();

            if (name.isEmpty() || flightIdStr.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int flightId = Integer.parseInt(flightIdStr);
                Flight flight = fbs.getFlightByID(flightId);
                if (flight == null) {
                    JOptionPane.showMessageDialog(dialog, "Flight not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Find or create customer
                Customer customer = fbs.getCustomers().stream()
                    .filter(c -> c.getName().equals(name) && c.getEmail().equals(email))
                    .findFirst().orElse(null);

                if (customer == null) {
                    // Create new customer
                    int customerId = fbs.getNextCustomerId();
                    customer = new Customer(customerId, name, phone, email, "");
                    fbs.addCustomer(customer);
                }

                // Create booking
                int bookingId = fbs.getNextBookingId();
                Booking booking = new Booking(bookingId, customer, flight, fbs.getSystemDate(), "1", seatType, foodType + " - " + request);
                customer.addBooking(booking);
                flight.addPassenger(customer);

                JOptionPane.showMessageDialog(dialog, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();

                // Refresh views
                refreshBookingsTable(bookingsPanel);
                refreshFlightsTable(flightsPanel);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid flight ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void cancelBooking() {
        JDialog dialog = new JDialog(this, "Cancel Booking", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Flight ID
        JPanel flightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flightPanel.add(new JLabel("Flight ID:"));
        JTextField flightField = new JTextField(20);
        flightPanel.add(flightField);
        panel.add(flightPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Reason
        JPanel reasonPanel = new JPanel();
        reasonPanel.setLayout(new BoxLayout(reasonPanel, BoxLayout.Y_AXIS));
        JLabel reasonLabel = new JLabel("Reason for Cancellation:");
        reasonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        reasonPanel.add(reasonLabel);
        reasonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JTextArea reasonArea = new JTextArea(5, 20);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        reasonPanel.add(scrollPane);
        panel.add(reasonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton cancelBookingButton = new JButton("Cancel Booking");
        JButton closeButton = new JButton("Close");
        
        styleButton(cancelBookingButton);
        styleButton(closeButton);
        
        cancelBookingButton.setBackground(new Color(239, 68, 68));
        cancelBookingButton.setForeground(Color.WHITE);
        
        buttonPanel.add(cancelBookingButton);
        buttonPanel.add(closeButton);
        panel.add(buttonPanel);

        dialog.add(panel, BorderLayout.CENTER);

        cancelBookingButton.addActionListener(e -> {
            String flightIdStr = flightField.getText().trim();
            String reason = reasonArea.getText().trim();

            if (flightIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter Flight ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (reason.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a reason for cancellation.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int flightId = Integer.parseInt(flightIdStr);
                Flight flight = fbs.getFlightByID(flightId);
                if (flight == null) {
                    JOptionPane.showMessageDialog(dialog, "Flight not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Find customer bookings for this flight
                boolean bookingFound = false;
                for (Customer customer : fbs.getCustomers()) {
                    List<Booking> bookingsToRemove = customer.getBookings().stream()
                        .filter(b -> b.getFlight().getId() == flightId)
                        .toList();
                    
                    if (!bookingsToRemove.isEmpty()) {
                        for (Booking booking : bookingsToRemove) {
                            customer.getBookings().remove(booking);
                            flight.getPassengers().remove(customer);
                        }
                        bookingFound = true;
                        break;
                    }
                }

                if (bookingFound) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Booking cancelled successfully!\nReason: " + reason, 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    
                    // Refresh views
                    refreshBookingsTable(bookingsPanel);
                    refreshFlightsTable(flightsPanel);
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "No booking found for this flight.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid Flight ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void rescheduleBooking() {
        JDialog dialog = new JDialog(this, "Reschedule Booking", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Customer ID
        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerPanel.add(new JLabel("Customer ID:"));
        JTextField customerField = new JTextField(20);
        customerPanel.add(customerField);
        panel.add(customerPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Current Flight ID
        JPanel currentFlightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        currentFlightPanel.add(new JLabel("Current Flight ID:"));
        JTextField currentFlightField = new JTextField(20);
        currentFlightPanel.add(currentFlightField);
        panel.add(currentFlightPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // New Flight ID
        JPanel newFlightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newFlightPanel.add(new JLabel("New Flight ID:"));
        JTextField newFlightField = new JTextField(20);
        newFlightPanel.add(newFlightField);
        panel.add(newFlightPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton rescheduleButton = new JButton("Reschedule Booking");
        JButton closeButton = new JButton("Close");
        
        styleButton(rescheduleButton);
        styleButton(closeButton);
        
        rescheduleButton.setBackground(new Color(34, 197, 94));
        rescheduleButton.setForeground(Color.BLACK);
        closeButton.setForeground(Color.BLACK);
        
        buttonPanel.add(rescheduleButton);
        buttonPanel.add(closeButton);
        panel.add(buttonPanel);

        dialog.add(panel, BorderLayout.CENTER);

        rescheduleButton.addActionListener(e -> {
            String customerIdStr = customerField.getText().trim();
            String currentFlightIdStr = currentFlightField.getText().trim();
            String newFlightIdStr = newFlightField.getText().trim();

            if (customerIdStr.isEmpty() || currentFlightIdStr.isEmpty() || newFlightIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int customerId = Integer.parseInt(customerIdStr);
                int currentFlightId = Integer.parseInt(currentFlightIdStr);
                int newFlightId = Integer.parseInt(newFlightIdStr);

                if (currentFlightId == newFlightId) {
                    JOptionPane.showMessageDialog(dialog, "Current and new flight IDs cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                EditBooking editCommand = new EditBooking(customerId, currentFlightId, newFlightId);
                editCommand.execute(fbs);

                JOptionPane.showMessageDialog(dialog, 
                    "Booking rescheduled successfully!\nCustomer " + customerId + " changed from Flight " + currentFlightId + " to " + newFlightId, 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                
                // Refresh views
                refreshBookingsTable(bookingsPanel);
                refreshFlightsTable(flightsPanel);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid ID. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}



