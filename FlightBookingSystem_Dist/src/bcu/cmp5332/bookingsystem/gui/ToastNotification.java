package bcu.cmp5332.bookingsystem.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

// Toast Notification Class
public class ToastNotification extends JWindow {
    private static final int TOAST_WIDTH = 350;
    private static final int TOAST_HEIGHT = 80;
    private static final int DISPLAY_TIME = 3000; // 3 seconds

    public enum ToastType {
        SUCCESS, ERROR, INFO, WARNING
    }

    public ToastNotification(JFrame parent, String message, ToastType type) {
        setSize(TOAST_WIDTH, TOAST_HEIGHT);
        setAlwaysOnTop(true);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Set colors based on type
        Color bgColor, textColor;
        String icon;
        switch (type) {
            case SUCCESS:
                bgColor = DesignConstants.SUCCESS;
                textColor = DesignConstants.TEXT_ON_PRIMARY;
                icon = "✓";
                break;
            case ERROR:
                bgColor = DesignConstants.ERROR;
                textColor = DesignConstants.TEXT_ON_PRIMARY;
                icon = "✗";
                break;
            case WARNING:
                bgColor = DesignConstants.WARNING;
                textColor = DesignConstants.TEXT_ON_PRIMARY;
                icon = "⚠";
                break;
            default: // INFO
                bgColor = DesignConstants.INFO;
                textColor = DesignConstants.TEXT_ON_PRIMARY;
                icon = "ℹ";
                break;
        }

        panel.setBackground(bgColor);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(textColor);

        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageArea.setForeground(textColor);
        messageArea.setBackground(bgColor);
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setOpaque(false);

        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(messageArea, BorderLayout.CENTER);

        add(panel);

        // Position the toast
        if (parent != null) {
            Point location = parent.getLocationOnScreen();
            setLocation(location.x + parent.getWidth() / 2 - TOAST_WIDTH / 2,
                       location.y + parent.getHeight() / 2 - TOAST_HEIGHT / 2);
        } else {
            setLocationRelativeTo(null);
        }

        // Auto-hide after display time
        Timer timer = new Timer(DISPLAY_TIME, e -> setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }

    public static void showToast(JFrame parent, String message, ToastType type) {
        ToastNotification toast = new ToastNotification(parent, message, type);
        toast.setVisible(true);
    }
}