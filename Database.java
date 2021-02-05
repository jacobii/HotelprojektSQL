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
    public static Scanner input = new Scanner(System.in);
    private static Statement sqlStatement = null;
    private static Connection connection = null;
    public static void main(String[] args) throws IOException {
    // Connect to MySQL, samt metoderna för CRUD.

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
        //try (Connection connection = DriverManager.getConnection(url, user, pass)) {
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

    private static void menu() throws SQLException{
        boolean exit = false;
        while (!exit) {
            System.out.println("---------------- Admin-panel ----------------");
            System.out.println("1. Find Available rooms");
            System.out.println("2. Book a room");
            System.out.println("3. Create Customer");
            System.out.println("4. Statistik");
            System.out.println("5. Avsluta");
            int choice = Main.intInput();
            switch (choice) {
                case 1 -> availableRooms();
                case 2 -> bookAroom();

                default -> exit = true;
            }
        }
    }
    private static void availableRooms() throws SQLException{
        {
            String go = "SELECT * FROM reservationNotBooked;";
            ListAll(go);
        }
    }
    protected static void ListAll(String go) throws SQLException {
        // Utför query mot databas
        // String go = "SELECT * FROM course;";
        ResultSet result = sqlStatement.executeQuery(go);
        // hämtar antal kollimner
        int columnCount = result.getMetaData().getColumnCount();
        // hämta alla kollumnamn
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = result.getMetaData().getColumnName(i + 1);
        }
        // skriver ut
        for (String columnName : columnNames) {
            System.out.print(PadRight(columnName));
        }
        while(result.next()) { // next = loopar så länge det finns data kvar.
            System.out.println();
            for (String columnName : columnNames) {
                String value = result.getString(columnName);
                if(value == null)
                    value = "Saknas";
                System.out.print(PadRight(value));
            }
        }
    }
    private static String PadRight(String string) {
        int totalStringLength = 30;
        int charsToPadd = totalStringLength - string.length();
        // incase the string is the same length or longer than our maximum lenght
        if(string.length() >= totalStringLength)
            return string;
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < charsToPadd; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
    protected static void bookAroom() throws SQLException {
        int id = 5;
        availableRooms();
        System.out.println("Choose a room-number that is available.");
        int roomChoice = Main.intInput();
        System.out.println("Amount of days: ");
        int days = Main.intInput();
        checkBefore(id, roomChoice,days);
        //bookRoom(id, roomChoice, days);
}
    protected static void bookRoom(int id, int roomChoice, int days) throws SQLException {
        PreparedStatement statement = sqlStatement.getConnection().prepareStatement("INSERT INTO reservation (customer_id, room_nmbr,amount_days,check_in) VALUES(?,?,?,CURRENT_timestamp);");
        statement.setInt(1,id);
        statement.setInt(2,roomChoice);
        statement.setInt(3,days);
        int i = statement.executeUpdate();
    }
    protected static void executeSql(int i) {
        if (i == 1) {
            System.out.println("Lyckades!");
        } else {
            System.out.println("Misslyckades, dubbelkolla att du angivit korrekt id");
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
    protected static void checkBefore(int id, int roomChoice,int days) throws SQLException {
        String searchValue = "room_nmbr";
        String go2 = "SELECT * FROM allRooms WHERE customer_id ="+id+";"; // ifal denna kund redan lånat denna bok.
        ResultSet result = sqlStatement.executeQuery(go2);

        if(!result.isBeforeFirst()){
            String go1 = "SELECT * FROM allRooms WHERE room_nmbr = "+roomChoice+" AND booked IS NOT true;";
            int booked = valueDBLookUp(go1, searchValue);
            if(booked <= 1) {
                System.out.println("Sorry, this room is already booked. Please choose another one.");

            }
            else {
                System.out.println("Welcome to our hotel, you are not checked in! Your room-number is: "+roomChoice);
                //String sqlExecute = "INSERT INTO rental (book_id, customer_id, rent_date) VALUES(?,?,CURRENT_timestamp);";
                bookRoom(id, roomChoice, days);
            }
        }
        else{
            System.out.println("You already have a room. Ff you want to get another room, you have to check out.");// data exist
        }

    }
    protected static void createCust(String table, String firstName, String lastName,String userName, String password, String telephone) throws SQLException {
        //table ="customer";
        //firstName = firstName.substring(0, Math.min(firstName.length(), 25));
       // lastName = lastName.substring(0, Math.min(lastName.length(), 25));
        PreparedStatement statement = sqlStatement.getConnection().prepareStatement("INSERT INTO customer (firstName, lastName, telephone, userName, password) VALUES(?,?,?,?,?);");
        statement.setString(1,firstName);
        statement.setString(2,lastName);
        statement.setString(3,telephone);
        statement.setString(4,userName);
        statement.setString(5,password);
        int i = statement.executeUpdate();
        executeSql(i);
    }
}
