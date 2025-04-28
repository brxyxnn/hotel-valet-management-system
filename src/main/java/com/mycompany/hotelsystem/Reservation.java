package com.mycompany.hotelsystem;

import java.time.LocalDate;

public class Reservation {
    private int reservationID;
    private int guestID;
    private int roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String reservationStatus;
    private double totalPrice;

    public Reservation(int reservationID, int guestID, int roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationID = reservationID;
        this.guestID = guestID;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = 0;
        this.reservationStatus = "Booked";
    }

    public int getReservationID() { return reservationID; }
    public int getGuestID() { return guestID; }
    public int getRoomNumber() { return roomNumber; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public double getTotalPrice() { return totalPrice; }
    public String getReservationStatus() { return reservationStatus; }

    public void setCheckInDate(LocalDate d) { this.checkInDate = d; }
    public void setCheckOutDate(LocalDate d) { this.checkOutDate = d; }
    public void setReservationStatus(String status) { this.reservationStatus = status; }
    public void setTotalPrice(double price) { this.totalPrice = price; }
}
