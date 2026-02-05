package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public interface GuiWindow {
    FlightBookingSystem getFlightBookingSystem();
    default void displayFlights() {}
    default void displayCustomers() {}
}