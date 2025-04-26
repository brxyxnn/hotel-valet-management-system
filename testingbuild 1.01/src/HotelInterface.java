// HotelInterface.java
// Main entry point for the Hotel System application

import BoundaryClasses.AdminUI;
import BoundaryClasses.ReceptionistUI;
import ControlClass.ReservationControl;
import ControlClass.ValetControl;
import EntityClasses.Reservation;

import java.util.Scanner;

public class HotelInterface {

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
                case 6 -> parkVehicle(scanner);
                case 7 -> retrieveVehicle(scanner);
                case 8 -> AdminUI.login(scanner);
                case 0 -> {
                    System.out.println("Goodbye!");
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
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume leftover newline
            return choice;
        } catch (Exception e) {
            scanner.nextLine(); // clear invalid input
            return -1; // trigger "invalid option"
        }
    }

    // Handles booking a reservation
    private static void bookReservation(Scanner scanner) {
        System.out.print("Enter your full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter check-in date (MM/dd/yyyy): ");
        String checkIn = scanner.nextLine();

        System.out.print("Enter check-out date (MM/dd/yyyy): ");
        String checkOut = scanner.nextLine();

        Reservation res = ReservationControl.bookReservation(name, checkIn, checkOut);
        System.out.println("Reservation booked successfully! Your Hotel ID is: " + res.getHotelID());
    }

    // Handles customer check-in
    private static void checkIn(Scanner scanner) {
        System.out.print("Enter your Hotel ID: ");
        String hotelID = scanner.nextLine();

        String roomNumber = ReservationControl.checkIn(hotelID);
        if (roomNumber != null) {
            System.out.println("Check-in successful! Your room number is: " + roomNumber);
        } else {
            System.out.println("Hotel ID not found. Please try again.");
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
            System.out.println("Hotel ID not found. Please try again.");
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
            System.out.println("Hotel ID not found. Please try again.");
        }
    }

    // Handles valet parking (parking the vehicle)
    private static void parkVehicle(Scanner scanner) {
        System.out.print("Enter your Hotel ID: ");
        String hotelID = scanner.nextLine();

        System.out.print("Enter your license plate number: ");
        String licensePlate = scanner.nextLine();

        String valetID = ValetControl.parkVehicle(hotelID, licensePlate);
        if (valetID != null) {
            System.out.println("Vehicle parked successfully! Your valet ticket number is: " + valetID);
        } else {
            System.out.println("Error parking vehicle. Please check your Hotel ID.");
        }
    }

    // Handles valet parking (retrieving the vehicle)
    private static void retrieveVehicle(Scanner scanner) {
        System.out.print("Enter your valet ticket number: ");
        String valetID = scanner.nextLine();

        boolean retrieved = ValetControl.retrieveVehicle(valetID);
        if (retrieved) {
            String info = ValetControl.getValetInfo(valetID);
            System.out.println("Vehicle retrieved successfully:\n" + info);
        } else {
            System.out.println("Invalid valet ticket number or vehicle already retrieved.");
        }
    }
}
