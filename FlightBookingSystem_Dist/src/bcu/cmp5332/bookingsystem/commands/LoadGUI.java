package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.gui.LoginDialog;
import bcu.cmp5332.bookingsystem.gui.MainWindow;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.JOptionPane;

public class LoadGUI implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Show role selection dialog
        Object[] options = {"Admin", "Customer"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "Select your role:",
            "Flight Booking System",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == JOptionPane.CLOSED_OPTION) {
            return; // User closed the dialog
        }

        String selectedRole = (choice == JOptionPane.YES_OPTION) ? "admin" : "customer";

        // Show login dialog
        LoginDialog loginDialog = new LoginDialog(null, flightBookingSystem, selectedRole);
        loginDialog.setVisible(true);

        if (loginDialog.isLoggedIn()) {
            boolean isAdmin = loginDialog.getLoggedInUser().isAdmin();
            MainWindow mainWindow = new MainWindow(flightBookingSystem, isAdmin);
            mainWindow.setVisible(true);
        }
    }
    
}
