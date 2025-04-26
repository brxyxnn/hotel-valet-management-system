package ControlClass;

import java.sql.*;

public class AuthenticationControl {
    public static boolean authenticateReceptionist(String username, String password) {
        String sql = "SELECT * FROM receptionists WHERE username = ? AND password = ?";

        try (Connection conn = ControlClass.DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if a record was found
            }
        } catch (SQLException e) {
            System.out.println("Database error during authentication: " + e.getMessage());
            return false;
        }
    }
}