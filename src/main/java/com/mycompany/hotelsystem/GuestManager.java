package com.mycompany.hotelsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class GuestManager {

    private static final String URL = "jdbc:mysql://localhost:3306/hotelsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "test";

    CheckInSystem ci = new CheckInSystem();
    CheckOutSystem co = new CheckOutSystem();
    ValetSystem valet = new ValetSystem();

    public void checkIn() {
        ci.processCheckIn();
    }

    public void checkOut() {
        co.processCheckOut();
    }

    public void valetRequest() {
        valet.processValetRequest();
    }

    public void addGuest(Guest guest) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO guests (id, name) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, guest.getGuestId());
            stmt.setString(2, guest.getName());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Guest inserted into the database!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to insert guest: " + e.getMessage());
        }
    }
}
