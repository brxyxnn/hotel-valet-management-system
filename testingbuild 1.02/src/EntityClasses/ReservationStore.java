package EntityClasses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ReservationStore is responsible for interacting with the database
 * to load, save, and append reservation records.
 */
public class ReservationStore {

    /**
     Loads all reservations from the database into a list.
     @return List of Reservation objects retrieved from the database.
     */
    public static List<Reservation> loadReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (Connection conn = ControlClass.DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set and create Reservation objects
            while (rs.next()) {
                Reservation res = new Reservation(
                        rs.getString("hotelID"),
                        rs.getString("name"),
                        rs.getString("checkIn"),
                        rs.getString("checkOut"),
                        rs.getString("status"),
                        rs.getString("roomNumber")
                );
                reservations.add(res);
            }
        } catch (SQLException e) {
            System.out.println("Error loading reservations from database: " + e.getMessage());
        }
        return reservations;
    }

    /**
     * Saves a list of reservations to the database.
     * This method clears existing reservations and replaces them with the new list.
     *
     * @param reservations List of Reservation objects to save.
     */
    public static void saveReservations(List<Reservation> reservations) {
        String deleteSql = "DELETE FROM reservations"; // SQL to delete all existing reservations
        String insertSql = "INSERT INTO reservations (hotelID, name, checkIn, checkOut, status, roomNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?)"; // SQL to insert a new reservation

        try (Connection conn = ControlClass.DatabaseManager.getConnection()) {
            // Start transaction (manual control of commit/rollback)
            conn.setAutoCommit(false);

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

                // 1. Delete all existing records from the reservations table
                deleteStmt.executeUpdate();

                // 2. Insert each reservation from the provided list
                for (Reservation res : reservations) {
                    insertStmt.setString(1, res.getHotelID());
                    insertStmt.setString(2, res.getName());
                    insertStmt.setString(3, res.getCheckIn());
                    insertStmt.setString(4, res.getCheckOut());
                    insertStmt.setString(5, res.getStatus());
                    insertStmt.setString(6, res.getRoomNumber());
                    insertStmt.addBatch(); // Add the insert command to a batch
                }

                // Execute all insertions as a batch for better performance
                insertStmt.executeBatch();

                // Commit transaction after successful insert
                conn.commit();

            } catch (SQLException e) {
                // If something goes wrong, rollback to the previous stable state
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("Error saving reservations to database: " + e.getMessage());
        }
    }

    /**
     * Appends (adds) a single new reservation to the database without clearing existing records.
     *
     * @param reservation Reservation object to insert.
     */
    public static void appendReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (hotelID, name, checkIn, checkOut, status, roomNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ControlClass.DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set each placeholder (?) in the SQL statement with reservation data
            pstmt.setString(1, reservation.getHotelID());
            pstmt.setString(2, reservation.getName());
            pstmt.setString(3, reservation.getCheckIn());
            pstmt.setString(4, reservation.getCheckOut());
            pstmt.setString(5, reservation.getStatus());
            pstmt.setString(6, reservation.getRoomNumber());

            // Execute the SQL insert command
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving reservation to database: " + e.getMessage());
        }
    }
}
