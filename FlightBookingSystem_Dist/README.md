# Flight Booking Management System

A comprehensive Java-based flight booking system with both CLI and GUI interfaces.

## Features

- **Admin Dashboard**: Modern GUI with sidebar navigation
- **Flight Management**: Add, view, and manage flights
- **Customer Management**: Comprehensive customer profiles with contact details
- **Booking System**: Full booking management with seat assignments and class selection
- **Data Persistence**: Automatic save/load functionality

## Project Structure

```
FlightBookingSystem_Dist/
├── src/                    # Source code
│   └── bcu/cmp5332/bookingsystem/
│       ├── commands/       # Command pattern implementations
│       ├── data/          # Data persistence layer
│       ├── gui/           # Swing GUI components
│       ├── main/          # Main application entry point
│       └── model/         # Domain models
├── bin/                   # Compiled classes
└── resources/             # Data files
    └── data/
        ├── customers.txt
        ├── flights.txt
        └── bookings.txt
```

## Running the Project

### Option 1: Command Line

1. **Compile** (if not already compiled):
   ```bash
   javac -d bin -sourcepath src src/bcu/cmp5332/bookingsystem/main/Main.java
   ```

2. **Run**:
   ```bash
   java -cp bin bcu.cmp5332.bookingsystem.main.Main
   ```

3. **Launch GUI**:
   - Type `loadgui` in the console
   - Or use `help` to see all available commands

### Option 2: IntelliJ IDEA

1. **Open Project**:
   - File → Open → Select the `FlightBookingSystem_Dist` folder

2. **Configure**:
   - Right-click `src` folder → Mark Directory as → Sources Root
   - File → Project Structure → Modules → Set output path to `bin`

3. **Create Run Configuration**:
   - Run → Edit Configurations → + → Application
   - Name: `Flight Booking System`
   - Main class: `bcu.cmp5332.bookingsystem.main.Main`
   - Working directory: `[project root]`

4. **Run**:
   - Click the green Run button or press Shift + F10
   - Type `loadgui` to launch the GUI

### Option 3: VS Code

1. **Prerequisites**:
   - Install "Extension Pack for Java" from Microsoft

2. **Open Project**:
   - File → Open Folder → Select `FlightBookingSystem_Dist`

3. **Run**:
   - Press F5 or click Run → Start Debugging
   - Type `loadgui` to launch the GUI

   **Or use the integrated terminal**:
   ```bash
   java -cp bin bcu.cmp5332.bookingsystem.main.Main
   ```

## Using the Application

### GUI Mode (Recommended)

1. Run the application and type `loadgui`
2. The Admin Dashboard will open with:
   - **Dashboard**: View system statistics
   - **Flights**: Manage flights
   - **Customers**: Manage customer information
   - **Bookings**: Create and manage bookings

### CLI Mode

Available commands:
- `listflights` - Display all flights
- `listcustomers` - Display all customers
- `addflight` - Add a new flight
- `addcustomer` - Add a new customer
- `showflight [id]` - Show flight details
- `showcustomer [id]` - Show customer details
- `addbooking [customer_id] [flight_id]` - Create a booking
- `cancelbooking [customer_id] [flight_id]` - Cancel a booking
- `loadgui` - Launch the GUI
- `help` - Show all commands
- `exit` - Exit the application

## Customer Fields

When adding a customer, you can specify:
- **Name** (Required)
- **Phone** (Required)
- **Email** (Optional)
- **Address** (Optional)

## Booking Fields

When creating a booking, you can specify:
- **Customer ID** (Required)
- **Flight ID** (Required)
- **Seat Number** (Required) - e.g., "12A", "5B"
- **Booking Class** (Required) - Economy, Premium Economy, Business, or First Class
- **Special Requests** (Optional) - Dietary requirements, wheelchair assistance, etc.

## Data Storage

All data is automatically saved to text files in the `resources/data/` directory:
- `customers.txt` - Customer information
- `flights.txt` - Flight information
- `bookings.txt` - Booking records

Data is loaded on startup and saved on exit.

## System Requirements

- Java 8 or higher
- Windows/Mac/Linux operating system

## Troubleshooting

### "Cannot find or load main class"
- Ensure you're running from the project root directory
- Check that the `bin` folder contains compiled classes
- Recompile using the command above

### GUI doesn't appear
- Make sure you typed `loadgui` correctly
- Check that your system supports Java Swing
- Try running with `java -version` to verify Java installation

### Data not persisting
- Ensure the `resources/data/` directory exists
- Check file permissions for the data directory
- Verify the application has write access

## Development

To modify the project:
1. Edit source files in the `src` directory
2. Recompile using the compile command
3. Run to test changes

## License

Educational project for OOP coursework.
