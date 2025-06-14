package com.mycompany.hotelsystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class HotelSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize system components
        GuestManager guestManager = new GuestManager();
        ReceptionistManager receptionistManager = new ReceptionistManager();
        ReservationSystem reservationSystem = new ReservationSystem();
        AdminManager adminManager = new AdminManager();
        CheckOutSystem checkOutSystem = new CheckOutSystem(); 

        // Use MM-dd-yyyy for date input
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        System.out.println("Welcome to the Hotel Reservation and Valet System!");
        boolean running = true;

        while (running) {
            // Main menu
            System.out.println("\nMain Menu:");
            System.out.println("1. Register Guest");
            System.out.println("2. Make a Reservation");
            System.out.println("3. Request Valet");
            System.out.println("4. Check Out Guest"); 
            System.out.println("5. Generate Admin Report");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    // Guest registration
                    System.out.print("Enter guest name: ");
                    String name = scanner.nextLine();

                    Guest guest = new Guest();
                    guest.setName(name);
                    guest.setGuestId((int) (Math.random() * 10000));
                    guestManager.addGuest(guest);

                    System.out.println("Guest registered with ID: " + guest.getGuestId());
                    break;

                case "2":
                    // Make a reservation
                    try {
                        System.out.print("Enter guest ID: ");
                        int guestID = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter room number: ");
                        int roomNumber = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter check-in date (MM-dd-yyyy): ");
                        LocalDate checkIn = LocalDate.parse(scanner.nextLine(), formatter);

                        System.out.print("Enter check-out date (MM-dd-yyyy): ");
                        LocalDate checkOut = LocalDate.parse(scanner.nextLine(), formatter);

                        if (!checkOut.isAfter(checkIn)) {
                            System.out.println("Error: Check-out must be after check-in.");
                            break;
                        }

                        boolean available = reservationSystem.checkRoomAvailability(roomNumber, checkIn, checkOut);
                        if (!available) {
                            System.out.println("Sorry, room is not available for those dates.");
                        } else {
                            reservationSystem.createReservation(guestID, roomNumber, checkIn, checkOut);
                            System.out.println("Reservation successfully created.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number input. Please enter numbers only.");
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Use MM-dd-yyyy.");
                    }
                    break;

                case "3":
                    // Request Valet
                    guestManager.valetRequest();
                    break;

                case "4":
                    // Check Out Guest
                    checkOutSystem.processCheckOut();
                    break;

                case "5":
                    // Generate Admin Report
                    adminManager.generateGuestValetReport();
                    break;

                case "6":
                    // Exit
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}
