##################
# Hotel & Valet Management System

A Java-based hotel reservation and valet service management system built for university coursework.  
The system uses a **console-based interface** and stores data in a **MySQL relational database**.

---

## Features

- Register guests
- Make room reservations with check-in and check-out dates
- Check real-time room availability using MySQL
- Log valet service requests with license plate and location
- Store all data (guests, reservations, valet logs) in a MySQL database
- Modular design with Java classes for guest, admin, receptionist, valet, and reservation systems

---

## How to Run the project for local set up

###  1. Install MySQL if you don't have it

- Download: [https://dev.mysql.com/downloads/installer](https://dev.mysql.com/downloads/installer)
- Install MySQL Community Server.
- During installation, set a root password when prompted
  -This project assumes the username and password are: root / test
    - alternatively you could change the user and password part of the code to match what you already use on your own MySQL connection

---

### 2. Create the Database & Tables

Open **MySQL Workbench** and copy, paste, and run the following:

CREATE DATABASE hotelsystem;
USE hotelsystem;

CREATE TABLE guests (
    id INT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guest_id INT,
    room_number INT,
    check_in DATE,
    check_out DATE,
    FOREIGN KEY (guest_id) REFERENCES guests(id)
);

CREATE TABLE valet_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(100),
    license_plate VARCHAR(10),
    location VARCHAR(50),
    returned BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
