package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.gui.RoleSelectionWindow;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class LoadGUI implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        RoleSelectionWindow roleSelectionWindow = new RoleSelectionWindow(flightBookingSystem);
        roleSelectionWindow.setVisible(true);
    }
    
}
