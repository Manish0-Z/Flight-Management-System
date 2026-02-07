package bcu.cmp5332.bookingsystem.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

class AirplaneIcon extends JPanel {
    private final Color planeColor = DesignConstants.ACCENT;

    public AirplaneIcon(int width, int height) {
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

        // Draw airplane body (fuselage)
        g2d.setColor(planeColor);
        g2d.fillRoundRect(width / 4, height / 2 - 8, width / 2, 16, 8, 8);

        // Draw wings
        g2d.fillRoundRect(width / 3, height / 2 - 20, width / 3, 8, 4, 4);
        g2d.fillRoundRect(width / 3, height / 2 + 12, width / 3, 8, 4, 4);

        // Draw tail
        g2d.fillRoundRect(width - width / 4 - 6, height / 2 - 25, 8, 20, 4, 4);

        // Draw windows
        g2d.setColor(DesignConstants.TEXT_ON_PRIMARY);
        for (int i = 0; i < 4; i++) {
            g2d.fillOval(width / 3 + 15 + i * 12, height / 2 - 4, 6, 8);
        }

        g2d.dispose();
    }
}