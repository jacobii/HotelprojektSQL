package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Database {
    private static String pass;
    private static String user;
    private static String url;
    private static Statement sqlStatement = null;
    private static Connection connection = null;
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        try {
            dbConnect(login());
            menu();
        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    protected static void dbConnect(String pass) throws SQLException {
        connection = DriverManager.getConnection(url, user, pass);
        System.out.println("Anslutningen lyckades!");
        sqlStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    protected static String login() throws SQLException {
        try {
            File myObj = new File("login.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                url = myReader.nextLine();
                user = myReader.nextLine();
                pass = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return pass;
    }

    private static void menu() throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println("---------------- Admin-panel ----------------");
            System.out.println("1. Find Available rooms");
            System.out.println("2. Book a room");
            System.out.println("3. Show customers");
            System.out.println("4. Exit");
            int choice = Main.intInput();
            switch (choice) {
                case 1 -> availableRooms();
                case 2 -> bookAroom();
                case 3 -> allCustomers();

                default -> exit = true;
            }
        }
    }

    protected static void availableRooms() throws SQLException {
        {
            String go = "SELECT * FROM reservationNotBooked;";
            ListAll(go);
        }
    }

    protected static void customerInfo() throws SQLException {
        {
            String go = "SELECT * FROM customerInfoCheckedIn WHERE userName ='" + Customer.getCustomers().get(0).getUserName() + "';";
            ListAll(go);
            System.out.println("\nYour current bill is: " + Customer.getCustomers().get(0).getBill() + ":-");
        }
    }

    private static void allCustomers() throws SQLException {
        {
            String go = "SELECT * FROM customer;";
            ListAll(go);
        }
    }

    protected static void ListAll(String go) throws SQLException {
        ResultSet result = sqlStatement.executeQuery(go);
        int columnCount = result.getMetaData().getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = result.getMetaData().getColumnName(i + 1);
        }
        // skriver ut
        for (String columnName : columnNames) {
            System.out.print(PadRight(columnName));
        }
        while (result.next()) { // next = loopar så länge det finns data kvar.
            System.out.println();
            for (String columnName : columnNames) {
                String value = result.getString(columnName);
                if (value == null)
                    value = "Saknas";
                System.out.print(PadRight(value));
            }
        }
    }

    protected static void seeItems() throws SQLException {
        {
            String go = "select * from items;";
            ListAll(go);
        }
    }

    private static String PadRight(String string) {
        int totalStringLength = 30;
        int charsToPadd = totalStringLength - string.length();
        if (string.length() >= totalStringLength)
            return string;
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < charsToPadd; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    protected static void bookAroom() throws SQLException {

        //bookRoom(id, roomChoice, days);
    }

    protected static void bookRoom(String userName, int roomChoice, int days) throws SQLException {
        PreparedStatement statement = sqlStatement.getConnection().prepareStatement("INSERT INTO reservation (userName, room_nmbr, amount_days,check_in) VALUES(?,?,?,CURRENT_timestamp);");
        statement.setString(1, userName);
        statement.setInt(2, roomChoice);
        statement.setInt(3, days);
        int i = statement.executeUpdate();
    }

    protected static void checkOut(String userName) throws SQLException {
        PreparedStatement statement = sqlStatement.getConnection().prepareStatement("UPDATE reservation SET check_out = CURRENT_timestamp, booked = false WHERE userName=? AND booked = true;");
        statement.setString(1, userName);
        int i = statement.executeUpdate();
        executeSql(i);
    }

    protected static void executeSql(int i) {
        if (i == 1) {
            System.out.println("Successful!");
        } else {
            System.out.println("Failed, please check that you typed in correct information");
        }
    }

    protected static int valueDBLookUp(String go1, String searchValue) throws SQLException {
        ResultSet result = sqlStatement.executeQuery(go1);
        int value = 0;
        while (result.next()) {
            if (result.isFirst()) {
                String stringValue = result.getString(searchValue);
                value = Integer.parseInt(stringValue);
            }
        }
        return value;
    }
    protected static String valueSDb(String go1, String searchValue) throws SQLException {
        ResultSet result = sqlStatement.executeQuery(go1);
        String value = null;
        while (result.next()) {
            if (result.isFirst()) {
                value = (result.getString(searchValue));
            }
        }
        return value;
    }
    public static void updateInfo() throws SQLException {
        String searchValue = "room_name";
        String go1 = "SELECT * FROM reservationBooked where userName='"+Customer.getCustomers().get(0).getUserName()+"';";
        String updateString = valueSDb(go1, searchValue);
        Customer.getCustomers().get(0).setRoomType(updateString);
        searchValue = "room_nmbr";
        int updateInt = valueDBLookUp(go1, searchValue);
        Customer.getCustomers().get(0).setRoomNumber(updateInt);


       // searchValue = "item";
       // go1 = "SELECT * FROM customerBoughtCheckedIn where customer.userName='"+Customer.getCustomers().get(0).getUserName()+"'GROUP BY item;";
        //updateString = valueSDb(go1, searchValue);
        //System.out.println(updateString);
        //Customer.getCustomers().get(0).setRoomType(updateString);
       //Customer.getCustomers().get(0).setRoomType(update);
    }

    protected static void checkBefore(String userName, int roomChoice, int days) throws SQLException {
        String searchValue = "room_nmbr";
        String go2 = "SELECT * FROM checkIn WHERE userName ='"+userName+"'";
        ResultSet result = sqlStatement.executeQuery(go2);
        if (!result.isBeforeFirst()) {
            String go1 = "SELECT * FROM allRooms WHERE room_nmbr = " + roomChoice + " AND booked IS NOT true;";
            int booked = valueDBLookUp(go1, searchValue);
            if (booked == 1) {
                System.out.println("\nSorry, this room is already booked. Please choose another one.");
            } else {
                System.out.println("\nWelcome to our hotel, you are now checked in! Your room-number is: " + roomChoice);
                bookRoom(userName,roomChoice, days);
                searchValue = "price";
                go1 = "SELECT * from checkPrice WHERE room_nmbr = " + roomChoice + ";";
                int price = Database.valueDBLookUp(go1, searchValue);
                int totalPrice = price * days;
                Main.updateBill(totalPrice);
            }
        }else {
            System.out.println("\nYou already have a room. If you want to get another room, you have to check out.");// data exist
        }
    }

    protected static void createCust(String table, String userName, String firstName, String lastName, String telephone, String password) throws SQLException {
        //table ="customer";
        firstName = firstName.substring(0, Math.min(firstName.length(), 25));
        lastName = lastName.substring(0, Math.min(lastName.length(), 25));
        userName = userName.substring(0, Math.min(userName.length(), 25));
        PreparedStatement statement = sqlStatement.getConnection().prepareStatement("INSERT INTO customer (userName,firstName, lastName, telephone, password) VALUES(?,?,?,?,?);");
        statement.setString(1, userName);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, telephone);
        statement.setString(5, password);
        int i = statement.executeUpdate();
        executeSql(i);
    }

    protected static void buyFood(int item_id, int amount) throws SQLException {
        //table ="customer";
        //firstName = firstName.substring(0, Math.min(firstName.length(), 25));
        // lastName = lastName.substring(0, Math.min(lastName.length(), 25));
        PreparedStatement statement = sqlStatement.getConnection().prepareStatement("INSERT INTO bought (item_id, amount,userName) VALUES(?,?,?);");
        statement.setInt(1, item_id);
        statement.setInt(2, amount);
        statement.setString(3, Customer.getCustomers().get(0).getUserName());

        int i = statement.executeUpdate();
        executeSql(i);
    }


    protected static int check(String sql, int check) throws SQLException {
        PreparedStatement statement = sqlStatement.getConnection().prepareStatement(sql);
        statement.setInt(1,check);
        ResultSet rs = statement.executeQuery();
        int n = 0;
        if (rs.next() ) {
            n = rs.getInt(1);
        }
        else{
            System.out.println("Error!");
        }
        return n;
    }
}
