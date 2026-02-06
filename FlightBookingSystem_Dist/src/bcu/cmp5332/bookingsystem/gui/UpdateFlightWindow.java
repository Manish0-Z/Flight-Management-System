package bcu.cmp5332.bookingsystem.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.UpdateFlight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;

public class UpdateFlightWindow extends JFrame implements ActionListener {

    private final GuiWindow mw;
    private final Flight flight;
    private final JTextField flightNoText = new JTextField();
    private final JTextField originText = new JTextField();
    private final JTextField destinationText = new JTextField();
    private final JTextField depDateText = new JTextField();
    private final JTextField capacityText = new JTextField();
    private final JTextField priceText = new JTextField();

    private final JButton updateBtn = new JButton("Update");
    private final JButton cancelBtn = new JButton("Cancel");

    public UpdateFlightWindow(GuiWindow mw, Flight flight) {
        this.mw = mw;
        this.flight = flight;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {

        }

        setTitle("Update Flight");

        setSize(350, 280);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(7, 2));
        topPanel.add(new JLabel("Flight No : "));
        flightNoText.setText(flight.getFlightNumber());
        topPanel.add(flightNoText);
        topPanel.add(new JLabel("Origin : "));
        originText.setText(flight.getOrigin());
        topPanel.add(originText);
        topPanel.add(new JLabel("Destination : "));
        destinationText.setText(flight.getDestination());
        topPanel.add(destinationText);
        topPanel.add(new JLabel("Departure Date (YYYY-MM-DD) : "));
        depDateText.setText(flight.getDepartureDate().toString());
        topPanel.add(depDateText);
        topPanel.add(new JLabel("Capacity : "));
        capacityText.setText(String.valueOf(flight.getCapacity()));
        topPanel.add(capacityText);
        topPanel.add(new JLabel("Price : "));
        priceText.setText(String.valueOf(flight.getPrice()));
        topPanel.add(priceText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(updateBtn);
        bottomPanel.add(cancelBtn);

        updateBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo((Component) mw);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            updateFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }

    }

    private void updateFlight() {
        try {
            String flightNumber = flightNoText.getText();
            String origin = originText.getText();
            String destination = destinationText.getText();
            LocalDate departureDate = null;
            try {
                departureDate = LocalDate.parse(depDateText.getText());
            }
            catch (DateTimeParseException dtpe) {
                throw new FlightBookingSystemException("Date must be in YYYY-MM-DD format");
            }
            int capacity = Integer.parseInt(capacityText.getText());
            double price = Double.parseDouble(priceText.getText());
            // create and execute the UpdateFlight Command
            Command updateFlight = new UpdateFlight(flight.getId(), flightNumber, origin, destination, departureDate, capacity, price);
            updateFlight.execute(mw.getFlightBookingSystem());
            // refresh the view with the list of flights
            mw.displayFlights();
            // hide (close) the UpdateFlightWindow
            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}