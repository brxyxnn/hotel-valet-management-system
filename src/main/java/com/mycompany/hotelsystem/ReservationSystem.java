package com.mycompany.hotelsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationSystem {

    private static final String URL = "jdbc:mysql://localhost:3306/hotelsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "test";

    private List<Reservation> reservations = new ArrayList<>();
    private int reservationIDCounter = 1;

    // MySQL-based availability check
    public boolean checkRoomAvailability(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = """
                SELECT COUNT(*) FROM reservations
                WHERE room_number = ?
                  AND check_in < ?
                  AND check_out > ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, roomNumber);
            stmt.setDate(2, java.sql.Date.valueOf(checkOut));
            stmt.setDate(3, java.sql.Date.valueOf(checkIn));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // true if no overlap
            }
        } catch (SQLException e) {
            System.out.println("Failed to check room availability: " + e.getMessage());
        }
        return false;
    }

    public void createReservation(int guestID, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        int newID = reservationIDCounter++;
        Reservation r = new Reservation(newID, guestID, roomNumber, checkIn, checkOut);
        reservations.add(r);

        // Save to MySQL
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO reservations (guest_id, room_number, check_in, check_out) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, guestID);
            stmt.setInt(2, roomNumber);
            stmt.setDate(3, java.sql.Date.valueOf(checkIn));
            stmt.setDate(4, java.sql.Date.valueOf(checkOut));
            stmt.executeUpdate();

            System.out.println("Reservation inserted into the MySQL database.");
        } catch (SQLException e) {
            System.out.println("Failed to insert reservation: " + e.getMessage());
        }
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public Reservation getReservationDetails(int reservationID) {
        for (Reservation r : reservations) {
            if (r.getReservationID() == reservationID) return r;
        }
        return null;
    }

    public void updateReservation(int reservationID, LocalDate newCheckIn, LocalDate newCheckOut) {
        Reservation r = getReservationDetails(reservationID);
        if (r != null) {
            r.setCheckInDate(newCheckIn);
            r.setCheckOutDate(newCheckOut);
            r.setReservationStatus("Modified");
            System.out.println("Reservation updated.");
        } else {
            System.out.println("Reservation not found.");
        }
    }

    public void cancelReservation(int reservationID) {
        Reservation r = getReservationDetails(reservationID);
        if (r != null) {
            reservations.remove(r);
            System.out.println("Reservation cancelled.");
        } else {
            System.out.println("Reservation not found.");
        }
    }
}
