
package com.mycompany.hotelsystem;

import java.sql.*;
import java.util.Scanner;

public class CheckOutSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/hotelsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "test";

    Scanner sc = new Scanner(System.in);

    public void processCheckOut() {
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)
        ) {
            System.out.print("Enter Guest ID for checkout: ");
            int guestID = Integer.parseInt(sc.nextLine());

            // Step 1: Find an active reservation
            String findReservation = """
                SELECT * FROM reservations
                WHERE guest_id = ? 
                AND CURDATE() BETWEEN check_in AND check_out
                AND (status IS NULL OR status = 'Booked')
            """;

            PreparedStatement findStmt = conn.prepareStatement(findReservation);
            findStmt.setInt(1, guestID);
            ResultSet rs = findStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No active reservation found for Guest ID: " + guestID);
                return;
            }

            int reservationId = rs.getInt("id");

            // Step 2: Ask physical checkout questions
            System.out.print("Is the room clean and undamaged? (Y/N): ");
            boolean clean = sc.nextLine().equalsIgnoreCase("Y");

            System.out.print("Was the keycard returned? (Y/N): ");
            boolean cardReturned = sc.nextLine().equalsIgnoreCase("Y");

            System.out.print("Is there a late checkout request? (Y/N): ");
            boolean lateCheckout = sc.nextLine().equalsIgnoreCase("Y");

            // Step 3: Update reservation status to Checked Out
            String updateReservation = "UPDATE reservations SET status = 'Checked Out' WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateReservation);
            updateStmt.setInt(1, reservationId);
            updateStmt.executeUpdate();

            // Step 4: If valet exists, mark returned
            String updateValet = "UPDATE valet_logs SET returned = TRUE WHERE guest_name = (SELECT name FROM guests WHERE id = ?)";
            PreparedStatement valetStmt = conn.prepareStatement(updateValet);
            valetStmt.setInt(1, guestID);
            valetStmt.executeUpdate();

            // Step 5: Final message
            if (clean && cardReturned && !lateCheckout) {
                System.out.println("Checkout complete. Reservation closed. Valet returned (if applicable).");
            } else {
                System.out.println("Checkout completed but flagged for staff review (issues noted).");
            }

        } catch (Exception e) {
            System.out.println("Error during checkout: " + e.getMessage());
        }
    }
}
