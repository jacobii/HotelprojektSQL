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

    public static void saveFile2() {
        try {
            try (FileOutputStream fileOut = new FileOutputStream("rec.ser")) {
                try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                    for (Receptionist rec : Receptionist.getReceptionists()) {
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

    public static void readFile2() {
        Receptionist.getReceptionists().removeAll(Receptionist.getReceptionists());
        Receptionist r = null;
        try {
            try (FileInputStream fileIn = new FileInputStream("rec.ser"); ObjectInput in = new ObjectInputStream(fileIn)) {
                while (true) {
                    r = (Receptionist) in.readObject();
                    Receptionist.getReceptionists().add(r);
                }
            }
        } catch (IOException i) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
