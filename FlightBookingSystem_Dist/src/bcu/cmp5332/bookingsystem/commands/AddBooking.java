package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class AddBooking implements Command {

    private final int customerId;
    private final int flightId;
    private final String seatNumber;
    private final String bookingClass;
    private final String specialRequests;

    public AddBooking(int customerId, int flightId, String seatNumber, String bookingClass, String specialRequests) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.seatNumber = seatNumber;
        this.bookingClass = bookingClass;
        this.specialRequests = specialRequests;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight flight = flightBookingSystem.getFlightByID(flightId);

        // Check for duplicate booking
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getId() == flight.getId()) {
                throw new FlightBookingSystemException(
                        "Customer " + customer.getName() + " is already booked on flight " + flight.getFlightNumber());
            }
        }

        int maxId = 0;
        if (!flightBookingSystem.getBookings().isEmpty()) {
            int lastIndex = flightBookingSystem.getBookings().size() - 1;
            maxId = flightBookingSystem.getBookings().get(lastIndex).getId();
        }

        Booking booking = new Booking(++maxId, customer, flight, flightBookingSystem.getSystemDate(), seatNumber, bookingClass,
                specialRequests);
        customer.addBooking(booking);
        flightBookingSystem.addBooking(booking);
        flight.addPassenger(customer);
        System.out
                .println("Booking created: Customer " + customer.getName() + " on Flight " + flight.getFlightNumber()
                        + " - Seat: " + seatNumber);
    }
}
