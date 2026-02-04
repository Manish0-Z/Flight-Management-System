package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.EditBooking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.Help;
import bcu.cmp5332.bookingsystem.commands.ListFlights;
import bcu.cmp5332.bookingsystem.commands.ListCustomers;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.commands.LoadGUI;
import bcu.cmp5332.bookingsystem.commands.ShowCustomer;
import bcu.cmp5332.bookingsystem.commands.ShowFlight;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CommandParser {

    public static Command parse(String line, FlightBookingSystem fbs, boolean isAdmin) throws IOException, FlightBookingSystemException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String[] parts = line.split(" ", 3);
            String cmd = parts[0];

            switch (cmd) {
                case "addflight": {
                    if (!isAdmin) {
                        throw new FlightBookingSystemException("This command is only available for admins.");
                    }
                    System.out.print("Flight Number: ");
                    String flighNumber = reader.readLine();
                    System.out.print("Origin: ");
                    String origin = reader.readLine();
                    System.out.print("Destination: ");
                    String destination = reader.readLine();
                    LocalDate departureDate = parseDateWithAttempts(reader);
                    return new AddFlight(flighNumber, origin, destination, departureDate);
                }
                case "addcustomer": {
                    if (!isAdmin) {
                        throw new FlightBookingSystemException("This command is only available for admins.");
                    }
                    System.out.print("Name: ");
                    String name = reader.readLine();
                    System.out.print("Phone: ");
                    String phone = reader.readLine();
                    System.out.print("Email: ");
                    String email = reader.readLine();
                    System.out.print("Address: ");
                    String address = reader.readLine();
                    return new AddCustomer(name, phone, email, address);
                }
                case "loadgui": {
                    return new LoadGUI();
                }
                case "listflights": {
                    if (parts.length != 1) {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                    return new ListFlights();
                }
                case "listcustomers": {
                    if (parts.length != 1) {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                    return new ListCustomers();
                }
                case "help": {
                    if (parts.length != 1) {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                    return new Help(isAdmin);
                }
                case "showflight": {
                    if (parts.length == 1) {
                        System.out.println("Available flights:");
                        for (Flight f : fbs.getFlights()) {
                            System.out.println(f.getId() + " - " + f.getFlightNumber() + " (" + f.getOrigin() + " to " + f.getDestination() + ")");
                        }
                        System.out.print("Enter flight ID: ");
                        String idStr = reader.readLine();
                        int flightId = Integer.parseInt(idStr);
                        return new ShowFlight(flightId);
                    } else if (parts.length == 2) {
                        int flightId = Integer.parseInt(parts[1]);
                        return new ShowFlight(flightId);
                    } else {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                }
                case "showcustomer": {
                    if (parts.length == 1) {
                        System.out.println("Available customers:");
                        for (Customer c : fbs.getCustomers()) {
                            System.out.println(c.getId() + " - " + c.getName());
                        }
                        System.out.print("Enter customer ID: ");
                        String idStr = reader.readLine();
                        int customerId = Integer.parseInt(idStr);
                        return new ShowCustomer(customerId);
                    } else if (parts.length == 2) {
                        int customerId = Integer.parseInt(parts[1]);
                        return new ShowCustomer(customerId);
                    } else {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                }
                case "addbooking": {
                    if (parts.length != 3) {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                    int id1 = Integer.parseInt(parts[1]);
                    int id2 = Integer.parseInt(parts[2]);
                    System.out.print("Seat Number: ");
                    String seatNumber = reader.readLine();
                    System.out.print("Booking Class (Economy/Premium Economy/Business/First Class): ");
                    String bookingClass = reader.readLine();
                    System.out.print("Special Requests: ");
                    String specialRequests = reader.readLine();
                    return new AddBooking(id1, id2, seatNumber, bookingClass, specialRequests);
                }
                case "editbooking": {
                    if (parts.length == 1) {
                        System.out.println("Available customers with bookings:");
                        for (Customer c : fbs.getCustomers()) {
                            if (!c.getBookings().isEmpty()) {
                                System.out.println(c.getId() + " - " + c.getName() + " (has " + c.getBookings().size() + " booking(s))");
                            }
                        }
                        System.out.print("Enter customer ID: ");
                        String cidStr = reader.readLine();
                        int cid = Integer.parseInt(cidStr);
                        Customer customer = fbs.getCustomerByID(cid);
                        System.out.println("Current bookings for " + customer.getName() + ":");
                        for (Booking b : customer.getBookings()) {
                            System.out.println("Flight " + b.getFlight().getId() + " - " + b.getFlight().getFlightNumber() + " (" + b.getFlight().getOrigin() + " to " + b.getFlight().getDestination() + ")");
                        }
                        System.out.print("Enter current flight ID: ");
                        String oldFidStr = reader.readLine();
                        int oldFid = Integer.parseInt(oldFidStr);
                        System.out.println("Available flights:");
                        for (Flight f : fbs.getFlights()) {
                            System.out.println(f.getId() + " - " + f.getFlightNumber() + " (" + f.getOrigin() + " to " + f.getDestination() + ")");
                        }
                        System.out.print("Enter new flight ID: ");
                        String newFidStr = reader.readLine();
                        int newFid = Integer.parseInt(newFidStr);
                        return new EditBooking(cid, oldFid, newFid);
                    } else if (parts.length == 4) {
                        int cid = Integer.parseInt(parts[1]);
                        int oldFid = Integer.parseInt(parts[2]);
                        int newFid = Integer.parseInt(parts[3]);
                        return new EditBooking(cid, oldFid, newFid);
                    } else {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                }
                case "cancelbooking": {
                    if (parts.length == 1) {
                        System.out.println("Available customers with bookings:");
                        for (Customer c : fbs.getCustomers()) {
                            if (!c.getBookings().isEmpty()) {
                                System.out.println(c.getId() + " - " + c.getName() + " (has " + c.getBookings().size() + " booking(s))");
                            }
                        }
                        System.out.print("Enter customer ID: ");
                        String cidStr = reader.readLine();
                        int cid = Integer.parseInt(cidStr);
                        Customer customer = fbs.getCustomerByID(cid);
                        System.out.println("Bookings for " + customer.getName() + ":");
                        for (Booking b : customer.getBookings()) {
                            System.out.println("Flight " + b.getFlight().getId() + " - " + b.getFlight().getFlightNumber() + " (" + b.getFlight().getOrigin() + " to " + b.getFlight().getDestination() + ")");
                        }
                        System.out.print("Enter flight ID to cancel: ");
                        String fidStr = reader.readLine();
                        int fid = Integer.parseInt(fidStr);
                        return new CancelBooking(cid, fid);
                    } else if (parts.length == 3) {
                        int id1 = Integer.parseInt(parts[1]);
                        int id2 = Integer.parseInt(parts[2]);
                        return new CancelBooking(id1, id2);
                    } else {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                }
                default: {
                    throw new FlightBookingSystemException("Invalid command.");
                }
            }
        } catch (NumberFormatException ex) {

        }

        throw new FlightBookingSystemException("Invalid command.");
    }

    private static LocalDate parseDateWithAttempts(BufferedReader br, int attempts)
            throws IOException, FlightBookingSystemException {
        if (attempts < 1) {
            throw new IllegalArgumentException("Number of attempts should be higher that 0");
        }
        while (attempts > 0) {
            attempts--;
            System.out.print("Departure Date (\"YYYY-MM-DD\" format): ");
            try {
                LocalDate departureDate = LocalDate.parse(br.readLine());
                return departureDate;
            } catch (DateTimeParseException dtpe) {
                System.out.println("Date must be in YYYY-MM-DD format. " + attempts + " attempts remaining...");
            }
        }

        throw new FlightBookingSystemException("Incorrect departure date provided. Cannot create flight.");
    }

    private static LocalDate parseDateWithAttempts(BufferedReader br) throws IOException, FlightBookingSystemException {
        return parseDateWithAttempts(br, 3);
    }
}
