package bcu.cmp5332.bookingsystem.gui;

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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;


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
        DesignConstants.applyDarkThemeDefaults();
        
        setTitle("Flight Management System - Role Selection");
        // CHANGED: Increased size for better spacing and modern layout
        setSize(500, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // CHANGED: Set background color for modern appearance
        // Dark base for the role selection screen.
        getContentPane().setBackground(DesignConstants.BACKGROUND);

    // CHANGED: Create header with logo and better typography
    JPanel headerPanel = createHeaderPanel();
    add(headerPanel, BorderLayout.NORTH);

    // CHANGED: Create card-based button panel with modern styling
    JPanel buttonPanel = createButtonPanel();
    add(buttonPanel, BorderLayout.CENTER);

    setVisible(true);
}
    
    
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
        
        // Logo/Icon - Airplane
        AirlineLogo logo = new AirlineLogo(120, 60);
        logo.setAlignmentX(CENTER_ALIGNMENT);
        header.add(logo);
        
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
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        
        // CHANGED: Create card-style buttons with custom icons
        JPanel adminCard = createRoleCard(
            "Administrator",
            "Full system access and management",
            DesignConstants.PRIMARY,
            RoleIcon.RoleType.ADMIN,
            e -> {
                LoginDialog loginDialog = new LoginDialog(this, fbs, "admin");
                loginDialog.setVisible(true);
                if (loginDialog.isLoggedIn()) {
                    new MainWindow(fbs, true, loginDialog.getLoggedInUser()).setVisible(true);
                    dispose();
                }
            }
        );
        panel.add(adminCard, gbc);
        
        gbc.gridy = 1;
        JPanel customerCard = createRoleCard(
            "Customer",
            "Book flights and manage your bookings",
            DesignConstants.ACCENT,
            RoleIcon.RoleType.CUSTOMER,
            e -> {
                LoginDialog loginDialog = new LoginDialog(this, fbs, "customer");
                loginDialog.setVisible(true);
                if (loginDialog.isLoggedIn()) {
                    new MainWindow(fbs, false, loginDialog.getLoggedInUser()).setVisible(true);
                    dispose();
                }
            }
        );
        panel.add(customerCard, gbc);
        
        return panel;
    }
    
    /**
     * Creates a modern card-style role selection button with custom icon
     * CHANGED: New method for card-based design with RoleIcon
     */
    private JPanel createRoleCard(String title, String description, Color accentColor, RoleIcon.RoleType iconType, ActionListener action) {
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
        card.setPreferredSize(new Dimension(380, 110));
        card.setMinimumSize(new Dimension(320, 110));
        card.setMaximumSize(new Dimension(500, 110));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Left: Icon with subtle background
        Color iconBgColor = new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30);
        RoleIcon icon = new RoleIcon(iconType, 64, accentColor, iconBgColor);
        icon.setBorder(new EmptyBorder(0, 0, 0, DesignConstants.SPACING_MD));
        card.add(icon, BorderLayout.WEST);
        
        // Center: Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(DesignConstants.SURFACE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(DesignConstants.FONT_SMALL);
        descLabel.setForeground(DesignConstants.TEXT_SECONDARY);
        descLabel.setAlignmentX(LEFT_ALIGNMENT);
        descLabel.setPreferredSize(new Dimension(280, 16));
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
                // Brighten icon background on hover
                Color hoverIconBg = new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50);
                icon.setBackgroundColor(hoverIconBg);
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
                // Reset icon background
                icon.setBackgroundColor(iconBgColor);
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
    }
}