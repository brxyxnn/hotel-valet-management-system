package ControlClass; // Ensure package name matches your structure

import java.sql.*;
import java.util.Random;
import java.util.regex.Matcher; // Import Matcher
import java.util.regex.Pattern;  // Import Pattern

public class ValetControl {
    private static final Random rand = new Random();


    private static final Pattern PARKING_SPOT_PATTERN = Pattern.compile("^[A-E]-(0[1-9]|[1-4][0-9]|50)$");

    public static String parkVehicle(String hotelID, String licensePlate) {
        // --- License Plate Validation ---
        if (licensePlate == null || licensePlate.length() != 7) {
            System.out.println("Validation Error (Control): License plate must be exactly 7 characters long.");
            return null; // Indicate failure
        }
        // --- End License Plate Validation ---


        // --- Database Interaction ---
        String valetID = generateValetID();
        String parkingSpot = generateParkingSpot(); // Use the generator for initial parking

        String sql = "INSERT INTO valet_parking (valetID, hotelID, licensePlate, parkingSpot, checkInTime) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)"; // Ensure checkInTime is set

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, valetID);
            pstmt.setString(2, hotelID);
            pstmt.setString(3, licensePlate); // Validated license plate
            pstmt.setString(4, parkingSpot);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return valetID; // Return ID on success
            } else {
                System.out.println("Database Error: Failed to insert valet record.");
                return null;
            }
        } catch (SQLException e) {
            // Catch potential foreign key constraint violations (invalid hotelID) or other SQL issues
            System.out.println("Error parking vehicle: " + e.getMessage());
            // Check for specific SQLState codes if needed (e.g., for foreign key violation)
            // if (e.getSQLState().equals("23000")) { // Example SQLState for integrity constraint violation
            //     System.out.println("Error: Invalid Hotel ID provided.");
            // }
            return null;
        }
        // --- End Database Interaction ---
    }


    public static boolean retrieveVehicle(String valetID) {
        String sql = "UPDATE valet_parking SET checkOutTime = CURRENT_TIMESTAMP " +
                "WHERE valetID = ? AND checkOutTime IS NULL";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, valetID);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // True if 1 row was updated
        } catch (SQLException e) {
            System.out.println("Error retrieving vehicle: " + e.getMessage());
            return false;
        }
    }

    public static String getValetInfo(String valetID) {
        // Corrected JOIN to link valet_parking hotelID to reservations hotelID
        String sql = "SELECT v.licensePlate, v.parkingSpot, v.checkInTime, v.checkOutTime, r.name " +
                "FROM valet_parking v " +
                "JOIN reservations r ON v.hotelID = r.hotelID " +
                "WHERE v.valetID = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, valetID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp checkOutTs = rs.getTimestamp("checkOutTime");
                    String checkOutStr = (checkOutTs == null) ? "Still Parked" : checkOutTs.toString();
                    return String.format("Guest: %s%nLicense Plate: %s%nParking Spot: %s%nCheck-In Time: %s%nCheck-Out Time: %s",
                            rs.getString("name"),
                            rs.getString("licensePlate"),
                            rs.getString("parkingSpot"),
                            rs.getTimestamp("checkInTime").toString(), // Assuming checkInTime is never null
                            checkOutStr);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting valet info: " + e.getMessage());
        }
        return null; // Not found or error occurred
    }

    // Generates a 4-digit valet ID string (e.g., "0042")
    private static String generateValetID() {
        return String.format("%04d", rand.nextInt(10000));
    }

    // Generates a parking spot in the valid format (e.g., "C-05")
    private static String generateParkingSpot() {
        char zone = (char) ('A' + rand.nextInt(5)); // A-E
        int spot = rand.nextInt(50) + 1; // 1-50
        return String.format("%s-%02d", zone, spot); // Ensures two digits for spot number
    }

    public static boolean updateParkingSpot(String valetID, String newSpot) {
        // --- Parking Spot Validation ---
        if (newSpot == null || !PARKING_SPOT_PATTERN.matcher(newSpot).matches()) {
            System.out.println("Validation Error: Invalid parking spot format.");
            System.out.println("Required format: X-NN (where X is A-E, NN is 01-50). Example: C-08");
            return false; // Indicate failure
        }
        // --- End Parking Spot Validation ---


        // --- Database Interaction ---
        String sql = "UPDATE valet_parking SET parkingSpot = ? WHERE valetID = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newSpot); // Validated spot
            pstmt.setString(2, valetID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Parking spot updated successfully in database."); // Confirmation
                return true;
            } else {
                System.out.println("Failed to update parking spot: Valet ID not found.");
                return false; // Valet ID likely didn't exist
            }
        } catch (SQLException e) {
            System.out.println("Error updating parking spot: " + e.getMessage());
            return false;
        }

    }
}
