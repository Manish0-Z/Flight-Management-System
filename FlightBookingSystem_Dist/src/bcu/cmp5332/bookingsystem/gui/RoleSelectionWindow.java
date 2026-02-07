package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
<<<<<<< HEAD
import javax.swing.SwingConstants;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.User;
=======
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
>>>>>>> a64682e21958cf54e06495b49a79abfbace553bd

/**
 * RoleSelectionWindow - Modern redesigned role selection interface
 * Improved visual hierarchy, spacing, and user experience
 * 
 * CHANGES FROM ORIGINAL:
 * - Increased window size for better spacing
 * - Added modern card-based layout for role buttons
 * - Enhanced visual hierarchy with larger icons and better typography
 * - Added hover effects and smooth interactions
 * - Improved color scheme and contrast
 * - Added subtitle and description for better UX
 * 
 * @author UI/UX Redesign 2026
 */
public class RoleSelectionWindow extends JFrame {
    private FlightBookingSystem fbs;

    public RoleSelectionWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    private void initialize() {
        // CHANGED: Set system look and feel for better native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default if system LAF not available
        }
        
        setTitle("Flight Management System - Role Selection");
        // CHANGED: Increased size for better spacing and modern layout
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // CHANGED: Set background color for modern appearance
        getContentPane().setBackground(DesignConstants.BACKGROUND);

<<<<<<< HEAD
        JLabel titleLabel = new JLabel("Access the system as:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton adminButton = new JButton("Admin");
        JButton customerButton = new JButton("Customer");

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog("admin");
            }
        });

        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog("customer");
            }
        });

        buttonPanel.add(adminButton);
        buttonPanel.add(customerButton);
=======
        // CHANGED: Create header with logo and better typography
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
>>>>>>> a64682e21958cf54e06495b49a79abfbace553bd

        // CHANGED: Create card-based button panel with modern styling
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }
<<<<<<< HEAD

    private void showLoginDialog(String role) {
        LoginDialog loginDialog = new LoginDialog(this, fbs, role);
        loginDialog.setVisible(true);

        if (loginDialog.isLoggedIn()) {
            User loggedInUser = loginDialog.getLoggedInUser();
            if ("admin".equals(role)) {
                new AdminMainWindow(fbs).setVisible(true);
            } else if ("customer".equals(role)) {
                new CustomerMainWindow(fbs, loggedInUser).setVisible(true);
            }
            dispose();
        }
=======
    
    /**
     * Creates a modern header panel with logo and title
     * CHANGED: New method for better visual hierarchy
     */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(DesignConstants.BACKGROUND);
        header.setBorder(new EmptyBorder(
            DesignConstants.SPACING_XL, 
            DesignConstants.SPACING_MD, 
            DesignConstants.SPACING_LG, 
            DesignConstants.SPACING_MD
        ));
        
        // Logo/Icon
        JLabel iconLabel = new JLabel("✈");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setForeground(DesignConstants.PRIMARY);
        iconLabel.setAlignmentX(CENTER_ALIGNMENT);
        header.add(iconLabel);
        
        header.add(Box.createRigidArea(new Dimension(0, DesignConstants.SPACING_MD)));
        
        // Title
        JLabel titleLabel = new JLabel("Flight Management System");
        titleLabel.setFont(DesignConstants.FONT_HEADING_LARGE);
        titleLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        header.add(titleLabel);
        
        header.add(Box.createRigidArea(new Dimension(0, DesignConstants.SPACING_SM)));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Select your access level");
        subtitleLabel.setFont(DesignConstants.FONT_BODY);
        subtitleLabel.setForeground(DesignConstants.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);
        header.add(subtitleLabel);
        
        return header;
    }
    
    /**
     * Creates modern card-based button panel
     * CHANGED: Completely redesigned layout with cards instead of simple buttons
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(DesignConstants.BACKGROUND);
        panel.setBorder(new EmptyBorder(0, DesignConstants.SPACING_XL, DesignConstants.SPACING_XL, DesignConstants.SPACING_XL));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(DesignConstants.SPACING_SM, 0, DesignConstants.SPACING_SM, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // CHANGED: Create card-style buttons
        JPanel adminCard = createRoleCard(
            "🔐 Administrator",
            "Full system access and management",
            DesignConstants.PRIMARY,
            e -> {
                new MainWindow(fbs, true).setVisible(true);
                dispose();
            }
        );
        panel.add(adminCard, gbc);
        
        gbc.gridy = 1;
        JPanel customerCard = createRoleCard(
            "👤 Customer",
            "Book flights and manage your bookings",
            DesignConstants.ACCENT,
            e -> {
                new MainWindow(fbs, false).setVisible(true);
                dispose();
            }
        );
        panel.add(customerCard, gbc);
        
        return panel;
    }
    
    /**
     * Creates a modern card-style role selection button
     * CHANGED: New method for card-based design
     */
    private JPanel createRoleCard(String title, String description, Color accentColor, ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(DesignConstants.SPACING_MD, DesignConstants.SPACING_SM));
        card.setBackground(DesignConstants.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DesignConstants.BORDER, 1),
            new EmptyBorder(
                DesignConstants.SPACING_LG,
                DesignConstants.SPACING_LG,
                DesignConstants.SPACING_LG,
                DesignConstants.SPACING_LG
            )
        ));
        card.setPreferredSize(new Dimension(380, 90));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Left: Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(DesignConstants.SURFACE);
        contentPanel.setAlignmentY(CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        contentPanel.add(titleLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(DesignConstants.FONT_SMALL);
        descLabel.setForeground(DesignConstants.TEXT_SECONDARY);
        contentPanel.add(descLabel);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        // Right: Arrow indicator
        JLabel arrow = new JLabel("→");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 24));
        arrow.setForeground(accentColor);
        card.add(arrow, BorderLayout.EAST);
        
        // CHANGED: Add hover effects for better interactivity
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(DesignConstants.SURFACE_HOVER);
                contentPanel.setBackground(DesignConstants.SURFACE_HOVER);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    new EmptyBorder(
                        DesignConstants.SPACING_LG - 1,
                        DesignConstants.SPACING_LG - 1,
                        DesignConstants.SPACING_LG - 1,
                        DesignConstants.SPACING_LG - 1
                    )
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(DesignConstants.SURFACE);
                contentPanel.setBackground(DesignConstants.SURFACE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(DesignConstants.BORDER, 1),
                    new EmptyBorder(
                        DesignConstants.SPACING_LG,
                        DesignConstants.SPACING_LG,
                        DesignConstants.SPACING_LG,
                        DesignConstants.SPACING_LG
                    )
                ));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, null));
            }
        });
        
        return card;
>>>>>>> a64682e21958cf54e06495b49a79abfbace553bd
    }
}