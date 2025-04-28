package com.mycompany.hotelsystem;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ReceptionistManager {

    private ReservationSystem reservationSystem = new ReservationSystem();
    private Scanner scanner = new Scanner(System.in);

    public void addReservation() {
        try {
            System.out.print("Enter Guest ID: ");
            int guestID = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Room Number: ");
            int roomNumber = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Check-In Date (yyyy-MM-dd): ");
            LocalDate checkIn = LocalDate.parse(scanner.nextLine());

            System.out.print("Enter Check-Out Date (yyyy-MM-dd): ");
            LocalDate checkOut = LocalDate.parse(scanner.nextLine());

            if (!checkOut.isAfter(checkIn)) {
                System.out.println("Error: Check-out must be after check-in.");
                return;
            }

            boolean available = reservationSystem.checkRoomAvailability(roomNumber, checkIn, checkOut);

            if (!available) {
                System.out.println("Room is not available for those dates.");
                return;
            }

            reservationSystem.createReservation(guestID, roomNumber, checkIn, checkOut);
            System.out.println("Reservation successfully created!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    // Optional: if you want to reuse the same ReservationSystem instance in HotelSystem
    public ReservationSystem getReservationSystem() {
        return reservationSystem;
    }
}
