package bcu.cmp5332.bookingsystem.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * ModernHeader - A modern, reusable header component for application panels
 * 
 * Features:
 * - Clean, minimal design with subtle elevation
 * - Support for icon, title, and optional subtitle
 * - Gradient or solid color background
 * - Responsive layout that adapts to content
 * - Professional typography and spacing
 * - Optional action buttons or status indicators
 * 
 * @author UI/UX Redesign 2026
 */
public class ModernHeader extends JPanel {
    
    // Header style presets
    public enum HeaderStyle {
        /** Primary style with blue gradient */
        PRIMARY,
        /** Light style with white background */
        LIGHT,
        /** Accent style with gold accent */
        ACCENT,
        /** Success style with green theme */
        SUCCESS,
        /** Minimal flat style */
        MINIMAL
    }
    
    private JLabel iconLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JPanel actionPanel;
    private HeaderStyle style;
    private Component iconComponent;
    
    /**
     * Creates a modern header with title only
     */
    public ModernHeader(String title) {
        this(null, title, null, HeaderStyle.LIGHT);
    }
    
    /**
     * Creates a modern header with icon and title
     */
    public ModernHeader(Component icon, String title) {
        this(icon, title, null, HeaderStyle.LIGHT);
    }
    
    /**
     * Creates a modern header with full customization
     */
    public ModernHeader(Component icon, String title, String subtitle, HeaderStyle style) {
        this.iconComponent = icon;
        this.style = style;
        initialize(title, subtitle);
    }
    
    private void initialize(String title, String subtitle) {
        setLayout(new BorderLayout(DesignConstants.SPACING_MD, 0));
        applyStyle();
        
        // Main content area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Title and subtitle container
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        // Title
        titleLabel = new JLabel(title);
        titleLabel.setFont(DesignConstants.FONT_HEADING);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyTitleColor();
        textPanel.add(titleLabel);
        
        // Subtitle (if provided)
        if (subtitle != null && !subtitle.isEmpty()) {
            textPanel.add(Box.createRigidArea(new Dimension(0, DesignConstants.SPACING_XS)));
            subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setFont(DesignConstants.FONT_BODY);
            subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            applySubtitleColor();
            textPanel.add(subtitleLabel);
        }
        
        contentPanel.add(textPanel);
        
        // Icon and content layout
        JPanel mainPanel = new JPanel(new BorderLayout(DesignConstants.SPACING_MD, 0));
        mainPanel.setOpaque(false);
        
        // Icon (if provided)
        if (iconComponent != null) {
            JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            iconPanel.setOpaque(false);
            iconPanel.add(iconComponent);
            mainPanel.add(iconPanel, BorderLayout.WEST);
        }
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Action panel placeholder (for buttons/status indicators)
        actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, DesignConstants.SPACING_SM, 0));
        actionPanel.setOpaque(false);
        mainPanel.add(actionPanel, BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void applyStyle() {
        switch (style) {
            case PRIMARY:
                // Gradient background from primary dark to primary
                setBackground(DesignConstants.PRIMARY_DARK);
                setBorder(createStyledBorder(DesignConstants.PRIMARY_DARK));
                break;
                
            case LIGHT:
                // Light background with subtle border
                setBackground(new Color(248, 250, 252));
                setBorder(createStyledBorder(new Color(241, 245, 249)));
                break;
                
            case ACCENT:
                // Accent color theme
                setBackground(new Color(254, 252, 232)); // Light amber
                setBorder(createStyledBorder(new Color(254, 243, 199)));
                break;
                
            case SUCCESS:
                // Success color theme
                setBackground(new Color(236, 253, 245)); // Light green
                setBorder(createStyledBorder(new Color(209, 250, 229)));
                break;
                
            case MINIMAL:
                // Minimal flat style
                setBackground(Color.WHITE);
                setBorder(new EmptyBorder(
                    DesignConstants.SPACING_LG,
                    DesignConstants.SPACING_LG,
                    DesignConstants.SPACING_LG,
                    DesignConstants.SPACING_LG
                ));
                break;
        }
        
        setOpaque(true);
    }
    
    private EmptyBorder createStyledBorder(Color bottomColor) {
        // Create padding with shadow effect simulation
        return new EmptyBorder(
            DesignConstants.SPACING_LG,
            DesignConstants.SPACING_LG,
            DesignConstants.SPACING_LG,
            DesignConstants.SPACING_LG
        );
    }
    
    private void applyTitleColor() {
        switch (style) {
            case PRIMARY:
                titleLabel.setForeground(DesignConstants.TEXT_ON_PRIMARY);
                break;
            case ACCENT:
                titleLabel.setForeground(new Color(146, 64, 14)); // Amber-900
                break;
            case SUCCESS:
                titleLabel.setForeground(new Color(6, 95, 70)); // Green-900
                break;
            default:
                titleLabel.setForeground(DesignConstants.TEXT_PRIMARY);
        }
    }
    
    private void applySubtitleColor() {
        if (subtitleLabel == null) return;
        
        switch (style) {
            case PRIMARY:
                subtitleLabel.setForeground(new Color(200, 215, 255, 230));
                break;
            case ACCENT:
                subtitleLabel.setForeground(new Color(180, 83, 9)); // Amber-700
                break;
            case SUCCESS:
                subtitleLabel.setForeground(new Color(21, 128, 61)); // Green-700
                break;
            default:
                subtitleLabel.setForeground(DesignConstants.TEXT_SECONDARY);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Add subtle gradient for certain styles
        if (style == HeaderStyle.PRIMARY) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Gradient from primarydark to primary
            GradientPaint gradient = new GradientPaint(
                0, 0, DesignConstants.PRIMARY_DARK,
                0, getHeight(), new Color(37, 99, 235, 240)
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
        
        // Draw shadow/elevation effect
        drawElevation(g);
    }
    
    private void drawElevation(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Subtle bottom shadow
        int shadowHeight = 3;
        GradientPaint shadow = new GradientPaint(
            0, getHeight() - shadowHeight,
            new Color(0, 0, 0, 15),
            0, getHeight(),
            new Color(0, 0, 0, 0)
        );
        g2.setPaint(shadow);
        g2.fillRect(0, getHeight() - shadowHeight, getWidth(), shadowHeight);
        
        g2.dispose();
    }
    
    // ==================== PUBLIC API ====================
    
    /**
     * Updates the header title
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    /**
     * Gets the current title
     */
    public String getTitle() {
        return titleLabel.getText();
    }
    
    /**
     * Updates the subtitle text
     */
    public void setSubtitle(String subtitle) {
        if (subtitleLabel != null) {
            subtitleLabel.setText(subtitle);
        }
    }
    
    /**
     * Gets the current subtitle
     */
    public String getSubtitle() {
        return subtitleLabel != null ? subtitleLabel.getText() : null;
    }
    
    /**
     * Adds an action button to the right side of the header
     */
    public void addAction(JButton button) {
        actionPanel.add(button);
        revalidate();
        repaint();
    }
    
    /**
     * Adds any component to the action area (right side)
     */
    public void addActionComponent(Component component) {
        actionPanel.add(component);
        revalidate();
        repaint();
    }
    
    /**
     * Removes all action components
     */
    public void clearActions() {
        actionPanel.removeAll();
        revalidate();
        repaint();
    }
    
    /**
     * Changes the header style dynamically
     */
    public void setHeaderStyle(HeaderStyle newStyle) {
        this.style = newStyle;
        applyStyle();
        applyTitleColor();
        applySubtitleColor();
        revalidate();
        repaint();
    }
    

    public void setIcon(Component icon) {
        this.iconComponent = icon;
        removeAll();
        initialize(titleLabel.getText(), subtitleLabel != null ? subtitleLabel.getText() : null);
        revalidate();
        repaint();
    }
}
