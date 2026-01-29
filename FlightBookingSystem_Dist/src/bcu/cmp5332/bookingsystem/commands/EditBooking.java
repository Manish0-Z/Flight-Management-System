package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class EditBooking implements Command {

    private final int customerId;
    private final int oldFlightId;
    private final int newFlightId;

    public EditBooking(int customerId, int oldFlightId, int newFlightId) {
        this.customerId = customerId;
        this.oldFlightId = oldFlightId;
        this.newFlightId = newFlightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight oldFlight = flightBookingSystem.getFlightByID(oldFlightId);
        Flight newFlight = flightBookingSystem.getFlightByID(newFlightId);

        // Find the booking
        Booking booking = null;
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getId() == oldFlightId) {
                booking = b;
                break;
            }
        }
        if (booking == null) {
            throw new FlightBookingSystemException("No booking found for customer " + customerId + " on flight " + oldFlightId);
        }

        // Cancel old booking
        customer.cancelBookingForFlight(oldFlight);
        oldFlight.removePassenger(customer);

        // Add new booking with same details
        Booking newBooking = new Booking(customer, newFlight, booking.getBookingDate(), booking.getSeatNumber(), booking.getBookingClass(), booking.getSpecialRequests());
        customer.addBooking(newBooking);
        newFlight.addPassenger(customer);

        System.out.println("Booking updated: Customer " + customer.getName() + " changed from Flight " + oldFlight.getFlightNumber() + " to " + newFlight.getFlightNumber());
    }
}