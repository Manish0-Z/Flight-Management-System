package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

public class FlightBookingSystem {

    private final LocalDate systemDate = LocalDate.parse("2024-11-11");

    private final Map<Integer, Customer> customers = new TreeMap<>();
    private final Map<Integer, Flight> flights = new TreeMap<>();
    private final Map<Integer, Booking> bookings = new TreeMap<>();
    private final Map<String, User> users = new HashMap<>();

    public LocalDate getSystemDate() {
        return systemDate;
    }

    public List<Flight> getFlights() {
        List<Flight> out = new ArrayList<>(flights.values());
        return Collections.unmodifiableList(out);
    }

    public List<Customer> getCustomers() {
        List<Customer> out = new ArrayList<>(customers.values());
        return Collections.unmodifiableList(out);
    }

    public List<Booking> getBookings() {
        List<Booking> out = new ArrayList<>(bookings.values());
        return Collections.unmodifiableList(out);
    }

    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        if (!flights.containsKey(id)) {
            throw new FlightBookingSystemException("There is no flight with that ID.");
        }
        return flights.get(id);
    }

    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        if (!customers.containsKey(id)) {
            throw new FlightBookingSystemException("There is no customer with that ID.");
        }
        return customers.get(id);
    }

    public Customer getCustomerByEmail(String email) throws FlightBookingSystemException {
        for (Customer customer : customers.values()) {
            if (customer.getEmail().equals(email)) {
                return customer;
            }
        }
        throw new FlightBookingSystemException("There is no customer with that email.");
    }

    public void addFlight(Flight flight) throws FlightBookingSystemException {
        if (flights.containsKey(flight.getId())) {
            throw new IllegalArgumentException("Duplicate flight ID.");
        }
        for (Flight existing : flights.values()) {
            if (existing.getFlightNumber().equals(flight.getFlightNumber())
                    && existing.getDepartureDate().isEqual(flight.getDepartureDate())) {
                throw new FlightBookingSystemException("There is a flight with same "
                        + "number and departure date in the system");
            }
        }
        flights.put(flight.getId(), flight);
    }

    public void removeFlight(Flight flight) {
        flights.remove(flight.getId());
    }

    public void addCustomer(Customer customer) throws FlightBookingSystemException {
        if (customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Duplicate customer ID.");
        }
        customers.put(customer.getId(), customer);
    }

    public void addBooking(Booking booking) throws FlightBookingSystemException {
        if (bookings.containsKey(booking.getId())) {
            throw new IllegalArgumentException("Duplicate booking ID.");
        }
        bookings.put(booking.getId(), booking);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking.getId());
    }

    public void addUser(User user) throws FlightBookingSystemException {
        if (users.containsKey(user.getUsername())) {
            throw new FlightBookingSystemException("Username already exists.");
        }
        users.put(user.getUsername(), user);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUser(String username) {
        return users.get(username);
    }
}
