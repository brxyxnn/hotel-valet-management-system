package com.mycompany.hotelsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ValetSystem {

    private static final String URL = "jdbc:mysql://localhost:3306/hotelsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "test";

    public void processValetRequest() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Assign valet? (Y/N): ");
        if (!sc.nextLine().equalsIgnoreCase("Y")) {
            System.out.println("Valet service declined.");
            return;
        }

        System.out.print("Enter guest name: ");
        String name = sc.nextLine();

        System.out.print("Enter license plate number (min 6 characters): ");
        String plate = sc.nextLine();
        if (plate.length() < 6) {
            System.out.println("License plate must be at least 6 characters.");
            return;
        }

        System.out.print("Enter vehicle location (e.g., Front, Lot B): ");
        String location = sc.nextLine();

        System.out.print("Return vehicle now? (Y/N): ");
        boolean returned = sc.nextLine().equalsIgnoreCase("Y");

        // Save to MySQL
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO valet_logs (guest_name, license_plate, location, returned) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, plate);
            stmt.setString(3, location);
            stmt.setBoolean(4, returned);

            stmt.executeUpdate();
            System.out.println("Valet logged successfully!");
            System.out.println("Guest: " + name);
            System.out.println("Location: " + location);
            System.out.println("Returned: " + (returned ? "Yes" : "No"));

        } catch (SQLException e) {
            System.out.println("Failed to log valet: " + e.getMessage());
        }
    }
}
