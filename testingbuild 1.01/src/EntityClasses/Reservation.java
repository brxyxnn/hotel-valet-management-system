package EntityClasses;

// EntityClass.Reservation.java
public class Reservation {
    private String hotelID;
    private String name;
    private String checkIn;
    private String checkOut;
    private String status;
    private String roomNumber; // New field

    public Reservation(String hotelID, String name, String checkIn, String checkOut, String status, String roomNumber) {
        this.hotelID = hotelID;
        this.name = name;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.roomNumber = roomNumber;
    }

    // Getters
    public String getHotelID() { return hotelID; }
    public String getName() { return name; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }
    public String getStatus() { return status; }
    public String getRoomNumber() { return roomNumber; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    @Override
    public String toString() {
        return hotelID + "," + name + "," + checkIn + "," + checkOut + "," + status + "," + roomNumber;
    }
}
