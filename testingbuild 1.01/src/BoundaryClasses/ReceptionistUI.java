package BoundaryClasses;// BoundaryClass.ReceptionistUI.java
import ControlClass.AuthenticationControl;
import ControlClass.ReservationControl;
import ControlClass.ValetControl;
import EntityClasses.Reservation;
import EntityClasses.ReservationStore;

import java.util.*;

import static ControlClass.ValetControl.updateParkingSpot;

public class ReceptionistUI {
    public static void login(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (AuthenticationControl.authenticateReceptionist(username, password)) {
            menu(scanner);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void menu(Scanner scanner) {
        while (true) {
            System.out.println("\nReceptionist Menu:");
            System.out.println("1. View Reservations");
            System.out.println("2. Modify Reservation Dates");
            System.out.println("3. Update Room Number");
            System.out.println("4. Remove Hotel Guest");
            System.out.println("5. View Valet Parking Information");
            System.out.println("6. Update Valet Parking Information");
            System.out.println("7. Logout");

            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    List<Reservation> reservations = ReservationStore.loadReservations();
                    System.out.println("Reservations:");
                    for (Reservation res : reservations) {
                        System.out.println(res);
                    }
                }
                case 2 -> {
                    System.out.print("Enter Hotel ID to modify: ");
                    String hotelID = scanner.nextLine();
                    System.out.print("Enter new check-in date (mm/dd/yyyy): ");
                    String newIn = scanner.nextLine();
                    System.out.print("Enter new check-out date (mm/dd/yyyy): ");
                    String newOut = scanner.nextLine();
                    if (ReservationControl.modifyReservation(hotelID, newIn, newOut)) {
                        System.out.println("Reservation dates modified.");
                    } else {
                        System.out.println("Hotel ID not found.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter Hotel ID to update room number: ");
                    String hotelID = scanner.nextLine();
                    System.out.print("Enter new room number: ");
                    String newRoomNumber = scanner.nextLine();
                    if (ReservationControl.updateRoomNumber(hotelID, newRoomNumber)) {
                        System.out.println("Room number updated.");
                    } else {
                        System.out.println("Hotel ID not found.");
                    }
                }
                case 4 -> {
                    System.out.print("Enter Hotel ID to cancel: ");
                    String hotelID = scanner.nextLine();
                    if (ReservationControl.cancelReservation(hotelID)) {
                        System.out.println("Reservation cancelled.");
                    } else {
                        System.out.println("Hotel ID not found.");
                    }
                }
                case 5 -> {  // View Valet Info
                    System.out.print("Enter valet ticket number: ");
                    String valetID = scanner.nextLine();
                    String info = ValetControl.getValetInfo(valetID);
                    if (info != null) {
                        System.out.println("Valet Information:\n" + info);
                    } else {
                        System.out.println("No information found for that valet ticket.");
                    }
                }
                case 6 -> {  // Update Valet Info
                    System.out.print("Enter valet ticket number to update: ");
                    String valetID = scanner.nextLine();
                    System.out.print("Enter new parking spot: ");
                    String newSpot = scanner.nextLine();

                    if (updateParkingSpot(valetID, newSpot)) {
                        System.out.println("Parking spot updated successfully.");
                    } else {
                        System.out.println("Failed to update parking spot.");
                    }
                }
                case 7 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}