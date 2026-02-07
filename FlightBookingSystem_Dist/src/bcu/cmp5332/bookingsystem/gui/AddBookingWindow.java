package bcu.cmp5332.bookingsystem.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;


public class AddBookingWindow extends JFrame implements ActionListener {

    private final GuiWindow mw;
    private final JTextField custIdText;
    private final JTextField flightIdText;
    private final JComboBox<String> seatNumberCombo;
    private JComboBox<String> bookingClassCombo;
    private final JTextField specialRequestsText;

    private final JButton addBtn;
    private final JButton cancelBtn;

    public AddBookingWindow(GuiWindow mw) {
        this.mw = mw;
        
        // CHANGED: Initialize styled text fields
        custIdText = DesignConstants.createStyledTextField(20);
        flightIdText = DesignConstants.createStyledTextField(20);
        seatNumberCombo = new JComboBox<>();
        seatNumberCombo.setFont(DesignConstants.FONT_BODY);
        seatNumberCombo.setBackground(DesignConstants.SURFACE);
        seatNumberCombo.setForeground(DesignConstants.TEXT_PRIMARY);
        seatNumberCombo.setPreferredSize(new Dimension(seatNumberCombo.getPreferredSize().width, DesignConstants.INPUT_HEIGHT));
        specialRequestsText = DesignConstants.createStyledTextField(20);
        
        // CHANGED: Initialize styled buttons
        addBtn = DesignConstants.createPrimaryButton("Issue Booking");
        cancelBtn = DesignConstants.createSecondaryButton("Cancel");
        
        initialize();
    }

    /**
     * CHANGED: Completely redesigned layout with modern styling
     */
    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                    IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Use default LAF
        }

        setTitle("Issue Booking");
        // CHANGED: Increased size
        setSize(520, 480);
        setLayout(new BorderLayout());
        
        // CHANGED: Set background color
        getContentPane().setBackground(DesignConstants.BACKGROUND);
        
        // CHANGED: Create header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(DesignConstants.BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(
            DesignConstants.SPACING_LG,
            DesignConstants.SPACING_LG,
            DesignConstants.SPACING_MD,
            DesignConstants.SPACING_LG
        ));
        
        JLabel titleLabel = new JLabel("🎫 Issue New Booking");
        titleLabel.setFont(DesignConstants.FONT_HEADING);
        titleLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        headerPanel.add(Box.createRigidArea(new Dimension(0, DesignConstants.SPACING_SM)));
        
        JLabel subtitleLabel = new JLabel("Create a booking for a customer");
        subtitleLabel.setFont(DesignConstants.FONT_BODY);
        subtitleLabel.setForeground(DesignConstants.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // CHANGED: Create form panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(DesignConstants.BACKGROUND);
        formPanel.setBorder(new EmptyBorder(
            0,
            DesignConstants.SPACING_XL,
            DesignConstants.SPACING_LG,
            DesignConstants.SPACING_XL
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(DesignConstants.SPACING_SM, 0, DesignConstants.SPACING_SM, DesignConstants.SPACING_MD);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Customer ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel custIdLabel = DesignConstants.createBodyLabel("Customer ID:");
        custIdLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(custIdLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(custIdText, gbc);
        
        // Flight ID
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel flightIdLabel = DesignConstants.createBodyLabel("Flight ID:");
        flightIdLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(flightIdLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(flightIdText, gbc);
        
        // Seat Number
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel seatLabel = DesignConstants.createBodyLabel("Seat Number:");
        seatLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(seatLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPanel seatPanel = new JPanel(new BorderLayout(DesignConstants.SPACING_SM, 0));
        seatPanel.setBackground(DesignConstants.BACKGROUND);
        seatPanel.add(seatNumberCombo, BorderLayout.CENTER);
        JLabel seatHint = new JLabel("(Select available seat)");
        seatHint.setFont(DesignConstants.FONT_SMALL);
        seatHint.setForeground(DesignConstants.TEXT_SECONDARY);
        seatPanel.add(seatHint, BorderLayout.EAST);
        formPanel.add(seatPanel, gbc);
        
        // Booking Class
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel classLabel = DesignConstants.createBodyLabel("Booking Class:");
        classLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(classLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        String[] classes = { "Economy", "Premium Economy", "Business", "First Class" };
        bookingClassCombo = new JComboBox<>(classes);
        // CHANGED: Style the combo box
        bookingClassCombo.setFont(DesignConstants.FONT_BODY);
        bookingClassCombo.setBackground(DesignConstants.SURFACE);
        bookingClassCombo.setForeground(DesignConstants.TEXT_PRIMARY);
        bookingClassCombo.setPreferredSize(new Dimension(bookingClassCombo.getPreferredSize().width, DesignConstants.INPUT_HEIGHT));
        
        // Add listener to update seats when class changes
        bookingClassCombo.addActionListener(e -> updateAvailableSeats());
        
        formPanel.add(bookingClassCombo, gbc);
        
        // Special Requests
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel requestsLabel = DesignConstants.createBodyLabel("Special Requests:");
        requestsLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(requestsLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(specialRequestsText, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // CHANGED: Create button panel with better spacing
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(DesignConstants.BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(
            0,
            DesignConstants.SPACING_XL,
            DesignConstants.SPACING_LG,
            DesignConstants.SPACING_XL
        ));
        
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(DesignConstants.SPACING_MD, 0)));
        buttonPanel.add(addBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo((Component) mw);
        
        // Initialize seat list
        updateAvailableSeats();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addBooking();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void addBooking() {
        try {
            int custId = Integer.parseInt(custIdText.getText().trim());
            int flightId = Integer.parseInt(flightIdText.getText().trim());
            String seatNumber = (String) seatNumberCombo.getSelectedItem();
            String bookingClass = (String) bookingClassCombo.getSelectedItem();
            String specialRequests = specialRequestsText.getText();

            // Basic validation
            if (seatNumber == null || seatNumber.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a seat number", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Command addBooking = new AddBooking(custId, flightId, seatNumber, bookingClass, specialRequests);
            addBooking.execute(mw.getFlightBookingSystem());

            JOptionPane.showMessageDialog(this, "Booking issued successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "IDs must be integers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAvailableSeats() {
        String selectedClass = (String) bookingClassCombo.getSelectedItem();
        if (selectedClass == null) return;

        // Get flight ID if entered
        int flightId = -1;
        try {
            if (!flightIdText.getText().trim().isEmpty()) {
                flightId = Integer.parseInt(flightIdText.getText().trim());
            }
        } catch (NumberFormatException e) {
            // Flight ID not entered or invalid, show all seats
        }

        // Get all seats for the selected class
        List<String> allSeats = getSeatsForClass(selectedClass);
        
        // Get booked seats for this flight
        List<String> bookedSeats = getBookedSeatsForFlight(flightId);
        
        // Create model with colored items
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        for (String seat : allSeats) {
            model.addElement(seat);
        }
        
        seatNumberCombo.setModel(model);
        
        // Set custom renderer for color coding
        seatNumberCombo.setRenderer(new SeatRenderer(bookedSeats));
        
        // Select first available seat if any
        if (model.getSize() > 0) {
            seatNumberCombo.setSelectedIndex(0);
        }
    }

    private List<String> getSeatsForClass(String bookingClass) {
        List<String> seats = new ArrayList<>();
        String[] seatLetters = {"A", "B", "C", "D", "E", "F"};
        
        switch (bookingClass) {
            case "Economy":
                // Economy: rows 1-30, seats A-F
                for (int row = 1; row <= 30; row++) {
                    for (String letter : seatLetters) {
                        seats.add(row + letter);
                    }
                }
                break;
            case "Premium Economy":
                // Premium Economy: rows 31-35, seats A-F
                for (int row = 31; row <= 35; row++) {
                    for (String letter : seatLetters) {
                        seats.add(row + letter);
                    }
                }
                break;
            case "Business":
                // Business: rows 1-5, seats A-F
                for (int row = 1; row <= 5; row++) {
                    for (String letter : seatLetters) {
                        seats.add(row + letter);
                    }
                }
                break;
            case "First Class":
                // First Class: rows 1-2, seats A-F
                for (int row = 1; row <= 2; row++) {
                    for (String letter : seatLetters) {
                        seats.add(row + letter);
                    }
                }
                break;
        }
        
        return seats;
    }

    private List<String> getBookedSeatsForFlight(int flightId) {
        List<String> bookedSeats = new ArrayList<>();
        
        if (flightId <= 0) {
            return bookedSeats; // No flight selected, return empty list
        }
        
        // Get all bookings for this flight
        for (Booking booking : mw.getFlightBookingSystem().getBookings()) {
            if (!booking.isDeleted() && booking.getFlight().getId() == flightId) {
                bookedSeats.add(booking.getSeatNumber());
            }
        }
        
        return bookedSeats;
    }

    // Custom renderer for seat combo box with color coding
    private class SeatRenderer extends JLabel implements ListCellRenderer<String> {
        private List<String> bookedSeats;
        
        public SeatRenderer(List<String> bookedSeats) {
            this.bookedSeats = bookedSeats;
            setOpaque(true);
        }
        
        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            
            setText(value);
            setFont(DesignConstants.FONT_BODY);
            
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                // Color code based on availability
                if (bookedSeats.contains(value)) {
                    setBackground(Color.RED);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(DesignConstants.TEXT_PRIMARY);
                }
            }
            
            return this;
        }
    }
}
