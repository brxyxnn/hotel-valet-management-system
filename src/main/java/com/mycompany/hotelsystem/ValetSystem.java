package com.mycompany.hotelsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

public class ValetSystem {

    private static final String URL = "jdbc:mysql://localhost:3306/hotelsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "test";

    public void processValetRequest() {

        String valetChoice = JOptionPane.showInputDialog("Assign valet? (Y/N)");
        if (!valetChoice.equalsIgnoreCase("Y")) {
            JOptionPane.showMessageDialog(null, "Valet service declined.");
            return;
        }

        String name = JOptionPane.showInputDialog("Enter guest name");

        String plate = JOptionPane.showInputDialog("Enter license plate number (min 6 characters)");
        while(plate.length() < 6) {
            JOptionPane.showMessageDialog(null, "License plate must be at least 6 characters.");
            plate = JOptionPane.showInputDialog("Enter license plate number (min 6 characters)");
        }

        String location = JOptionPane.showInputDialog("Enter vehicle location (e.g., Front, Lot B)");

        boolean returned = JOptionPane.showInputDialog("Return vehicle now? (Y/N)").equalsIgnoreCase("Y");

        // Save to MySQL
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO valet_logs (guest_name, license_plate, location, returned) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, plate);
            stmt.setString(3, location);
            stmt.setBoolean(4, returned);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, 
            "Valet logged successfully!\nGuest: " + name + "\nLocation: " + location + "\nReturned: " + (returned ? "Yes" : "No"));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to log valet: " + e.getMessage());
        }
    }
}
