package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.gui.AdminMainWindow;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class LoadGUI implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        AdminMainWindow adminMainWindow = new AdminMainWindow(flightBookingSystem);
        adminMainWindow.setVisible(true);
    }
    
}
