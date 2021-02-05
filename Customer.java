package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Customer extends Accounts implements Serializable {
    static ArrayList<Customer> customers = new ArrayList<>();

    static ArrayList<Customer> getCustomers() {
        return customers;
    }

    private String teleNumber;
    private int bill;
    private Room roomType;
    int roomNumber;

    public Customer(String userName, String firstName, String lastName, String teleNumber, String password, int bill, Room roomType) {
        super(firstName, lastName, userName, password);
        this.teleNumber = teleNumber;
        this.bill = bill;
        this.roomType = roomType;
    }


    public Room getRoomType() {
        return roomType;
    }

    public void setRoomType(Room roomType) {
        this.roomType = roomType;
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
                " | roomType: " + roomType + " | ";
    }
}
