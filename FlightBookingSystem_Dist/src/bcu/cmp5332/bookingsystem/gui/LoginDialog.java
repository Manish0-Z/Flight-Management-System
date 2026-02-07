package bcu.cmp5332.bookingsystem.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.User;


public class LoginDialog extends JDialog {
    private FlightBookingSystem fbs;
    private String selectedRole;
    private boolean loggedIn = false;
    private User loggedInUser;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField usernameField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField emailFieldLogin;
    private JPasswordField passwordField;
    private JPasswordField passwordFieldLogin;
    private JPasswordField confirmPasswordField;
    private JButton loginButton;
    private JButton signupButton;

    public LoginDialog(Frame parent, FlightBookingSystem fbs, String selectedRole) {
        super(parent, "Login - " + selectedRole, true);
        this.fbs = fbs;
        this.selectedRole = selectedRole;
        initialize();
    }

    private void initialize() {
        setSize(400, 450);
        setLocationRelativeTo(getParent());
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        if ("customer".equals(selectedRole)) {
            createChoicePanel();
            createSignupPanel();
            createLoginPanel();
            cardLayout.show(mainPanel, "choice");
        } else {
            createLoginPanel();
            cardLayout.show(mainPanel, "login");
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void createChoicePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("Welcome to Flight Booking System"), gbc);

        gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(new JLabel("Please choose an option:"), gbc);

        gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        panel.add(loginBtn, gbc);

        gbc.gridx = 1;
        JButton signupBtn = new JButton("Sign Up");
        signupBtn.addActionListener(e -> cardLayout.show(mainPanel, "signup"));
        panel.add(signupBtn, gbc);

        mainPanel.add(panel, "choice");
    }

    private void createSignupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(15);
        setPlaceholder(fullNameField, "Enter your full name");
        panel.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        setPlaceholder(emailField, "Enter your email address");
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        setPlaceholder(phoneField, "Enter your phone number");
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(15);
        setPlaceholder(addressField, "Enter your address");
        panel.add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Create Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        setPlaceholder(passwordField, "Create a password");
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        setPlaceholder(confirmPasswordField, "Confirm your password");
        panel.add(confirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        signupButton = new JButton("Signup");
        signupButton.addActionListener(e -> signup());
        panel.add(signupButton, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JButton backFromSignupBtn = new JButton("Back");
        backFromSignupBtn.addActionListener(e -> cardLayout.show(mainPanel, "choice"));
        panel.add(backFromSignupBtn, gbc);

        mainPanel.add(panel, "signup");
    }

    private void createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        if ("customer".equals(selectedRole)) {
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            emailFieldLogin = new JTextField(15);
            panel.add(emailFieldLogin, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Password:"), gbc);
            gbc.gridx = 1;
            passwordFieldLogin = new JPasswordField(15);
            panel.add(passwordFieldLogin, gbc);
        } else {
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Username:"), gbc);
            gbc.gridx = 1;
            usernameField = new JTextField(15);
            panel.add(usernameField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Password:"), gbc);
            gbc.gridx = 1;
            passwordField = new JPasswordField(15);
            panel.add(passwordField, gbc);
        }

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        panel.add(loginButton, gbc);

        if ("customer".equals(selectedRole)) {
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            JButton backFromLoginBtn = new JButton("Back");
            backFromLoginBtn.addActionListener(e -> cardLayout.show(mainPanel, "choice"));
            panel.add(backFromLoginBtn, gbc);
        }

        mainPanel.add(panel, "login");
    }

    private void login() {
        String username = "customer".equals(selectedRole) ? emailFieldLogin.getText().trim() : usernameField.getText().trim();
        String password = "customer".equals(selectedRole) ? new String(passwordFieldLogin.getPassword()) : new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = fbs.getUser(username);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "User does not exist. Please sign up first.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            if ("customer".equals(selectedRole)) {
                cardLayout.show(mainPanel, "choice");
            }
        } else if (!user.getPassword().equals(password)) {
            JOptionPane.showMessageDialog(this, "Incorrect password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        } else if (!user.getRole().equals(selectedRole)) {
            JOptionPane.showMessageDialog(this, "Invalid role for this user.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            loggedInUser = user;
            loggedIn = true;
            dispose();
        }
    }

    private void signup() {
        String password = getActualPassword(passwordField, "Create a password");
        String confirmPassword = getActualPassword(confirmPasswordField, "Confirm your password");

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("customer".equals(selectedRole)) {
            String email = getActualText(emailField, "Enter your email address");
            String fullName = getActualText(fullNameField, "Enter your full name");
            String phone = getActualText(phoneField, "Enter your phone number");
            String address = getActualText(addressField, "Enter your address");

            if (email.isEmpty() || fullName.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate email format
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address (e.g., example@gmail.com).", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                User newUser = new User(email, password, selectedRole, fullName);
                fbs.addUser(newUser);
                
                // Also create a customer record for the new user
                int maxId = 0;
                if (!fbs.getCustomers().isEmpty()) {
                    int lastIndex = fbs.getCustomers().size() - 1;
                    maxId = fbs.getCustomers().get(lastIndex).getId();
                }
                Customer newCustomer = new Customer(++maxId, fullName, phone, email, address);
                fbs.addCustomer(newCustomer);
                
                JOptionPane.showMessageDialog(this, "Signup successful! Please login.", "Signup Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(mainPanel, "login");
                setTitle("Login - " + selectedRole);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Signup Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Admin signup not allowed
            JOptionPane.showMessageDialog(this, "Signup not allowed for admins.", "Access Denied", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void setPlaceholder(JPasswordField field, String placeholder) {
        field.setEchoChar((char) 0); // Show text
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('*'); // Hide text
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(field.getPassword()).isEmpty()) {
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private String getActualText(JTextField field, String placeholder) {
        String text = field.getText().trim();
        return text.equals(placeholder) ? "" : text;
    }

    private String getActualPassword(JPasswordField field, String placeholder) {
        String password = String.valueOf(field.getPassword());
        return password.equals(placeholder) ? "" : password;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}