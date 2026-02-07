package bcu.cmp5332.bookingsystem.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * DesignConstants - Centralized design system for consistent UI styling
 * Contains colors, fonts, spacing, and utility methods for creating modern UI components
 * 
 * @author UI/UX Redesign 2026
 */
public class DesignConstants {
    
    // ==================== COLOR PALETTE ====================
    
    // Primary Colors
    public static final Color PRIMARY_DARK = new Color(30, 58, 138);      // #1e3a8a - Deep Blue
    public static final Color PRIMARY = new Color(37, 99, 235);           // #2563eb - Royal Blue
    public static final Color PRIMARY_LIGHT = new Color(59, 130, 246);    // #3b82f6 - Bright Blue
    public static final Color PRIMARY_LIGHTER = new Color(96, 165, 250);  // #60a5fa - Light Blue
    
    // Accent Colors
    public static final Color ACCENT = new Color(245, 158, 11);           // #f59e0b - Amber Gold
    public static final Color ACCENT_LIGHT = new Color(251, 191, 36);     // #fbbf24 - Light Gold
    public static final Color ACCENT_DARK = new Color(217, 119, 6);       // #d97706 - Dark Gold
    
    // Semantic Colors
    public static final Color SUCCESS = new Color(16, 185, 129);          // #10b981 - Emerald
    public static final Color SUCCESS_LIGHT = new Color(52, 211, 153);    // #34d399
    public static final Color ERROR = new Color(239, 68, 68);             // #ef4444 - Red
    public static final Color ERROR_LIGHT = new Color(248, 113, 113);     // #f87171
    public static final Color WARNING = new Color(251, 146, 60);          // #fb923c - Orange
    public static final Color INFO = new Color(59, 130, 246);             // #3b82f6- Blue
    
    // Neutral Colors
    public static final Color BACKGROUND = new Color(248, 250, 252);      // #f8fafc - Light Gray
    public static final Color SURFACE = Color.WHITE;                      // #ffffff
    public static final Color SURFACE_HOVER = new Color(249, 250, 251);   // #f9fafb
    public static final Color BORDER = new Color(226, 232, 240);          // #e2e8f0
    public static final Color BORDER_DARK = new Color(203, 213, 225);     // #cbd5e1
    
    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(30, 41, 59);       // #1e293b - Dark Slate
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);  // #64748b - Gray
    public static final Color TEXT_DISABLED = new Color(148, 163, 184);   // #94a3b8
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;
    
    // Sidebar Colors
    public static final Color SIDEBAR_BG = PRIMARY_DARK;
    public static final Color SIDEBAR_HOVER = new Color(50, 78, 158);
    public static final Color SIDEBAR_ACTIVE = new Color(60, 98, 178);
    public static final Color SIDEBAR_TEXT = Color.WHITE;
    
    // ==================== TYPOGRAPHY ====================
    
    public static final String FONT_FAMILY = "Segoe UI";
    public static final Font FONT_HEADING_LARGE = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font FONT_HEADING = new Font(FONT_FAMILY, Font.BOLD, 20);
    public static final Font FONT_HEADING_SMALL = new Font(FONT_FAMILY, Font.BOLD, 16);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_SIDEBAR = new Font(FONT_FAMILY, Font.PLAIN, 15);
    
    // ==================== SPACING ====================
    
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 16;
    public static final int SPACING_LG = 24;
    public static final int SPACING_XL = 32;
    public static final int SPACING_XXL = 48;
    
    // ==================== DIMENSIONS ====================
    
    public static final int BORDER_RADIUS = 8;
    public static final int BORDER_RADIUS_SMALL = 6;
    public static final int BORDER_RADIUS_LARGE = 12;
    
    public static final int BUTTON_HEIGHT = 40;
    public static final int BUTTON_HEIGHT_SMALL = 32;
    public static final int INPUT_HEIGHT = 36;
    
    public static final int SIDEBAR_WIDTH = 260;
    public static final int SIDEBAR_BUTTON_HEIGHT = 48;
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Creates a styled primary button with modern appearance
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(TEXT_ON_PRIMARY);
        button.setBackground(PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, BUTTON_HEIGHT));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a styled secondary button
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(SURFACE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_DARK, 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, BUTTON_HEIGHT));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SURFACE_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SURFACE);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a styled accent button
     */
    public static JButton createAccentButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, BUTTON_HEIGHT));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a styled danger button (for delete, cancel actions)
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(ERROR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, BUTTON_HEIGHT));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ERROR_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ERROR);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a modern styled text field
     */
    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(SURFACE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, INPUT_HEIGHT));
        
        // Add focus effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_LIGHT, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Creates a modern styled password field
     */
    public static JPasswordField createStyledPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(SURFACE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, INPUT_HEIGHT));
        
        // Add focus effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_LIGHT, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Creates a card-style panel with shadow effect
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER_RADIUS, BORDER),
            BorderFactory.createEmptyBorder(SPACING_MD, SPACING_MD, SPACING_MD, SPACING_MD)
        ));
        return panel;
    }
    
    /**
     * Creates a section header label
     */
    public static JLabel createSectionHeader(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADING_SMALL);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Creates a body label
     */
    public static JLabel createBodyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
    
    /**
     * Custom rounded border class
     */
    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        
        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }
        
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = 8;
            insets.top = insets.bottom = 4;
            return insets;
        }
    }
    
    /**
     * Applies modern table styling
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY_LIGHTER);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setBackground(SURFACE);
        table.setForeground(TEXT_PRIMARY);
        
        // Style table header
        table.getTableHeader().setFont(FONT_BODY_BOLD);
        table.getTableHeader().setBackground(BACKGROUND);
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_DARK));
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 45));
    }
    
    /**
     * Creates padding insets using the spacing system
     */
    public static Insets createPadding(int top, int right, int bottom, int left) {
        return new Insets(top, left, bottom, right);
    }
    
    /**
     * Creates uniform padding
     */
    public static Insets createPadding(int size) {
        return new Insets(size, size, size, size);
    }
}
