package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class CancelBookingWindow extends JFrame implements ActionListener {

    private final MainWindow mw;
    private final JTextField custIdText = new JTextField();
    private final JTextField flightIdText = new JTextField();

    private final JButton cancelBtn = new JButton("Cancel Booking");
    private final JButton closeBtn = new JButton("Close");

    public CancelBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {

        }

        setTitle("Cancel Booking");

        setSize(300, 150);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));
        topPanel.add(new JLabel("Customer ID : "));
        topPanel.add(custIdText);
        topPanel.add(new JLabel("Flight ID : "));
        topPanel.add(flightIdText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(cancelBtn);
        bottomPanel.add(closeBtn);

        cancelBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == cancelBtn) {
            cancelBooking();
        } else if (ae.getSource() == closeBtn) {
            this.setVisible(false);
        }
    }

    private void cancelBooking() {
        try {
            int custId = Integer.parseInt(custIdText.getText());
            int flightId = Integer.parseInt(flightIdText.getText());

            Command cancelBooking = new CancelBooking(custId, flightId);
            cancelBooking.execute(mw.getFlightBookingSystem());

            JOptionPane.showMessageDialog(this, "Booking cancelled successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "IDs must be integers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
