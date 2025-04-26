package ControlClass;// ReservationController.java
import EntityClasses.Reservation;
import EntityClasses.ReservationStore;

import java.util.*;
import java.util.Random;

public class ReservationControl {

    // Book a reservation (initially, room number is empty)
    public static Reservation bookReservation(String name, String checkIn, String checkOut) {
        String hotelID = UUID.randomUUID().toString().substring(0, 8);
        Reservation res = new Reservation(hotelID, name, checkIn, checkOut, "booked", "");
        ReservationStore.appendReservation(res);
        return res;
    }

    // Check in a reservation: update status to "checked-in" and assign a room number
    public static String checkIn(String hotelID) {
        List<Reservation> reservations = ReservationStore.loadReservations();
        for (Reservation res : reservations) {
            if (res.getHotelID().equals(hotelID)) {
                res.setStatus("checked-in");
                // Assign a room number if none is already assigned
                if (res.getRoomNumber() == null || res.getRoomNumber().trim().isEmpty()) {
                    res.setRoomNumber(generateRoomNumber());
                }
                ReservationStore.saveReservations(reservations);
                return res.getRoomNumber();
            }
        }
        return null;
    }

    // Update status (for check-out, for example)
    public static boolean updateStatus(String hotelID, String newStatus) {
        List<Reservation> reservations = ReservationStore.loadReservations();
        boolean found = false;
        for (Reservation res : reservations) {
            if (res.getHotelID().equals(hotelID)) {
                res.setStatus(newStatus);
                found = true;
                break;
            }
        }
        if (found) {
            ReservationStore.saveReservations(reservations);
        }
        return found;
    }

    // Modify reservation dates
    public static boolean modifyReservation(String hotelID, String newCheckIn, String newCheckOut) {
        List<Reservation> reservations = ReservationStore.loadReservations();
        boolean found = false;
        for (Reservation res : reservations) {
            if (res.getHotelID().equals(hotelID)) {
                res.setCheckIn(newCheckIn);
                res.setCheckOut(newCheckOut);
                found = true;
                break;
            }
        }
        if (found) {
            ReservationStore.saveReservations(reservations);
        }
        return found;
    }

    // Cancel a reservation
    public static boolean cancelReservation(String hotelID) {
        List<Reservation> reservations = ReservationStore.loadReservations();
        boolean found = false;
        Iterator<Reservation> it = reservations.iterator();
        while (it.hasNext()) {
            Reservation res = it.next();
            if (res.getHotelID().equals(hotelID)) {
                it.remove();
                found = true;
                break;
            }
        }
        if (found) {
            ReservationStore.saveReservations(reservations);
        }
        return found;
    }

    // Update room number specifically (for receptionist use)
    public static boolean updateRoomNumber(String hotelID, String newRoomNumber) {
        List<Reservation> reservations = ReservationStore.loadReservations();
        boolean found = false;
        for (Reservation res : reservations) {
            if (res.getHotelID().equals(hotelID)) {
                res.setRoomNumber(newRoomNumber);
                found = true;
                break;
            }
        }
        if (found) {
            ReservationStore.saveReservations(reservations);
        }
        return found;
    }

    // Helper to generate a random room number (e.g., between 100 and 999)
    private static String generateRoomNumber() {
        Random rand = new Random();
        int roomNum = rand.nextInt(900) + 100;
        return String.valueOf(roomNum);
    }
}