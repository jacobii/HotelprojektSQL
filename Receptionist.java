package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class Receptionist extends Accounts implements Serializable {

    static ArrayList<Receptionist> receptionists = new ArrayList<>();

    static ArrayList<Receptionist> getReceptionists() {
        return receptionists;
    }

    public Receptionist(String firstName, String lastName, String userName, String password) {
        super(firstName, lastName, userName, password);
    }

    @Override
    public String toString() {
        return "Receptionist{} " + super.toString();
    }
}
