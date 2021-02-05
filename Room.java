package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Room implements java.io.Serializable {
    private boolean booked;
    private String typeOfRoom;
    private int price;
    private int roomNumber;


    public Room(int roomNumber, String typeOfRoom, boolean booked, int price) {
        this.typeOfRoom = typeOfRoom;
        this.booked = booked;
        this.price = price;
        this.roomNumber = roomNumber;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isBooked() {
        if (booked) {
            System.out.println("this room is booked");
            return true;
        } else {
            return false;
        }
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public String getTypeOfRoom() {
        return typeOfRoom;
    }

    public void setTypeOfRoom(String typeOfRoom) {
        this.typeOfRoom = typeOfRoom;
    }

    @Override
    public String toString() {
        return "Room{" +
                "booked=" + booked +
                ", typeOfRoom='" + typeOfRoom + '\'' +
                ", price=" + price +
                '}';
    }
}

