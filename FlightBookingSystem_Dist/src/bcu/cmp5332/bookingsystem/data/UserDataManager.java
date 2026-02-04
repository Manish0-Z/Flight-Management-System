package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class UserDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/users.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] properties = line.split(SEPARATOR, -1);
                try {
                    String username = properties[0];
                    String password = properties[1];
                    String role = properties[2];
                    String fullName = properties.length > 3 ? properties[3] : null;
                    User user = new User(username, password, role, fullName);
                    fbs.addUser(user);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse user data at line " + line_idx + " in " + RESOURCE + " (" + ex.getMessage() + ")");
                }
                line_idx++;
            }
        } catch (IOException ex) {
            // If file doesn't exist, create it with default admin
            createDefaultAdmin();
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (User user : fbs.getUsers()) {
                out.print(user.getUsername() + SEPARATOR);
                out.print(user.getPassword() + SEPARATOR);
                out.print(user.getRole() + SEPARATOR);
                out.print(user.getFullName() != null ? user.getFullName() : "" + SEPARATOR);
                out.println();
            }
        }
    }

    private void createDefaultAdmin() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            // Default admin: admin/admin
            out.println("admin::admin::admin");
        }
    }
}