package bcu.cmp5332.bookingsystem.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

/**
 * Custom icon components for sidebar navigation
 * Provides professional SVG-style icons drawn with Java2D
 */
public class SidebarIcon extends JPanel {
    
    public enum IconType {
        DASHBOARD, FLIGHTS, CUSTOMERS, BOOKINGS, PROFILE, EXIT
    }
    
    private final IconType type;
    private Color iconColor = Color.WHITE;
    
    public SidebarIcon(IconType type, int size) {
        this.type = type;
        setPreferredSize(new Dimension(size, size));
        setOpaque(false);
    }
    
    public void setIconColor(Color color) {
        this.iconColor = color;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable anti-aliasing for smooth icon rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        g2d.setColor(iconColor);
        
        int width = getWidth();
        int height = getHeight();
        int padding = width / 8;
        
        switch (type) {
            case DASHBOARD:
                drawDashboardIcon(g2d, width, height, padding);
                break;
            case FLIGHTS:
                drawFlightsIcon(g2d, width, height, padding);
                break;
            case CUSTOMERS:
                drawCustomersIcon(g2d, width, height, padding);
                break;
            case BOOKINGS:
                drawBookingsIcon(g2d, width, height, padding);
                break;
            case PROFILE:
                drawProfileIcon(g2d, width, height, padding);
                break;
            case EXIT:
                drawExitIcon(g2d, width, height, padding);
                break;
        }
        
        g2d.dispose();
    }
    
    /* Dashboard Icon - House */
    private void drawDashboardIcon(Graphics2D g2d, int w, int h, int pad) {
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // House roof
        Path2D roof = new Path2D.Float();
        roof.moveTo(w / 2, pad);
        roof.lineTo(w - pad, h / 2);
        roof.lineTo(pad, h / 2);
        roof.closePath();
        g2d.draw(roof);
        
        // House base
        g2d.drawRect(pad + 2, h / 2, w - 2 * pad - 4, h / 2 - pad - 2);
        
        // Door
        int doorW = (w - 2 * pad) / 3;
        int doorH = (h / 2 - pad) / 2;
        g2d.fillRect(w / 2 - doorW / 2, h - pad - doorH - 2, doorW, doorH);
    }
    
    /* Flights Icon - Airplane */
    private void drawFlightsIcon(Graphics2D g2d, int w, int h, int pad) {
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Fuselage (airplane body)
        int centerY = h / 2;
        g2d.drawLine(pad, centerY, w - pad, centerY);
        
        // Wings
        Path2D wings = new Path2D.Float();
        wings.moveTo(w / 3, centerY);
        wings.lineTo(w / 2, pad + 2);
        g2d.draw(wings);
        
        wings = new Path2D.Float();
        wings.moveTo(w / 3, centerY);
        wings.lineTo(w / 2, h - pad - 2);
        g2d.draw(wings);
        
        // Tail wing
        wings = new Path2D.Float();
        wings.moveTo(w - pad - 5, centerY);
        wings.lineTo(w - pad - 2, pad + 5);
        g2d.draw(wings);
        
        // Nose
        g2d.fillOval(pad - 1, centerY - 2, 5, 5);
    }
    
    /* Customers Icon - People */
    private void drawCustomersIcon(Graphics2D g2d, int w, int h, int pad) {
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int personW = w / 3;
        int headR = personW / 3;
        
        // First person (left)
        drawPerson(g2d, w / 3, h / 2, headR, personW);
        
        // Second person (right)
        drawPerson(g2d, 2 * w / 3, h / 2, headR, personW);
    }
    
    private void drawPerson(Graphics2D g2d, int x, int y, int headR, int bodyW) {
        // Head
        g2d.drawOval(x - headR / 2, y - headR - 2, headR, headR);
        
        // Body (shoulders and torso)
        Path2D body = new Path2D.Float();
        body.moveTo(x - bodyW / 2, y + headR - 2);
        body.curveTo(x - bodyW / 2, y, x + bodyW / 2, y, x + bodyW / 2, y + headR - 2);
        g2d.draw(body);
    }
    
    /* Bookings Icon - Ticket */
    private void drawBookingsIcon(Graphics2D g2d, int w, int h, int pad) {
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Ticket outline with notches
        int notchR = 4;
        
        Path2D ticket = new Path2D.Float();
        ticket.moveTo(pad, pad);
        ticket.lineTo(w - pad, pad);
        ticket.lineTo(w - pad, h / 2 - notchR);
        ticket.curveTo(w - pad, h / 2, w - pad - notchR, h / 2, w - pad - notchR, h / 2);
        ticket.curveTo(w - pad - notchR, h / 2, w - pad, h / 2, w - pad, h / 2 + notchR);
        ticket.lineTo(w - pad, h - pad);
        ticket.lineTo(pad, h - pad);
        ticket.lineTo(pad, h / 2 + notchR);
        ticket.curveTo(pad, h / 2, pad + notchR, h / 2, pad + notchR, h / 2);
        ticket.curveTo(pad + notchR, h / 2, pad, h / 2, pad, h / 2 - notchR);
        ticket.closePath();
        g2d.draw(ticket);
        
        // Perforation line
        float[] dash = {3.0f, 3.0f};
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dash, 0.0f));
        g2d.drawLine(w / 2, pad + 4, w / 2, h - pad - 4);
    }
    
    /* Profile Icon - User Circle */
    private void drawProfileIcon(Graphics2D g2d, int w, int h, int pad) {
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int centerX = w / 2;
        int centerY = h / 2;
        int radius = (w - 2 * pad) / 2;
        
        // Circle outline
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Head
        int headR = radius / 3;
        g2d.drawOval(centerX - headR / 2, centerY - radius / 3, headR, headR);
        
        // Shoulders (arc at bottom)
        Path2D shoulders = new Path2D.Float();
        int shoulderW = (int)(radius * 1.4);
        shoulders.moveTo(centerX - shoulderW / 2, centerY + radius);
        shoulders.curveTo(centerX - shoulderW / 2, centerY + radius / 3,
                         centerX + shoulderW / 2, centerY + radius / 3,
                         centerX + shoulderW / 2, centerY + radius);
        g2d.draw(shoulders);
    }
    
    /* Exit Icon - Door with Arrow */
    private void drawExitIcon(Graphics2D g2d, int w, int h, int pad) {
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Door frame
        g2d.drawRect(w / 2 - 2, pad, w / 2 - pad + 2, h - 2 * pad);
        
        // Door knob
        g2d.fillOval(w / 2 + 2, h / 2 - 2, 4, 4);
        
        // Exit arrow (pointing left/out)
        int arrowY = h / 2;
        int arrowX = w / 2 - 10;
        
        // Arrow line
        g2d.drawLine(pad, arrowY, arrowX, arrowY);
        
        // Arrow head
        Path2D arrowHead = new Path2D.Float();
        arrowHead.moveTo(pad, arrowY);
        arrowHead.lineTo(pad + 6, arrowY - 5);
        arrowHead.moveTo(pad, arrowY);
        arrowHead.lineTo(pad + 6, arrowY + 5);
        g2d.draw(arrowHead);
    }
}
