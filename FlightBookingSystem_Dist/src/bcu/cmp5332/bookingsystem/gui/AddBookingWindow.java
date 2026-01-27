package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AddBookingWindow extends JFrame implements ActionListener {

    private final MainWindow mw;
    private final JTextField custIdText = new JTextField();
    private final JTextField flightIdText = new JTextField();
    private final JTextField seatNumberText = new JTextField();
    private JComboBox<String> bookingClassCombo;
    private final JTextField specialRequestsText = new JTextField();

    private final JButton addBtn = new JButton("Issue");
    private final JButton cancelBtn = new JButton("Cancel");

    public AddBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                    IllegalAccessException | UnsupportedLookAndFeelException ex) {

        }

        setTitle("Issue Booking");

        setSize(400, 280);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(5, 2, 5, 5));

        topPanel.add(new JLabel("Customer ID : "));
        topPanel.add(custIdText);

        topPanel.add(new JLabel("Flight ID : "));
        topPanel.add(flightIdText);

        topPanel.add(new JLabel("Seat Number : "));
        topPanel.add(seatNumberText);

        topPanel.add(new JLabel("Booking Class : "));
        String[] classes = { "Economy", "Premium Economy", "Business", "First Class" };
        bookingClassCombo = new JComboBox<>(classes);
        topPanel.add(bookingClassCombo);

        topPanel.add(new JLabel("Special Requests : "));
        topPanel.add(specialRequestsText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addBooking();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void addBooking() {
        try {
            int custId = Integer.parseInt(custIdText.getText());
            int flightId = Integer.parseInt(flightIdText.getText());
            String seatNumber = seatNumberText.getText();
            String bookingClass = (String) bookingClassCombo.getSelectedItem();
            String specialRequests = specialRequestsText.getText();

            // Basic validation
            if (seatNumber.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seat Number is required", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Command addBooking = new AddBooking(custId, flightId, seatNumber, bookingClass, specialRequests);
            addBooking.execute(mw.getFlightBookingSystem());

            JOptionPane.showMessageDialog(this, "Booking issued successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "IDs must be integers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
