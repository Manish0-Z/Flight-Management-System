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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

/**
 * AddCustomerWindow - Modern redesigned dialog for adding customers
 * 
 * CHANGES FROM ORIGINAL:
 * - Increased window size for better spacing (350x220 → 500x420)
 * - Changed from GridLayout to GridBagLayout
 * - Added modern header with icon and description
 * - Using styled text fields from DesignConstants
 * - Modern button styling (Primary and Secondary)
 * - Improved spacing and visual hierarchy
 * - Better color scheme
 * 
 * @author UI/UX Redesign 2026
 */
public class AddCustomerWindow extends JFrame implements ActionListener {

    private final GuiWindow mw;
    private final JTextField nameText;
    private final JTextField phoneText;
    private final JTextField emailText;
    private final JTextField addressText;
    private final JButton addBtn;
    private final JButton cancelBtn;

    public AddCustomerWindow(GuiWindow mw) {
        this.mw = mw;
        
        // CHANGED: Initialize styled text fields
        nameText = DesignConstants.createStyledTextField(20);
        phoneText = DesignConstants.createStyledTextField(20);
        emailText = DesignConstants.createStyledTextField(20);
        addressText = DesignConstants.createStyledTextField(20);
        
        // CHANGED: Initialize styled buttons
        addBtn = DesignConstants.createPrimaryButton("Add Customer");
        cancelBtn = DesignConstants.createSecondaryButton("Cancel");
        
        initialize();
    }

    /**
     * CHANGED: Completely redesigned layout with modern styling
     */
    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Use default LAF
        }

        setTitle("Add a New Customer");
        // CHANGED: Increased size
        setSize(500, 420);
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
        
        JLabel titleLabel = new JLabel("👤 Add New Customer");
        titleLabel.setFont(DesignConstants.FONT_HEADING);
        titleLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        headerPanel.add(Box.createRigidArea(new Dimension(0, DesignConstants.SPACING_SM)));
        
        JLabel subtitleLabel = new JLabel("Enter customer information below");
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
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = DesignConstants.createBodyLabel("Name:");
        nameLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        setPlaceholder(nameText, "Enter customer name");
        formPanel.add(nameText, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel phoneLabel = DesignConstants.createBodyLabel("Phone:");
        phoneLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        setPlaceholder(phoneText, "Enter phone number");
        formPanel.add(phoneText, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel emailLabel = DesignConstants.createBodyLabel("Email:");
        emailLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        setPlaceholder(emailText, "Enter email address");
        formPanel.add(emailText, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel addressLabel = DesignConstants.createBodyLabel("Address:");
        addressLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        formPanel.add(addressLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        setPlaceholder(addressText, "Enter address");
        formPanel.add(addressText, gbc);
        
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
            addCustomer();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void addCustomer() {
        try {
            String name = getActualText(nameText, "Enter customer name");
            String phone = getActualText(phoneText, "Enter phone number");
            String email = getActualText(emailText, "Enter email address");
            String address = getActualText(addressText, "Enter address");

            // Basic validation
            if (name.trim().isEmpty() || phone.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Phone are required fields", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Command addCustomer = new AddCustomer(name, phone, email, address);
            addCustomer.execute(mw.getFlightBookingSystem());

            mw.displayCustomers();

            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * CHANGED: Updated to use DesignConstants colors
     */
    private void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(DesignConstants.TEXT_DISABLED);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(DesignConstants.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(DesignConstants.TEXT_DISABLED);
                }
            }
        });
    }
    
    private String getActualText(JTextField field, String placeholder) {
        String text = field.getText().trim();
        return text.equals(placeholder) ? "" : text;
    }
}
