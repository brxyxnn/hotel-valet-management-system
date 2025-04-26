package ControlClass;

import java.sql.*;
import java.util.Random;

public class ValetControl {
    private static final Random rand = new Random();

    public static String parkVehicle(String hotelID, String licensePlate) {
        String valetID = generateValetID();
        String parkingSpot = generateParkingSpot();
        
        String sql = "INSERT INTO valet_parking (valetID, hotelID, licensePlate, parkingSpot) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, valetID);
            pstmt.setString(2, hotelID);
            pstmt.setString(3, licensePlate);
            pstmt.setString(4, parkingSpot);
            
            pstmt.executeUpdate();
            return valetID;
        } catch (SQLException e) {
            System.out.println("Error parking vehicle: " + e.getMessage());
            return null;
        }
    }

    public static boolean retrieveVehicle(String valetID) {
        String sql = "UPDATE valet_parking SET checkOutTime = CURRENT_TIMESTAMP " +
                     "WHERE valetID = ? AND checkOutTime IS NULL";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, valetID);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error retrieving vehicle: " + e.getMessage());
            return false;
        }
    }

    public static String getValetInfo(String valetID) {
        String sql = "SELECT v.*, r.name FROM valet_parking v " +
                     "JOIN reservations r ON v.hotelID = r.hotelID " +
                     "WHERE v.valetID = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, valetID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return String.format("Guest: %s%nLicense Plate: %s%nParking Spot: %s%nCheck-In Time: %s",
                            rs.getString("name"),
                            rs.getString("licensePlate"),
                            rs.getString("parkingSpot"),
                            rs.getString("checkInTime"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting valet info: " + e.getMessage());
        }
        return null;
    }


    private static String generateValetID() {
        return String.format("%04d", rand.nextInt(10000));
    }

    private static String generateParkingSpot() {
        char zone = (char) ('A' + rand.nextInt(5));
        int spot = rand.nextInt(50) + 1;
        return String.format("%s-%02d", zone, spot);
    }
    public static boolean updateParkingSpot(String valetID, String newSpot) {
        String sql = "UPDATE valet_parking SET parkingSpot = ? WHERE valetID = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newSpot);
            pstmt.setString(2, valetID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating parking spot: " + e.getMessage());
            return false;
        }
    }
}