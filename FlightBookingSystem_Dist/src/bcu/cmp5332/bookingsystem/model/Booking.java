package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

public class Booking {

    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private String seatNumber;
    private String bookingClass;
    private String specialRequests;

    public Booking(Customer customer, Flight flight, LocalDate bookingDate, String seatNumber, String bookingClass,
            String specialRequests) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.seatNumber = seatNumber;
        this.bookingClass = bookingClass;
        this.specialRequests = specialRequests;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(String bookingClass) {
        this.bookingClass = bookingClass;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
}
