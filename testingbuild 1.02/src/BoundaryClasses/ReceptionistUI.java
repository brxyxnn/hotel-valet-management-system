package BoundaryClasses; // Ensure package name matches your structure

import ControlClass.AuthenticationControl;
import ControlClass.ReservationControl;
import ControlClass.ValetControl; // Assuming ValetControl exists and is needed
import EntityClasses.Reservation;
import EntityClasses.ReservationStore;

import java.util.List;
import java.util.Scanner;

// Assuming ValetControl has this static method, adjust if necessary
// import static ControlClass.ValetControl.updateParkingSpot;

public class ReceptionistUI {

    public static void login(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Use the correct path if DatabaseManager is in ControlClass package
        if (AuthenticationControl.authenticateReceptionist(username, password)) {
            System.out.println("Login successful!");
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
            System.out.println("4. Cancel Reservation (for 'booked' status only)"); // Clarified purpose
            System.out.println("5. View Valet Parking Information");
            System.out.println("6. Update Valet Parking Spot"); // Renamed for clarity
            System.out.println("7. Logout");

            System.out.print("Choose: ");
            int choice = -1; // Initialize choice
            try {
                choice = Integer.parseInt(scanner.nextLine()); // Read whole line and parse
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue; // Skip to next iteration
            }


            switch (choice) {
                case 1: { // View Reservations
                    List<Reservation> reservations = ReservationStore.loadReservations();
                    if (reservations.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        System.out.println("\n--- Reservations ---");
                        for (Reservation res : reservations) {
                            System.out.println(res); // Uses Reservation's toString() method
                        }
                        System.out.println("--------------------");
                    }
                    break; // Added break
                }
                case 2: { // Modify Reservation Dates
                    System.out.print("Enter Hotel ID to modify: ");
                    String hotelID = scanner.nextLine();

                    // Get current details first to inform the user/process
                    List<Reservation> currentReservations = ReservationStore.loadReservations();
                    Reservation currentRes = null;
                    for(Reservation r : currentReservations) {
                        if(r.getHotelID().equals(hotelID)){
                            currentRes = r;
                            break;
                        }
                    }

                    if (currentRes == null) {
                        System.out.println("Hotel ID not found.");
                        break; // Exit case 2
                    }

                    String currentStatus = currentRes.getStatus();
                    String currentCheckIn = currentRes.getCheckIn();
                    String currentCheckOut = currentRes.getCheckOut();
                    String newCheckIn;
                    String newCheckOut;

                    System.out.println("Current Status: " + currentStatus);
                    System.out.println("Current Check-in: " + currentCheckIn);
                    System.out.println("Current Check-out: " + currentCheckOut);


                    if (currentStatus.equalsIgnoreCase("checked-out")) {
                        System.out.println("This reservation is already checked-out and cannot be modified.");
                        break; // Exit case 2
                    } else if (currentStatus.equalsIgnoreCase("checked-in")) {
                        System.out.println("Guest is checked-in. Check-in date cannot be changed.");
                        newCheckIn = currentCheckIn; // Keep the current check-in date
                        System.out.print("Enter new check-out date (MM/dd/yyyy) [" + currentCheckOut + "]: ");
                        newCheckOut = scanner.nextLine();
                        // If user presses Enter without typing, keep the old date
                        if (newCheckOut.trim().isEmpty()) {
                            newCheckOut = currentCheckOut;
                            System.out.println("Keeping existing check-out date.");
                        }
                    } else { // Assuming 'booked' or other modifiable status
                        System.out.print("Enter new check-in date (MM/dd/yyyy) [" + currentCheckIn + "]: ");
                        newCheckIn = scanner.nextLine();
                        if (newCheckIn.trim().isEmpty()) {
                            newCheckIn = currentCheckIn;
                            System.out.println("Keeping existing check-in date.");
                        }

                        System.out.print("Enter new check-out date (MM/dd/yyyy) [" + currentCheckOut + "]: ");
                        newCheckOut = scanner.nextLine();
                        if (newCheckOut.trim().isEmpty()) {
                            newCheckOut = currentCheckOut;
                            System.out.println("Keeping existing check-out date.");
                        }
                    }

                    // Attempt modification via control layer which has the final validation
                    if (ReservationControl.modifyReservation(hotelID, newCheckIn, newCheckOut)) {
                        // Success message is now printed within modifyReservation if successful
                        System.out.println("Reservation dates modified successfully."); // Optional: Add confirmation here too
                    } else {
                        // Failure message is printed within modifyReservation
                        System.out.println("Failed to modify reservation. Please check details and status.");
                    }
                    break; // Added break
                }
                case 3: { // Update Room Number
                    System.out.print("Enter Hotel ID to update room number: ");
                    String hotelID = scanner.nextLine();
                    System.out.print("Enter new room number: ");
                    String newRoomNumber = scanner.nextLine();
                    if (ReservationControl.updateRoomNumber(hotelID, newRoomNumber)) {
                        System.out.println("Room number updated.");
                    } else {
                        System.out.println("Failed to update room number (Hotel ID not found or other issue).");
                    }
                    break; // Added break
                }
                case 4: { // Cancel Reservation
                    System.out.print("Enter Hotel ID to cancel: ");
                    String hotelID = scanner.nextLine();
                    if (ReservationControl.cancelReservation(hotelID)) {
                        System.out.println("Reservation cancelled successfully.");
                    } else {
                        // Failure message is printed within cancelReservation
                        System.out.println("Failed to cancel reservation (Hotel ID not found or status prevents cancellation).");
                    }
                    break; // Added break
                }
                case 5: {  // View Valet Info
                    System.out.print("Enter valet ticket number: ");
                    String valetID = scanner.nextLine();
                    // Assuming ValetControl has getValetInfo - adjust if needed
                    // String info = ValetControl.getValetInfo(valetID);
                    String info = "Valet info placeholder for ID: " + valetID; // Placeholder
                    if (info != null) { // Replace placeholder logic
                        System.out.println("Valet Information:\n" + info);
                    } else {
                        System.out.println("No information found for that valet ticket.");
                    }
                    break; // Added break
                }
                case 6: {  // Update Valet Info
                    System.out.print("Enter valet ticket number to update: ");
                    String valetID = scanner.nextLine();
                    System.out.print("Enter new parking spot: ");
                    String newSpot = scanner.nextLine();

                    // Assuming ValetControl has updateParkingSpot - adjust if needed
                    // boolean success = ValetControl.updateParkingSpot(valetID, newSpot);
                    boolean success = false; // Placeholder
                    if (success) { // Replace placeholder logic
                        System.out.println("Parking spot updated successfully.");
                    } else {
                        System.out.println("Failed to update parking spot (Valet ID not found or other issue).");
                    }
                    break; // Added break
                }
                case 7: { // Logout
                    System.out.println("Logging out.");
                    return; // Exit the receptionist menu
                }
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break; // Added break
            }
        }
    }
}
