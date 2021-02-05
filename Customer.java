package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Customer extends Accounts implements Serializable{
   // ArrayList numRooms = new ArrayList<>();
    static ArrayList<Customer> customers = new ArrayList<>();
    ArrayList boughtFood = new ArrayList<>();




    // Add keys and values (Country, City)



    // static ArrayList<Room> newRoom = new ArrayList<>();

     ArrayList getBoughtFood() {
        return boughtFood;
    }

    static ArrayList<Customer> getCustomers() {
        return customers;
    }
     //ArrayList<Room> getNewRoom() { return newRoom; }


    private String teleNumber;
    private int bill;
    private Room roomType;
    int roomNumber;

    public Customer(String firstName, String lastName, String userName, String password, String teleNumber, int bill, Room roomType) {
        super(firstName, lastName, userName, password);
        this.teleNumber = teleNumber;
        this.bill = bill;
        this.roomType = roomType;
        // this.roomNumber = roomNumber;
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
                "Name: " + getFirstName() +" "+ getLastName() +
                " | Username: " + getUserName() +
                " | teleNumber: '" + teleNumber + '\'' +
                " | bill: " + bill +
                " | roomType: " + roomType + " | ";
    }




}
