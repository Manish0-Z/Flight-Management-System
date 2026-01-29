package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public interface Command {

    public static final String HELP_MESSAGE = "Commands:\n" +
        "\n" +
        "+--------------------------------------------------+\n" +
        "| Customer Commands:                               |\n" +
        "+--------------------------------------------------+\n" +
        "| listcustomers     print all customers            |\n" +
        "| addcustomer       add a new customer             |\n" +
        "| showcustomer [<id>] show customer details          |\n" +
        "+--------------------------------------------------+\n" +
        "| Flight Commands:                                 |\n" +
        "+--------------------------------------------------+\n" +
        "| listflights       print all flights              |\n" +
        "| addflight         add a new flight               |\n" +
        "| showflight [<id>]   show flight details            |\n" +
        "+--------------------------------------------------+\n" +
        "| Booking Commands:                                |\n" +
        "+--------------------------------------------------+\n" +
        "| addbooking <cid> <fid> add a new booking         |\n" +
        "| cancelbooking [<cid> <fid>] cancel a booking       |\n" +
        "| editbooking [<cid> <oldfid> <newfid>] update a booking         |\n" +
        "+--------------------------------------------------+\n" +
        "| Other Commands:                                  |\n" +
        "+--------------------------------------------------+\n" +
        "| loadgui           loads the GUI version of the app|\n" +
        "| help              prints this help message       |\n" +
        "| exit              exits the program              |\n" +
        "+--------------------------------------------------+\n";

    
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException;
    
}
