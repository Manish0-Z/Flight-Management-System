package bcu.cmp5332.bookingsystem.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

/**
 * AddFlightWindow - Modern redesigned dialog for adding flights
 * 
 * CHANGES FROM ORIGINAL:
 * - Increased window size for better spacing
 * - Changed from GridLayout to GridBagLayout for better control
 * - Added styled text fields using DesignConstants
 * - Improved button styling with modern appearance
 * - Enhanced visual hierarchy with better typography
 * - Added proper padding and spacing
 * - Improved label alignment and formatting
 * - Better color scheme
 * - Added field validation hints
 * 
 * @author UI/UX Redesign 2026
 */
public class AddFlightWindow extends JFrame implements ActionListener {

    private final GuiWindow mw;
    private final JTextField flightNoText;
    private final JTextField originText;
    private final JTextField destinationText;
    private final JTextField depDateText;
    private final JTextField capacityText;
    private final JTextField priceText;

    private final JButton addBtn;
    private final JButton cancelBtn;

    public AddFlightWindow(GuiWindow mw) {
        this.mw = mw;
        
        System.out.println("Creating AddFlightWindow...");
        
        // CHANGED: Initialize styled text fields
        flightNoText = DesignConstants.createStyledTextField(15);
        originText = DesignConstants.createStyledTextField(15);
        destinationText = DesignConstants.createStyledTextField(15);
        depDateText = DesignConstants.createStyledTextField(15);
        capacityText = DesignConstants.createStyledTextField(15);
        priceText = DesignConstants.createStyledTextField(15);
        
        // CHANGED: Initialize styled buttons
        addBtn = DesignConstants.createPrimaryButton("Add Flight");
        cancelBtn = DesignConstants.createSecondaryButton("Cancel");
        
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     * CHANGED: Completely redesigned layout with modern styling
     */
    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Use default LAF
        }

        setTitle("Add a New Flight");
        // CHANGED: Increased size for better spacing
        setSize(500, 450);
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
        
        JLabel titleLabel = new JLabel("✈ Add New Flight");
        titleLabel.setFont(DesignConstants.FONT_HEADING);
        titleLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        headerPanel.add(Box.createRigidArea(new Dimension(0, DesignConstants.SPACING_SM)));
        
        JLabel subtitleLabel = new JLabel("Enter flight details below");
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
        
        // Flight Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel flightNoLabel = DesignConstants.createBodyLabel("Flight Name:");
        flightNoLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(flightNoLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(flightNoText, gbc);
        
        // Origin
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel originLabel = DesignConstants.createBodyLabel("Origin:");
        originLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(originLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(originText, gbc);
        
        // Destination
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel destLabel = DesignConstants.createBodyLabel("Destination:");
        destLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(destLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(destinationText, gbc);
        
        // TakeOff Date
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel dateLabel = DesignConstants.createBodyLabel("TakeOff Date:");
        dateLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPanel datePanel = new JPanel(new BorderLayout(DesignConstants.SPACING_SM, 0));
        datePanel.setBackground(DesignConstants.BACKGROUND);
        datePanel.add(depDateText, BorderLayout.CENTER);
        JLabel dateHint = new JLabel("(YYYY-MM-DD format)");
        dateHint.setFont(DesignConstants.FONT_SMALL);
        dateHint.setForeground(DesignConstants.TEXT_SECONDARY);
        datePanel.add(dateHint, BorderLayout.EAST);
        formPanel.add(datePanel, gbc);
        
        // Capacity
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel capacityLabel = DesignConstants.createBodyLabel("Capacity:");
        capacityLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(capacityLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(capacityText, gbc);
        
        // Price
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel priceLabel = DesignConstants.createBodyLabel("Price:");
        priceLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(priceLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(priceText, gbc);
        
        // Wrap formPanel in JScrollPane for scrolling when window is small
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null); // Remove border for cleaner look
        add(scrollPane, BorderLayout.CENTER);
        
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

        setAlwaysOnTop(true);
        setVisible(true);
        toFront();
        requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addBook();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void addBook() {
        try {
            String flightNumber = flightNoText.getText();
            String origin = originText.getText();
            String destination = destinationText.getText();
            LocalDate departureDate = null;
            try {
                departureDate = LocalDate.parse(depDateText.getText());
            }
            catch (DateTimeParseException dtpe) {
                throw new FlightBookingSystemException("Date must be in YYYY-MM-DD format");
            }
            int capacity = Integer.parseInt(capacityText.getText());
            double price = Double.parseDouble(priceText.getText());
            // create and execute the AddFlight Command
            Command addFlight = new AddFlight(flightNumber, origin, destination, departureDate, capacity, price);
            addFlight.execute(mw.getFlightBookingSystem());
            // refresh the view with the list of flights
            mw.displayFlights();
            // hide (close) the AddFlightWindow
            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
