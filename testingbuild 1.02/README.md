# Hotel Management System - Database Setup Guide

## PREREQUISITES
- MySQL Server installed 
- MySQL Workbench 
- Java JDBC connector file

## INSTALLATION STEPS

1. DOWNLOAD MYSQL CONNECTOR/J
    - Get the latest version from: https://dev.mysql.com/downloads/connector/j/
    - Choose "Platform Independent" version
    - Download the .zip archive
    - Extract the .zip file
      Locate the .jar file (e.g., mysql-connector-java-8.0.28.jar)
    - For Eclipse/IntelliJ:
      Right-click project → Build Path → Configure Build Path
      Click "Add External JARs" and select the connector file

2. SET UP DATABASE
   Run these commands in MySQL:

   ```sql
   CREATE DATABASE IF NOT EXISTS hotel_system;
   USE hotel_system;

   CREATE TABLE IF NOT EXISTS admins (
       username VARCHAR(50) PRIMARY KEY,
       password VARCHAR(50) NOT NULL
   );

   CREATE TABLE IF NOT EXISTS reservations (
       hotelID VARCHAR(8) PRIMARY KEY,
       name VARCHAR(100) NOT NULL,
       checkIn VARCHAR(10) NOT NULL,
       checkOut VARCHAR(10) NOT NULL,
       status VARCHAR(20) NOT NULL,
       roomNumber VARCHAR(10)
   );

   CREATE TABLE IF NOT EXISTS valet_parking (
       valetID VARCHAR(8) PRIMARY KEY,
       hotelID VARCHAR(8) NOT NULL,
       licensePlate VARCHAR(15) NOT NULL,
       parkingSpot VARCHAR(10) NOT NULL,
       checkInTime DATETIME DEFAULT CURRENT_TIMESTAMP,
       checkOutTime DATETIME NULL,
       FOREIGN KEY (hotelID) REFERENCES reservations(hotelID)
       ON DELETE CASCADE
       ON UPDATE CASCADE
   );

   CREATE TABLE IF NOT EXISTS receptionists (
       username VARCHAR(50) PRIMARY KEY,
       password VARCHAR(50) NOT NULL
   );

   INSERT IGNORE INTO admins VALUES ('admin', 'admin123');
   INSERT IGNORE INTO receptionists VALUES ('reception', 'password123');
   
CONFIGURE APPLICATION
Update DatabaseManager.java with your credentials:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_system";
private static final String DB_USER = "your_username";
private static final String DB_PASSWORD = "your_password";