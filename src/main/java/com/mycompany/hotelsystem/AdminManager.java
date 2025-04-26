
package com.mycompany.hotelsystem;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminManager {

    // MySQL connection info
    private static final String URL = "jdbc:mysql://localhost:3306/hotelsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "test";

    // Generates a report showing currently checked-in guests + valet info
    public void generateGuestValetReport() {
        // Create timestamped filename
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = "admin_guest_report_" + timestamp + ".txt";

        // SQL to fetch active (not checked-out) guests
        String query = """
            SELECT 
                g.id AS guest_id,
                g.name AS guest_name,
                r.room_number,
                r.check_in,
                r.check_out,
                CASE WHEN v.id IS NOT NULL THEN 'Yes' ELSE 'No' END AS valet_status
            FROM reservations r
            JOIN guests g ON r.guest_id = g.id
            LEFT JOIN valet_logs v ON v.guest_name = g.name
            WHERE CURDATE() BETWEEN r.check_in AND r.check_out
            AND (r.status IS NULL OR r.status = 'Booked')
        """;

        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))
        ) {
            // Write report header
            writer.write("Guest Report with Valet Status - Generated at " + timestamp + "\n\n");

            // Loop through results and write details
            while (rs.next()) {
                writer.write("Guest ID: " + rs.getInt("guest_id") + "\n");
                writer.write("Name: " + rs.getString("guest_name") + "\n");
                writer.write("Room: " + rs.getInt("room_number") + "\n");
                writer.write("Check-In: " + rs.getDate("check_in") + "\n");
                writer.write("Check-Out: " + rs.getDate("check_out") + "\n");
                writer.write("Valet: " + rs.getString("valet_status") + "\n");
                writer.write("------------------------------------------------\n");
            }

            writer.flush();
            writer.close();

            System.out.println("Report generated: " + fileName);

            // Open the report automatically after generating
            File fileToOpen = new File(fileName);
            if (fileToOpen.exists()) {
                Desktop.getDesktop().open(fileToOpen);
            }

        } catch (Exception e) {
            System.out.println("Failed to generate report: " + e.getMessage());
        }
    }


}
