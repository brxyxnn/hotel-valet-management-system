package com.mycompany.hotelsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testing {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/hotelsystem"; // schema name
        String user = "root"; // schema username
        String password = "test"; // schema password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // ensures the JDBC driver is loaded
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println(" Connected to MySQL successfully!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println(" JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Failed to connect: " + e.getMessage());
        }
    }
}
