package bcu.cmp5332.bookingsystem.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.User;

public class RoleSelectionWindow extends JFrame {
    private FlightBookingSystem fbs;

    public RoleSelectionWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    private void initialize() {
        setTitle("Role Selection");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Access the system as:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton adminButton = new JButton("Admin");
        JButton customerButton = new JButton("Customer");

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog("admin");
            }
        });

        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog("customer");
            }
        });

        buttonPanel.add(adminButton);
        buttonPanel.add(customerButton);

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void showLoginDialog(String role) {
        LoginDialog loginDialog = new LoginDialog(this, fbs, role);
        loginDialog.setVisible(true);

        if (loginDialog.isLoggedIn()) {
            User loggedInUser = loginDialog.getLoggedInUser();
            if ("admin".equals(role)) {
                new AdminMainWindow(fbs).setVisible(true);
            } else if ("customer".equals(role)) {
                new CustomerMainWindow(fbs, loggedInUser).setVisible(true);
            }
            dispose();
        }
    }
}