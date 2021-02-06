package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Customer extends Accounts implements Serializable {
    static ArrayList<Customer> customers = new ArrayList<>();

    static ArrayList<Customer> getCustomers() {
        return customers;
    }

    private String teleNumber;
    private int bill;
    private String roomType;
    private int roomNumber;
    private String accountType;

    public Customer(String userName, String firstName, String lastName, String teleNumber, String password, int bill, String roomType, int roomNumber, int accountType) {
        super(firstName, lastName, userName, password, accountType);
        this.teleNumber = teleNumber;
        this.bill = bill;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
    }


    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTeleNumber() {
        return teleNumber;
    }

    public void setTeleNumber(String teleNumber) {
        this.teleNumber = teleNumber;
    }


    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {

        return "Customer: | " +
                "Name: " + getFirstName() + " " + getLastName() +
                " | Username: " + getUserName() +
                " | teleNumber: '" + teleNumber + '\'' +
                " | bill: " + bill +
                " | roomNumber: " + roomNumber +
                " | roomType: " + roomType + " | ";
    }
}
