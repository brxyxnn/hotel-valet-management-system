
package com.mycompany.hotelsystem;

import java.sql.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

public class CheckOutSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/hotelsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "test";

    Scanner sc = new Scanner(System.in);

    public void processCheckOut() {
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)
        ) {
            int guestID = Integer.parseInt(JOptionPane.showInputDialog("Enter Guest ID for checkout"));

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
                JOptionPane.showMessageDialog(null, "No active reservation found for Guest ID: " + guestID);
                return;
            }

            int reservationId = rs.getInt("id");

            // Step 2: Ask physical checkout questions
            boolean clean = JOptionPane.showInputDialog("Is the room clean and undamaged? (Y/N)").equalsIgnoreCase("Y");

            boolean cardReturned = JOptionPane.showInputDialog("Was the keycard returned? (Y/N)").equalsIgnoreCase("Y");

            boolean lateCheckout = JOptionPane.showInputDialog("Is there a late checkout request? (Y/N)").equalsIgnoreCase("Y");

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
                JOptionPane.showMessageDialog(null, "Checkout complete. Reservation closed. Valet returned (if applicable).");
            } else {
                JOptionPane.showMessageDialog(null, "Checkout completed but flagged for staff review (issues noted).");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error during checkout: " + e.getMessage());
        }
    }
}
