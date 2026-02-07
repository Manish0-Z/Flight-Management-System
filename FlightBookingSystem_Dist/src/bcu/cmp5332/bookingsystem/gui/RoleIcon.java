package bcu.cmp5332.bookingsystem.gui;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * Custom icon component for role selection cards
 * Provides professional vector-style icons for Admin and Customer roles
 */
public class RoleIcon extends JPanel {
    
    public enum RoleType {
        ADMIN, CUSTOMER
    }
    
    private final RoleType type;
    private Color iconColor;
    private Color backgroundColor;
    
    public RoleIcon(RoleType type, int size, Color iconColor, Color backgroundColor) {
        this.type = type;
        this.iconColor = iconColor;
        this.backgroundColor = backgroundColor;
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));
        setOpaque(false);
    }
    
    public void setIconColor(Color color) {
        this.iconColor = color;
        repaint();
    }
    
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable anti-aliasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw circular background
        if (backgroundColor != null) {
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, width, height, width / 4, height / 4);
        }
        
        g2d.setColor(iconColor);
        
        switch (type) {
            case ADMIN:
                drawAdminIcon(g2d, width, height);
                break;
            case CUSTOMER:
                drawCustomerIcon(g2d, width, height);
                break;
        }
        
        g2d.dispose();
    }
    
    /**
     * Admin Icon - Shield with lock symbol
     */
    private void drawAdminIcon(Graphics2D g2d, int w, int h) {
        g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int pad = w / 6;
        int centerX = w / 2;
        int centerY = h / 2;
        
        // Shield outline
        Path2D shield = new Path2D.Float();
        shield.moveTo(centerX, pad);
        shield.lineTo(w - pad, pad + h / 6);
        shield.lineTo(w - pad, centerY + pad / 2);
        shield.curveTo(w - pad, h - pad, centerX, h - pad + pad / 2, centerX, h - pad + pad / 2);
        shield.curveTo(centerX, h - pad + pad / 2, pad, h - pad, pad, centerY + pad / 2);
        shield.lineTo(pad, pad + h / 6);
        shield.closePath();
        g2d.draw(shield);
        
        // Lock body (rectangular)
        int lockW = w / 4;
        int lockH = h / 5;
        int lockX = centerX - lockW / 2;
        int lockY = centerY - lockH / 4;
        g2d.fillRoundRect(lockX, lockY, lockW, lockH, 3, 3);
        
        // Lock shackle (arc on top)
        int shackleW = lockW - 4;
        int shackleH = lockH / 2 + 2;
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(centerX - shackleW / 2, lockY - shackleH, shackleW, shackleH * 2, 0, 180);
        
        // Keyhole
        g2d.setColor(backgroundColor != null ? backgroundColor : Color.WHITE);
        g2d.fillOval(centerX - 2, lockY + 3, 4, 4);
    }
    
    /**
     * Customer Icon - User profile with ticket/boarding pass
     */
    private void drawCustomerIcon(Graphics2D g2d, int w, int h) {
        g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int pad = w / 6;
        int centerX = w / 2;
        int centerY = h / 2;
        
        // Head
        int headR = w / 5;
        g2d.drawOval(centerX - headR / 2, pad + 2, headR, headR);
        
        // Body (shoulders)
        Path2D shoulders = new Path2D.Float();
        int shoulderW = (int)(w * 0.5);
        int bodyY = pad + headR + 4;
        shoulders.moveTo(centerX - shoulderW / 2, centerY + pad);
        shoulders.curveTo(centerX - shoulderW / 2, bodyY,
                         centerX + shoulderW / 2, bodyY,
                         centerX + shoulderW / 2, centerY + pad);
        g2d.draw(shoulders);
        
        // Boarding pass/ticket in hand (small rectangle)
        int ticketW = w / 3;
        int ticketH = h / 7;
        int ticketX = centerX + w / 6;
        int ticketY = centerY + pad / 2;
        
        // Ticket outline
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawRoundRect(ticketX, ticketY, ticketW, ticketH, 3, 3);
        
        // Ticket notch
        g2d.fillOval(ticketX - 2, ticketY + ticketH / 2 - 2, 4, 4);
        
        // Ticket lines (details)
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawLine(ticketX + 3, ticketY + 3, ticketX + ticketW - 3, ticketY + 3);
        g2d.drawLine(ticketX + 3, ticketY + ticketH - 3, ticketX + ticketW / 2, ticketY + ticketH - 3);
    }
}
