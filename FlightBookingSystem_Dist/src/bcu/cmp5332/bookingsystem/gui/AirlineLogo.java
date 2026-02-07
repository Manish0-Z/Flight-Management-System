package bcu.cmp5332.bookingsystem.gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
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

        // CHANGED: Draw modern logo background with gradient
        GradientPaint gradient = new GradientPaint(
            0, 0, DesignConstants.PRIMARY_DARK,
            width, height, DesignConstants.PRIMARY
        );
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, 15, 15));

        // CHANGED: Draw border with accent color
        g2d.setColor(DesignConstants.ACCENT);
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(new RoundRectangle2D.Float(1, 1, width-2, height-2, 13, 13));

        // CHANGED: Draw improved airplane silhouette
        g2d.setColor(DesignConstants.TEXT_ON_PRIMARY);
        // Fuselage
        g2d.fillRoundRect(width/4 + 10, height/3 + 5, width/2 - 20, height/5, 6, 6);
        // Wings
        int[] wingsX = {width/5, width*4/5, width/5};
        int[] wingsY = {height/2 + 5, height/2 + 5, height/2 + 15};
        g2d.fillPolygon(wingsX, wingsY, 3);
        // Tail
        int[] tailX = {width*3/4, width*3/4 + 10, width*3/4};
        int[] tailY = {height/3, height/3, height/2};
        g2d.fillPolygon(tailX, tailY, 3);
        // Cockpit
        g2d.setColor(DesignConstants.PRIMARY_LIGHT);
        g2d.fillOval(width/4 + 12, height/3 + 7, 8, 8);

        // CHANGED: Draw airline name with better typography
        g2d.setColor(DesignConstants.TEXT_ON_PRIMARY);
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