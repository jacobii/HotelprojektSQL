package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class Receptionist extends Accounts implements Serializable {

    static ArrayList<Receptionist> account = new ArrayList<>();
    static ArrayList<Receptionist> getAccount() {
        return account;
    }

    public Receptionist(String firstName, String lastName, String userName, String password, int accountType) {
        super(firstName, lastName, userName, password, accountType);
    }

    @Override
    public String toString() {
        return "Receptionist{} " + super.toString();
    }
}
