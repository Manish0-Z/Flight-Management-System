package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class Help implements Command {

    private boolean isAdmin;

    public Help(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) {
        if (isAdmin) {
            System.out.println(Command.HELP_MESSAGE_ADMIN);
        } else {
            System.out.println(Command.HELP_MESSAGE_CUSTOMER);
        }
    }
}
