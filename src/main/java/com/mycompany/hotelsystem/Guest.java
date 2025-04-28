package com.mycompany.hotelsystem;



public class Guest {
    private int guestId;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;
    private String paymentCardNum;

    public void setGuestId(int id) { this.guestId = id; }
    public int getGuestId() { return guestId; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setAddress(String address) { this.address = address; }
    public String getAddress() { return address; }

    public void setPhoneNumber(String number) { this.phoneNumber = number; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public void setDateOfBirth(String dob) { this.dateOfBirth = dob; }
    public String getDateOfBirth() { return dateOfBirth; }

    public void setPaymentCardNum(String cardNum) { this.paymentCardNum = cardNum; }
    public String getPaymentCardNum() { return paymentCardNum; }
}
