// HotelInterface.java
// Main entry point for the Hotel System application

import BoundaryClasses.AdminUI;
import BoundaryClasses.ReceptionistUI;
import ControlClass.ReservationControl;
import ControlClass.ValetControl;
import EntityClasses.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class HotelInterface {

    // Define the date format we expect
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static void main(String[] args) {
        // Initialize the hotel database on startup
        ControlClass.DatabaseManager.initializeDatabase();

        Scanner scanner = new Scanner(System.in);

        // Main system loop
        while (true) {
            displayMainMenu();
            System.out.print("Choose an option: ");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1 -> bookReservation(scanner);
                case 2 -> checkIn(scanner);
                case 3 -> checkOut(scanner);
                case 4 -> cancelReservation(scanner);
                case 5 -> ReceptionistUI.login(scanner);
                case 6 -> parkVehicle(scanner); // Updated method call
                case 7 -> retrieveVehicle(scanner);
                case 8 -> AdminUI.login(scanner);
                case 0 -> {
                    System.out.println("Goodbye!");
                    scanner.close(); // Close the scanner
                    return; // Exit the program
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Displays the main menu options
    private static void displayMainMenu() {
        System.out.println("\n=== Welcome to the Hotel System ===");
        System.out.println("1. Book Reservation");
        System.out.println("2. Check In");
        System.out.println("3. Check Out");
        System.out.println("4. Cancel Reservation");
        System.out.println("5. Receptionist Login");
        System.out.println("6. Valet Parking-Park Vehicle");
        System.out.println("7. Valet Parking-Retrieve Vehicle");
        System.out.println("8. Admin Login (Generate Report)");
        System.out.println("0. Exit");
    }

    // Safely retrieves the user's menu choice
    private static int getUserChoice(Scanner scanner) {
        try {
            // Read the whole line and parse to avoid leftover newline issues
            int choice = Integer.parseInt(scanner.nextLine());
            return choice;
        } catch (NumberFormatException e) { // Catch specific exception
            System.out.println("Invalid input. Please enter a number.");
            // No need to consume newline here as nextLine() was used
            return -1; // trigger "invalid option"
        }
    }

    // Handles booking a reservation with date validation
    private static void bookReservation(Scanner scanner) {
        System.out.print("Enter your full name: ");
        String name = scanner.nextLine();

        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;
        String checkInStr = "";
        String checkOutStr = "";
        LocalDate today = LocalDate.now(); // Get today's date

        // Loop to get a valid check-in date
        while (checkInDate == null) {
            System.out.print("Enter check-in date (MM/dd/yyyy): ");
            checkInStr = scanner.nextLine();
            try {
                checkInDate = LocalDate.parse(checkInStr, DATE_FORMATTER);
                if (checkInDate.isBefore(today)) {
                    System.out.println("Check-in date cannot be in the past. Please enter today's date or a future date.");
                    checkInDate = null; // Reset to loop again
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use MM/dd/yyyy.");
            }
        }

        // Loop to get a valid check-out date (must be after or on check-in date)
        while (checkOutDate == null) {
            System.out.print("Enter check-out date (MM/dd/yyyy): ");
            checkOutStr = scanner.nextLine();
            try {
                checkOutDate = LocalDate.parse(checkOutStr, DATE_FORMATTER);
                if (checkOutDate.isBefore(checkInDate)) {
                    System.out.println("Check-out date cannot be before the check-in date. Please try again.");
                    checkOutDate = null; // Reset to loop again
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use MM/dd/yyyy.");
            }
        }

        // Now call the control method with validated date strings
        try {
            Reservation res = ReservationControl.bookReservation(name, checkInStr, checkOutStr);
            if (res != null) {
                System.out.println("Reservation booked successfully! Your Hotel ID is: " + res.getHotelID());
            } else {
                System.out.println("Failed to book reservation. Please check details or contact support.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error booking reservation: " + e.getMessage());
        }
    }


    // Handles customer check-in
    private static void checkIn(Scanner scanner) {
        System.out.print("Enter your Hotel ID: ");
        String hotelID = scanner.nextLine();

        String roomNumber = ReservationControl.checkIn(hotelID);
        if (roomNumber != null) {
            System.out.println("Check-in successful! Your room number is: " + roomNumber);
        } else {
            // Specific error message is printed within ReservationControl.checkIn
        }
    }

    // Handles customer check-out
    private static void checkOut(Scanner scanner) {
        System.out.print("Enter your Hotel ID: ");
        String hotelID = scanner.nextLine();

        boolean success = ReservationControl.updateStatus(hotelID, "checked-out");
        if (success) {
            System.out.println("Check-out successful. Thank you for staying with us!");
        } else {
            // Specific error message is printed within ReservationControl.updateStatus
        }
    }

    // Handles reservation cancellation
    private static void cancelReservation(Scanner scanner) {
        System.out.print("Enter your Hotel ID to cancel your reservation: ");
        String hotelID = scanner.nextLine();

        boolean cancelled = ReservationControl.cancelReservation(hotelID);
        if (cancelled) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            // Specific error message is printed within ReservationControl.cancelReservation
        }
    }

    // Handles valet parking (parking the vehicle) with license plate validation
    private static void parkVehicle(Scanner scanner) {
        System.out.print("Enter your Hotel ID: ");
        String hotelID = scanner.nextLine();


        String licensePlate = "";
        // *** Loop to validate license plate length ***
        while (true) {
            System.out.print("Enter your license plate number (must be 7 characters): ");
            licensePlate = scanner.nextLine();
            if (licensePlate != null && licensePlate.length() == 7) {
                break; // Exit loop if valid
            } else {
                System.out.println("Invalid input. License plate must be exactly 7 characters long.");
            }
        }

        // Call control method with validated license plate
        String valetID = ValetControl.parkVehicle(hotelID, licensePlate);
        if (valetID != null) {
            System.out.println("Vehicle parked successfully! Your valet ticket number is: " + valetID);
        } else {
            // Specific error message is printed within ValetControl.parkVehicle
            System.out.println("Error parking vehicle. Please check your Hotel ID or contact support.");
        }
    }

    // Handles valet parking (retrieving the vehicle)
    private static void retrieveVehicle(Scanner scanner) {
        System.out.print("Enter your valet ticket number: ");
        String valetID = scanner.nextLine();

        boolean retrieved = ValetControl.retrieveVehicle(valetID);
        if (retrieved) {
            String info = ValetControl.getValetInfo(valetID); // Get info after successful retrieval
            if (info != null) {
                System.out.println("Vehicle retrieved successfully:\n" + info);
            } else {
                System.out.println("Vehicle retrieved, but could not fetch details."); // Should ideally not happen if retrieval worked
            }
        } else {
            System.out.println("Failed to retrieve vehicle. Invalid valet ticket number or vehicle already retrieved.");
        }
    }
}
