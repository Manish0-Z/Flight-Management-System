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
    // Dark theme accents: cool blue for focus and primary actions.
    public static final Color PRIMARY_DARK = new Color(10, 12, 16);       // #0a0c10 - Near-black
    public static final Color PRIMARY = new Color(59, 130, 246);          // #3b82f6 - Blue 500
    public static final Color PRIMARY_LIGHT = new Color(96, 165, 250);    // #60a5fa - Blue 400
    public static final Color PRIMARY_LIGHTER = new Color(147, 197, 253); // #93c5fd - Blue 300
    
    // Accent Colors
    // Subtle cyan/teal accents for highlights and secondary emphasis.
    public static final Color ACCENT = new Color(34, 211, 238);           // #22d3ee - Cyan 400
    public static final Color ACCENT_LIGHT = new Color(103, 232, 249);    // #67e8f9 - Cyan 300
    public static final Color ACCENT_DARK = new Color(14, 116, 144);      // #0e7490 - Cyan 700
    
    // Semantic Colors
    // Slightly desaturated to avoid over-bright alerts on dark surfaces.
    public static final Color SUCCESS = new Color(34, 197, 94);           // #22c55e - Green 500
    public static final Color SUCCESS_LIGHT = new Color(74, 222, 128);    // #4ade80
    public static final Color ERROR = new Color(248, 113, 113);           // #f87171 - Soft red
    public static final Color ERROR_LIGHT = new Color(252, 165, 165);     // #fca5a5
    public static final Color WARNING = new Color(250, 204, 21);          // #facc15 - Amber 400
    public static final Color INFO = new Color(96, 165, 250);             // #60a5fa - Blue 400
    
    // Neutral Colors
    // Dark charcoal base with subtle surface lift for panels and cards.
    public static final Color BACKGROUND = new Color(15, 17, 21);         // #0f1115 - Charcoal
    public static final Color SURFACE = new Color(23, 26, 33);            // #171a21 - Surface
    public static final Color SURFACE_HOVER = new Color(28, 32, 40);      // #1c2028
    public static final Color BORDER = new Color(38, 43, 54);             // #262b36 - Subtle border
    public static final Color BORDER_DARK = new Color(52, 58, 70);        // #343a46
    
    // Text Colors
    // Off-white text to reduce glare on dark backgrounds.
    public static final Color TEXT_PRIMARY = new Color(226, 232, 240);    // #e2e8f0
    public static final Color TEXT_SECONDARY = new Color(148, 163, 184);  // #94a3b8
    public static final Color TEXT_DISABLED = new Color(100, 116, 139);   // #64748b
    public static final Color TEXT_ON_PRIMARY = new Color(226, 232, 240); // #e2e8f0
    
    // Sidebar Colors
    public static final Color SIDEBAR_BG = new Color(12, 14, 18);         // #0c0e12
    public static final Color SIDEBAR_HOVER = new Color(20, 24, 31);      // #14181f
    public static final Color SIDEBAR_ACTIVE = new Color(28, 33, 42);     // #1c212a
    public static final Color SIDEBAR_TEXT = TEXT_PRIMARY;
    
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
        button.setBorder(new RoundedBorder(BORDER_RADIUS_SMALL, BORDER));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
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
            new RoundedBorder(BORDER_RADIUS_SMALL, BORDER),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
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
        button.setForeground(TEXT_ON_PRIMARY);
        button.setBackground(ACCENT);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(BORDER_RADIUS_SMALL, BORDER));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
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
        button.setForeground(TEXT_ON_PRIMARY);
        button.setBackground(ERROR);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(BORDER_RADIUS_SMALL, BORDER));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
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
                    BorderFactory.createLineBorder(PRIMARY, 2),
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
                    BorderFactory.createLineBorder(PRIMARY, 2),
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
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 90));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setBackground(SURFACE);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(BORDER);
        
        // Style table header
        table.getTableHeader().setFont(FONT_BODY_BOLD);
        table.getTableHeader().setBackground(SURFACE);
        table.getTableHeader().setForeground(TEXT_SECONDARY);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_DARK));
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 45));
    }

    /**
     * Applies dark theme styling to scroll panes.
     */
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBackground(BACKGROUND);
        scrollPane.getViewport().setBackground(SURFACE);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER, 1));
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

    /**
     * Applies dark theme defaults to Swing UIManager for consistent rendering.
     */
    public static void applyDarkThemeDefaults() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Label.font", FONT_BODY);
        UIManager.put("Label.disabledForeground", TEXT_DISABLED);

        // Force basic button UI so backgrounds are respected on Windows LAF.
        UIManager.put("ButtonUI", "javax.swing.plaf.basic.BasicButtonUI");
        UIManager.put("ToggleButtonUI", "javax.swing.plaf.basic.BasicButtonUI");

        UIManager.put("Button.background", SURFACE);
        UIManager.put("Button.foreground", TEXT_PRIMARY);
        UIManager.put("Button.font", FONT_BUTTON);
        UIManager.put("Button.disabledText", TEXT_DISABLED);

        UIManager.put("TextField.background", SURFACE);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("TextField.inactiveForeground", TEXT_DISABLED);
        UIManager.put("PasswordField.background", SURFACE);
        UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground", TEXT_PRIMARY);
        UIManager.put("PasswordField.inactiveForeground", TEXT_DISABLED);
        UIManager.put("TextArea.background", SURFACE);
        UIManager.put("TextArea.foreground", TEXT_PRIMARY);
        UIManager.put("TextArea.caretForeground", TEXT_PRIMARY);

        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.selectionBackground", new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 90));
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.background", SURFACE);
        UIManager.put("TableHeader.foreground", TEXT_SECONDARY);

        UIManager.put("ScrollPane.background", BACKGROUND);
        UIManager.put("Viewport.background", BACKGROUND);

        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("OptionPane.foreground", TEXT_PRIMARY);
        UIManager.put("OptionPane.buttonFont", FONT_BUTTON);
        UIManager.put("OptionPane.buttonForeground", TEXT_PRIMARY);
        UIManager.put("OptionPane.buttonBackground", SURFACE);
        UIManager.put("Panel.foreground", TEXT_PRIMARY);
    }
}
