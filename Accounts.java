package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class Accounts implements Serializable {

    static ArrayList<Accounts> accounts = new ArrayList<>();
    static ArrayList<Accounts> getAccounts() {
        return accounts;
    }
    private String firstName;
    private String lastName;
    private String userName;
    private String password;

    public Accounts(String firstName, String lastName, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "Name='" + firstName + " " +lastName+ '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
