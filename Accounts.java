package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class Accounts implements Serializable {

    static ArrayList<Accounts> account = new ArrayList<>();
    static ArrayList<Accounts> account() {
        return account;
    }
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private int accountType;


    public Accounts(String firstName, String lastName, String userName, String password, int accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.accountType = accountType;
    }


    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
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
