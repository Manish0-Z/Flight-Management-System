package bcu.cmp5332.bookingsystem.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class BookingDataManager implements DataManager {

    public final String RESOURCE = "./resources/data/bookings.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] properties = line.split(SEPARATOR, -1);
                try {
                    int bookingId = Integer.parseInt(properties[0]);
                    int customerId = Integer.parseInt(properties[1]);
                    int flightId = Integer.parseInt(properties[2]);
                    LocalDate bookingDate = LocalDate.parse(properties[3]);
                    String seatNumber = properties.length > 4 ? properties[4] : "";
                    String bookingClass = properties.length > 5 ? properties[5] : "Economy";
                    String specialRequests = properties.length > 6 ? properties[6] : "";

                    Customer customer = fbs.getCustomerByID(customerId);
                    Flight flight = fbs.getFlightByID(flightId);

                    Booking booking = new Booking(bookingId, customer, flight, bookingDate, seatNumber, bookingClass,
                            specialRequests);
                    customer.addBooking(booking);
                    fbs.addBooking(booking);
                    flight.addPassenger(customer);
                } catch (NumberFormatException | FlightBookingSystemException ex) {
                    throw new FlightBookingSystemException("Unable to parse booking on line " + line_idx
                            + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Customer customer : fbs.getCustomers()) {
                for (Booking booking : customer.getBookings()) {
                    out.print(booking.getId() + SEPARATOR);
                    out.print(booking.getCustomer().getId() + SEPARATOR);
                    out.print(booking.getFlight().getId() + SEPARATOR);
                    out.print(booking.getBookingDate() + SEPARATOR);
                    out.print(booking.getSeatNumber() + SEPARATOR);
                    out.print(booking.getBookingClass() + SEPARATOR);
                    out.print(booking.getSpecialRequests() + SEPARATOR);
                    out.println();
                }
            }
        }
    }

}
