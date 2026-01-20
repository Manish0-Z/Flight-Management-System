package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.Help;
import bcu.cmp5332.bookingsystem.commands.ListCustomers;
import bcu.cmp5332.bookingsystem.commands.ListFlights;
import bcu.cmp5332.bookingsystem.commands.LoadGUI;
import bcu.cmp5332.bookingsystem.commands.ShowCustomer;
import bcu.cmp5332.bookingsystem.commands.ShowFlight;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CommandParser {

    public static Command parse(String line) throws IOException, FlightBookingSystemException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String[] parts = line.split(" ", 3);
            String cmd = parts[0];

            switch (cmd) {
                case "addflight": {
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
                    return new Help();
                }
                case "showflight": {
                    if (parts.length != 2) {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                    int flightId = Integer.parseInt(parts[1]);
                    return new ShowFlight(flightId);
                }
                case "showcustomer": {
                    if (parts.length != 2) {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                    int customerId = Integer.parseInt(parts[1]);
                    return new ShowCustomer(customerId);
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
                    if (parts.length != 3) {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                    break;
                }
                case "cancelbooking": {
                    if (parts.length != 3) {
                        throw new FlightBookingSystemException("Invalid command.");
                    }
                    int id1 = Integer.parseInt(parts[1]);
                    int id2 = Integer.parseInt(parts[2]);
                    return new CancelBooking(id1, id2);
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
