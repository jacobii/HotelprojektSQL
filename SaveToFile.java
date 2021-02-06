package com.company;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveToFile {
    public static void saveFile() {
        try {
            try (FileOutputStream fileOut = new FileOutputStream("customers.ser")) {
                try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                    for (Customer cust : Customer.getCustomers()) {
                        out.writeObject(cust);
                    }
                    for (Receptionist rec: Receptionist.getAccount()) {
                        out.writeObject(rec);
                    }
                    out.close();
                    fileOut.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void readFile() {
        Customer.customers.removeAll(Customer.getCustomers());
        Customer c = null;
        try {
            try (FileInputStream fileIn = new FileInputStream("customers.ser"); ObjectInput in = new ObjectInputStream(fileIn)) {
                while (true) {
                    c = (Customer) in.readObject();
                    Customer.customers.add(c);
                }
            }
        } catch (IOException i) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveAcc() {
        try {
            try (FileOutputStream fileOut = new FileOutputStream("accounts.ser")) {
                try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                    for (Accounts acc : Accounts.account()) {
                        out.writeObject(acc);
                    }
                    out.close();
                    fileOut.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void readAcc() {
        //Receptionist.getReceptionists().removeAll(Receptionist.getReceptionists());
        Accounts.account().removeAll(Accounts.account());
        Accounts acc = null;
        try {
            try (FileInputStream fileIn = new FileInputStream("accounts.ser"); ObjectInput in = new ObjectInputStream(fileIn)) {
                while (true) {
                    acc = (Accounts) in.readObject();
                   Accounts.account().add(acc);
                }
            }
        } catch (IOException i) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
