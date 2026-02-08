package bcu.cmp5332.bookingsystem.gui;

import java.awt.*;
import javax.swing.*;

class AirlineLogo extends JPanel {
    private final String airlineName = "Flight Management";
    private final String airlineSubtext = "System";

    public AirlineLogo(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // CHANGED: Draw airline name with better typography
        g2d.setColor(DesignConstants.TEXT_PRIMARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(airlineName);
        int textX = (width - textWidth) / 2;
        g2d.drawString(airlineName, textX, height - 22);
        
        // CHANGED: Draw subtext with accent color
        g2d.setColor(DesignConstants.ACCENT);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fm = g2d.getFontMetrics();
        textWidth = fm.stringWidth(airlineSubtext);
        textX = (width - textWidth) / 2;
        g2d.drawString(airlineSubtext, textX, height - 6);

        g2d.dispose();
    }
}