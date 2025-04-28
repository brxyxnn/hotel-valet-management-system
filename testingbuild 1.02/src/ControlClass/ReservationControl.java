package ControlClass; // Ensure package name matches your structure

import EntityClasses.Reservation;
import EntityClasses.ReservationStore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern; // Import Pattern for regex matching

public class ReservationControl {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    // Regex pattern for exactly 3 digits
    private static final Pattern THREE_DIGIT_PATTERN = Pattern.compile("^\\d{3}$");


    // Helper method to find a reservation by ID (reduces code duplication)
    private static Reservation findReservationById(String hotelID, List<Reservation> reservations) {
        for (Reservation res : reservations) {
            if (res.getHotelID().equals(hotelID)) {
                return res;
            }
        }
        return null;
    }

    // Book a reservation
    public static Reservation bookReservation(String name, String checkInStr, String checkOutStr) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkInStr, DATE_FORMATTER);
            LocalDate checkOutDate = LocalDate.parse(checkOutStr, DATE_FORMATTER);
            LocalDate today = LocalDate.now();

            if (checkInDate.isBefore(today)) {
                throw new IllegalArgumentException("Check-in date cannot be in the past.");
            }
            if (checkOutDate.isBefore(checkInDate)) {
                throw new IllegalArgumentException("Check-out date cannot be before check-in date.");
            }

            String hotelID = UUID.randomUUID().toString().substring(0, 8);
            // Room number is initially empty or null when booking
            Reservation res = new Reservation(hotelID, name, checkInStr, checkOutStr, "booked", null);
            ReservationStore.appendReservation(res); // Use append for new bookings
            return res;

        } catch (DateTimeParseException e) {
            System.err.println("Internal Error: Invalid date format received by ReservationControl: " + e.getMessage());
            throw new IllegalArgumentException("Invalid date format provided (MM/dd/yyyy).", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error during booking: " + e.getMessage());
            return null;
        }
    }

    // Check in a reservation
    public static String checkIn(String hotelID) {
        List<Reservation> reservations = ReservationStore.loadReservations();
        Reservation res = findReservationById(hotelID, reservations);
        LocalDate today = LocalDate.now();

        if (res == null) {
            System.out.println("Hotel ID not found.");
            return null;
        }

        if (!res.getStatus().equalsIgnoreCase("booked")) {
            System.out.println("Cannot check in reservation with status: " + res.getStatus());
            return null;
        }

        try {
            LocalDate checkInDate = LocalDate.parse(res.getCheckIn(), DATE_FORMATTER);
            if (today.isBefore(checkInDate)) {
                System.out.println("Cannot check in before the scheduled check-in date (" + res.getCheckIn() + ")");
                return null;
            }
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing check-in date during check-in process for ID " + hotelID);
            return null;
        }

        res.setStatus("checked-in");
        // Assign a room number using the generator *if* one isn't already assigned
        // The generator ensures a valid format but doesn't check uniqueness here
        if (res.getRoomNumber() == null || res.getRoomNumber().trim().isEmpty()) {
            // TODO: The generated room number should ideally also be checked for uniqueness
            // against currently checked-in guests before assignment.
            // For now, we rely on the receptionist potentially needing to update it later if there's a conflict.
            res.setRoomNumber(generateRoomNumber());
        }
        ReservationStore.saveReservations(reservations); // Inefficient
        return res.getRoomNumber();
    }

    // Update status (e.g., for check-out)
    public static boolean updateStatus(String hotelID, String newStatus) {
        List<Reservation> reservations = ReservationStore.loadReservations();
        Reservation res = findReservationById(hotelID, reservations);

        if (res == null) {
            System.out.println("Hotel ID not found.");
            return false;
        }

        if (newStatus.equalsIgnoreCase("checked-out")) {
            if (!res.getStatus().equalsIgnoreCase("checked-in")) {
                System.out.println("Cannot check out reservation with status: " + res.getStatus());
                return false;
            }
            // When checking out, we might want to clear the room number or handle it differently
            res.setRoomNumber(null); // Optional: Make room available immediately
        }

        res.setStatus(newStatus);
        ReservationStore.saveReservations(reservations); // Inefficient
        return true;
    }

    // Modify reservation dates (validation logic included)
    public static boolean modifyReservation(String hotelID, String newCheckInStr, String newCheckOutStr) {
        List<Reservation> reservations = ReservationStore.loadReservations();
        Reservation res = findReservationById(hotelID, reservations);

        if (res == null) {
            System.out.println("Hotel ID not found.");
            return false;
        }
        if (res.getStatus().equalsIgnoreCase("checked-out")) {
            System.out.println("Modification Error: Cannot modify a reservation that is already checked out.");
            return false;
        }

        LocalDate newCheckInDate;
        LocalDate newCheckOutDate;
        LocalDate existingCheckInDate;
        LocalDate today = LocalDate.now();

        try {
            newCheckInDate = LocalDate.parse(newCheckInStr, DATE_FORMATTER);
            newCheckOutDate = LocalDate.parse(newCheckOutStr, DATE_FORMATTER);
            existingCheckInDate = LocalDate.parse(res.getCheckIn(), DATE_FORMATTER);

            if (res.getStatus().equalsIgnoreCase("checked-in")) {
                if (!newCheckInDate.isEqual(existingCheckInDate)) {
                    System.out.println("Modification Error: Cannot change the check-in date for a guest who is already checked-in.");
                    return false;
                }
                if (newCheckOutDate.isBefore(existingCheckInDate)) {
                    System.out.println("Modification Error: New check-out date cannot be before the original check-in date.");
                    return false;
                }
            } else { // 'booked' status
                if (newCheckInDate.isBefore(today)) {
                    System.out.println("Modification Error: New check-in date cannot be in the past.");
                    return false;
                }
                if (newCheckOutDate.isBefore(newCheckInDate)) {
                    System.out.println("Modification Error: New check-out date cannot be before new check-in date.");
                    return false;
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Modification Error: Invalid date format provided (MM/dd/yyyy). " + e.getMessage());
            return false;
        }

        if (res.getStatus().equalsIgnoreCase("checked-in")) {
            res.setCheckOut(newCheckOutStr);
            System.out.println("Check-out date updated for checked-in guest.");
        } else {
            res.setCheckIn(newCheckInStr);
            res.setCheckOut(newCheckOutStr);
            System.out.println("Reservation dates updated.");
        }
        ReservationStore.saveReservations(reservations);
        return true;
    }


    // Cancel a reservation
    public static boolean cancelReservation(String hotelID) {
        List<Reservation> reservations = ReservationStore.loadReservations();
        Reservation resToRemove = findReservationById(hotelID, reservations); // Use helper

        if (resToRemove == null) {
            System.out.println("Hotel ID not found.");
            return false;
        }
        if (resToRemove.getStatus().equalsIgnoreCase("checked-in") || resToRemove.getStatus().equalsIgnoreCase("checked-out")) {
            System.out.println("Cannot cancel reservation with status: " + resToRemove.getStatus() + ". Please check out first if applicable.");
            return false;
        }

        reservations.remove(resToRemove);
        ReservationStore.saveReservations(reservations);
        return true;
    }

    /**
     * Updates the room number for a specific reservation with validation.
     * - Ensures the room number is exactly 3 digits.
     * - Ensures the room number is not already assigned to another currently 'checked-in' guest.
     *
     * @param hotelID       The ID of the reservation to update.
     * @param newRoomNumber The proposed new room number (must be 3 digits).
     * @return true if the room number was updated successfully, false otherwise.
     */
    public static boolean updateRoomNumber(String hotelID, String newRoomNumber) {
        // 1. Validate Format (must be exactly 3 digits)
        if (newRoomNumber == null || !THREE_DIGIT_PATTERN.matcher(newRoomNumber).matches()) {
            System.out.println("Validation Error: Room number must be exactly 3 digits.");
            return false;
        }

        List<Reservation> reservations = ReservationStore.loadReservations();
        Reservation targetReservation = findReservationById(hotelID, reservations);

        if (targetReservation == null) {
            System.out.println("Hotel ID not found.");
            return false;
        }

        // Optional: Add status check? Only allow assigning room if 'checked-in' or 'booked'?
         if (!targetReservation.getStatus().equalsIgnoreCase("checked-in") && !targetReservation.getStatus().equalsIgnoreCase("booked")) {
             System.out.println("Can only assign room number to booked or checked-in guests.");
             return false;
        }

        // 2. Validate Uniqueness (against other *checked-in* guests)
        for (Reservation otherRes : reservations) {
            // Skip the reservation we are currently updating
            if (otherRes.getHotelID().equals(hotelID)) {
                continue;
            }
            // Check if another *checked-in* guest has the same room number
            if (otherRes.getStatus().equalsIgnoreCase("checked-in") &&
                    newRoomNumber.equals(otherRes.getRoomNumber())) {
                System.out.println("Validation Error: Room number " + newRoomNumber +
                        " is already occupied by another guest");
                return false;
            }
        }

        // If all validations pass, update the room number
        targetReservation.setRoomNumber(newRoomNumber);
        ReservationStore.saveReservations(reservations); // Inefficient
        System.out.println("Room number updated successfully for Hotel ID: " + hotelID); // Added confirmation
        return true;
    }

    // Helper to generate a random room number (already generates 3 digits)
    private static String generateRoomNumber() {
        Random rand = new Random();
        int roomNum = rand.nextInt(900) + 100; // Generates 100-999
        return String.valueOf(roomNum);
        // NOTE: This generated number is NOT checked for uniqueness automatically upon generation.
    }
}
