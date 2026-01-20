package bcu.cmp5332.bookingsystem.gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

class AirlineLogo extends JPanel {
    private final String airlineName = "Flight Management System";

    public AirlineLogo(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw airline logo background
        g2d.setColor(new Color(30, 58, 138)); // Airline blue
        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, 15, 15));

        // Draw border
        g2d.setColor(new Color(255, 193, 7)); // Gold border
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(new RoundRectangle2D.Float(1, 1, width-2, height-2, 13, 13));

        // Draw airplane silhouette
        g2d.setColor(Color.WHITE);
        // Fuselage
        g2d.fillRoundRect(width/4, height/3, width/2, height/4, 5, 5);
        // Wings
        g2d.fillRoundRect(width/6, height/2, width*2/3, height/8, 3, 3);
        // Tail
        g2d.fillRoundRect(width- width/5, height/4, width/8, height/3, 3, 3);

        // Draw airline name
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(airlineName);
        int textX = (width - textWidth) / 2;
        int textY = height - 10;
        g2d.drawString(airlineName, textX, textY);

        g2d.dispose();
    }
}