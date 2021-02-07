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
                    out.close();
                    fileOut.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        saveRec();
    }

    public static void readFile() {
        Customer.customers.removeAll(Customer.getCustomers());
        Accounts.account().removeAll(Accounts.account());
        Receptionist.getAccount().removeAll(Receptionist.getAccount());
        Customer c = null;
        try {
            try (FileInputStream fileIn = new FileInputStream("customers.ser"); ObjectInput in = new ObjectInputStream(fileIn)) {
                while (true) {
                    c = (Customer) in.readObject();
                    Customer.customers.add(c);
                    Accounts.account().add(c);
                }
            }
        } catch (IOException i) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        readRec();
    }

    public static void saveRec() {
        try {
            try (FileOutputStream fileOut = new FileOutputStream("rec.ser")) {
                try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                    for (Receptionist rec : Receptionist.getAccount()) {
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
    public static void readRec() {
        Receptionist rec = null;
        try {
            try (FileInputStream fileIn = new FileInputStream("rec.ser"); ObjectInput in = new ObjectInputStream(fileIn)) {
                while (true) {
                    rec = (Receptionist) in.readObject();
                    Receptionist.getAccount().add(rec);
                    Accounts.account().add(rec);
                }
            }
        } catch (IOException i) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
