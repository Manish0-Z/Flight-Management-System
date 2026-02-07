package bcu.cmp5332.bookingsystem.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.User;

public class CustomerBookingDialog extends JDialog implements ActionListener {

    private final CustomerMainWindow parent;
    private final Flight flight;
    private final User loggedInUser;

    private final JTextField seatNumberText;
    private JComboBox<String> bookingClassCombo;
    private final JTextField specialRequestsText;

    private final JButton bookBtn;
    private final JButton cancelBtn;

    public CustomerBookingDialog(CustomerMainWindow parent, Flight flight, User loggedInUser) {
        super(parent, "Book Flight", true);
        this.parent = parent;
        this.flight = flight;
        this.loggedInUser = loggedInUser;

        // Initialize components
        seatNumberText = DesignConstants.createStyledTextField(20);
        specialRequestsText = DesignConstants.createStyledTextField(20);

        bookBtn = DesignConstants.createPrimaryButton("Book Flight");
        cancelBtn = DesignConstants.createSecondaryButton("Cancel");

        initialize();
    }

    private void initialize() {
        setTitle("Book Flight - " + flight.getFlightNumber());
        setSize(450, 350);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DesignConstants.BACKGROUND);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(DesignConstants.BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("🎫 Book Flight: " + flight.getFlightNumber());
        titleLabel.setFont(DesignConstants.FONT_HEADING);
        titleLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel routeLabel = new JLabel(flight.getOrigin() + " → " + flight.getDestination());
        routeLabel.setFont(DesignConstants.FONT_BODY);
        routeLabel.setForeground(DesignConstants.TEXT_SECONDARY);
        routeLabel.setAlignmentX(CENTER_ALIGNMENT);
        headerPanel.add(routeLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(DesignConstants.BACKGROUND);
        formPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Seat Number
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel seatLabel = DesignConstants.createBodyLabel("Seat Number:");
        seatLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(seatLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPanel seatPanel = new JPanel(new BorderLayout(5, 0));
        seatPanel.setBackground(DesignConstants.BACKGROUND);
        seatPanel.add(seatNumberText, BorderLayout.CENTER);
        JLabel seatHint = new JLabel("(e.g., 12A, 5B)");
        seatHint.setFont(DesignConstants.FONT_SMALL);
        seatHint.setForeground(DesignConstants.TEXT_SECONDARY);
        seatPanel.add(seatHint, BorderLayout.EAST);
        formPanel.add(seatPanel, gbc);

        // Booking Class
        gbc.gridx = 0; gbc.gridy = 1;
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
        bookingClassCombo.setFont(DesignConstants.FONT_BODY);
        bookingClassCombo.setBackground(DesignConstants.SURFACE);
        bookingClassCombo.setForeground(DesignConstants.TEXT_PRIMARY);
        bookingClassCombo.setPreferredSize(new Dimension(bookingClassCombo.getPreferredSize().width, DesignConstants.INPUT_HEIGHT));
        formPanel.add(bookingClassCombo, gbc);

        // Special Requests
        gbc.gridx = 0; gbc.gridy = 2;
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

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(DesignConstants.BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(bookBtn);

        bookBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(parent);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bookBtn) {
            bookFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void bookFlight() {
        try {
            // Get customer by email
            Customer customer = parent.getFlightBookingSystem().getCustomerByEmail(loggedInUser.getUsername());

            String seatNumber = seatNumberText.getText().trim();
            String bookingClass = (String) bookingClassCombo.getSelectedItem();
            String specialRequests = specialRequestsText.getText();

            // Validation
            if (seatNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seat Number is required", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create booking
            Command addBooking = new AddBooking(customer.getId(), flight.getId(), seatNumber, bookingClass, specialRequests);
            addBooking.execute(parent.getFlightBookingSystem());

            JOptionPane.showMessageDialog(this, "Flight booked successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh the bookings view if it exists
            parent.refreshBookingsIfVisible();

            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}