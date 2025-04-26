package ControlClass;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminControl {
    public static boolean authenticateAdmin(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("Error during admin login: " + e.getMessage());
            return false;
        }
    }
    
    public static void generateSimpleReport() {
        String reportName = "hotel_report_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportName))) {
            // 1. Occupancy Summary
            writer.println("=== OCCUPANCY SUMMARY ===");
            String occupancySql = "SELECT status, COUNT(*) as count FROM reservations GROUP BY status";
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(occupancySql)) {
                
                while (rs.next()) {
                    writer.println(rs.getString("status") + ": " + rs.getInt("count"));
                }
            }
            
            // 2. Valet Details
            writer.println("\n=== VALET PARKING ===");
            String valetSql = "SELECT COUNT(*) as total, " +
                            "SUM(CASE WHEN checkOutTime IS NULL THEN 1 ELSE 0 END) as parked " +
                            "FROM valet_parking";
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(valetSql)) {
                
                if (rs.next()) {
                    writer.println("Total vehicles: " + rs.getInt("total"));
                    writer.println("Currently parked: " + rs.getInt("parked"));
                }
            }
            
            // 3. Room Status
            writer.println("\n=== ROOM STATUS ===");
            String roomSql = "SELECT roomNumber, status FROM reservations WHERE roomNumber != '' ORDER BY roomNumber";
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(roomSql)) {
                
                while (rs.next()) {
                    writer.println("Room " + rs.getString("roomNumber") + ": " + rs.getString("status"));
                }
            }
            
            System.out.println("Report generated: " + reportName);
        } catch (IOException | SQLException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
}