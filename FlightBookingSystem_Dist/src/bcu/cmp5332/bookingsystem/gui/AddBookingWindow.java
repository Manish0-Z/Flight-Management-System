package bcu.cmp5332.bookingsystem.gui;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

/**
 * AddBookingWindow - Modern redesigned dialog for issuing bookings
 * 
 * CHANGES FROM ORIGINAL:
 * - Increased window size (400x280 → 520x480)
 * - Changed to GridBagLayout for better control
 * - Added modern header with icon
 * - Using styled text fields from DesignConstants
 * - Modern styled ComboBox
 * - Improved button styling
 * - Better spacing and visual hierarchy
 * - Professional color scheme
 * 
 * @author UI/UX Redesign 2026
 */
public class AddBookingWindow extends JFrame implements ActionListener {

    private final GuiWindow mw;
    private final JTextField custIdText;
    private final JTextField flightIdText;
    private final JTextField seatNumberText;
    private JComboBox<String> bookingClassCombo;
    private final JTextField specialRequestsText;

    private final JButton addBtn;
    private final JButton cancelBtn;

    public AddBookingWindow(GuiWindow mw) {
        this.mw = mw;
        
        // CHANGED: Initialize styled text fields
        custIdText = DesignConstants.createStyledTextField(20);
        flightIdText = DesignConstants.createStyledTextField(20);
        seatNumberText = DesignConstants.createStyledTextField(20);
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
        seatPanel.add(seatNumberText, BorderLayout.CENTER);
        JLabel seatHint = new JLabel("(e.g., 12A, 5B)");
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
            String seatNumber = seatNumberText.getText();
            String bookingClass = (String) bookingClassCombo.getSelectedItem();
            String specialRequests = specialRequestsText.getText();

            // Basic validation
            if (seatNumber.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seat Number is required", "Validation Error",
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
}
