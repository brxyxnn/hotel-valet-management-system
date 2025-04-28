package ControlClass;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_system";
    private static final String DB_USER = "root"; //replace with your username
    private static final String DB_PASSWORD = "test"; //enter your own password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create receptionists table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS receptionists (" +
                    "username VARCHAR(50) PRIMARY KEY," +
                    "password VARCHAR(50) NOT NULL)");
            
            // Create reservations table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS reservations (" +
                    "hotelID VARCHAR(8) PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "checkIn VARCHAR(10) NOT NULL," +
                    "checkOut VARCHAR(10) NOT NULL," +
                    "status VARCHAR(20) NOT NULL," +
                    "roomNumber VARCHAR(10))");

            // In initializeDatabase() method:
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS valet_parking (" +
                    "valetID VARCHAR(8) PRIMARY KEY," +
                    "hotelID VARCHAR(8) NOT NULL," +
                    "licensePlate VARCHAR(15) NOT NULL," +
                    "parkingSpot VARCHAR(10) NOT NULL," +
                    "checkInTime DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "checkOutTime DATETIME NULL," +
                    "FOREIGN KEY (hotelID) REFERENCES reservations(hotelID) " +
                    "ON DELETE CASCADE ON UPDATE CASCADE)");
            
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
}