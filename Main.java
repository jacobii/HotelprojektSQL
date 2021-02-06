package com.company;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class Main {
    public static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        SaveToFile.readFile();
        SaveToFile.readFile2();
        try {
            Database.dbConnect(Database.login());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //createRooms();
        try {
            custOrRep();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected static void custOrRep() throws SQLException {
        boolean exit = false;
        while (!exit) {

            System.out.println("1. Log in as [Customer]");
            System.out.println("2. Log in as [Receptionist]");
            System.out.println("3. Exit");
            int choice = intInput();

            switch (choice) {
                case 1 -> loginCust();
                case 2 -> loginRec();
                default -> exit = true;
            }
        }
    }

    public static void loginCust() throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println(Customer.getCustomers());
            System.out.println("Are you already a registered customer at this hotel?");
            System.out.println("1. Log in as a current customer");
            System.out.println("2. Create an account");
            System.out.println("3. Go back to previous menu");
            int choice = intInput();
            switch (choice) {
                case 1 -> existingCust();
                case 2 -> createCust();
                default -> exit = true;
            }
        }
    }

    public static void existingCust() throws SQLException {
        boolean exit = false;
        final boolean[] found = {false};
        do {
            Customer.getCustomers().sort(Comparator.comparing(Customer::getUserName));
            Customer.getCustomers().forEach(System.out::println);
            System.out.println("\n1. Login as an existing customer");
            System.out.println("0. Back to previous menu");
            int choice = intInput();
            if (choice == 1) {
                System.out.println("Type in your username: ");
                String userName = sc.nextLine();
                System.out.println("Type in your password: ");
                String password = sc.nextLine();
                Customer.getCustomers().forEach((customer) -> {
                    if (userName.equalsIgnoreCase(customer.getUserName()) && password.equals(customer.getPassword())) {
                        Collections.swap(Customer.getCustomers(), Customer.getCustomers().indexOf(customer), 0);
                        found[0] = true;
                        System.out.println("Found");
                    }
                });
                if (!found[0]) {
                    System.out.println("\nThe customer '" + userName + "' does not exsist or your password was wrong, please try again...\n");
                }
            }
        } while (!found[0]);
        custTable();
    }


    public static void createCust() throws SQLException {
        String table = "customer";
        System.out.println("Before you can check in, we just need some details from you..");
        System.out.println("Your firstname: ");
        String firstName = sc.nextLine();
        System.out.println("Your lastname: ");
        String lastName = sc.nextLine();
        System.out.println("Choose a username: ");
        String userName = inputUserName();
        System.out.println("Choose a password: ");
        String password = sc.nextLine();
        System.out.println("Your telephonenumber: ");
        String telephone = sc.nextLine();
        Customer customer = new Customer(userName, firstName, lastName, telephone, password, 0, null);
        Database.createCust(table, userName, firstName, lastName, telephone, password);
        Customer.getCustomers().add(customer);
        Collections.swap(Customer.getCustomers(), Customer.getCustomers().indexOf(customer), 0);
        chooseRoom();
    }


    protected static void custTable() throws SQLException {
        SaveToFile.saveFile();
        boolean exit = false;
        while (!exit) {
            System.out.println("\n\nYou are logged in as: " + Customer.getCustomers().get(0).getUserName());
            System.out.println("================ Customer view ================");
            System.out.println("1. Display room details");
            System.out.println("2. Display room availability");
            System.out.println("3. Upgrade");
            System.out.println("4. Order food");
            System.out.println("5. Checkout");
            System.out.println("6. My bill");
            System.out.println("7. Exit");
            int choice = intInput();

            switch (choice) {
                //case 3 -> chooseRoom();
                case 2 -> Database.availableRooms();
                case 3 -> chooseRoom();
                case 4 -> buyFood();
                case 5 -> checkOut();
                case 6 -> Database.customerInfo();


                default -> exit = true;
            }
        }
    }


    public static void chooseRoom() throws SQLException {
        System.out.println("================ Customer view Rooms ================");
        Database.availableRooms();
        System.out.println("\nChoose a room-number that is available.");
        int roomChoice = Main.intInput();
        System.out.println("Amount of days: ");
        int days = Main.intInput();
        Database.checkBefore(roomChoice, days);
        SaveToFile.saveFile();

    }

    public static void checkOut() throws SQLException {
        System.out.println("You have to pay: "+ Customer.getCustomers().get(0).getBill()+":-");
        System.out.println("1. Check out and pay");
        int choice = intInput();
        if (choice == 1) {
            System.out.println("You are now checked-out!");
            Customer.getCustomers().get(0).setBill(0);
            Database.checkOut(Customer.getCustomers().get(0).getUserName());
            SaveToFile.saveFile();
        }
    }

    public static void seeRoomInfo() {
        System.out.println("1. Luxury Double Room");
        System.out.println("2. Luxury Single Room");
        System.out.println("3. Deluxe Double Room");
        System.out.println("4. Deluxe Single Room");
    }

    public static void buyFood() throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println("------ Room Service -----");
            Database.seeItems();
            System.out.println("\nType in the item_id to buy it.");

            int item_choice = intInput();
            System.out.println("How many do you want to buy? ");
            int amount = intInput();
            Database.buyFood(item_choice, amount);
            String searchValue = "item_price";
            String go1 = "SELECT * FROM items WHERE item_id = " + item_choice + ";";
            int price = Database.valueDBLookUp(go1, searchValue); // get priceInfo
            int totalPrice = price * amount;
            updateBill(totalPrice);
            System.out.println(Customer.getCustomers());
            SaveToFile.saveFile();
        }
    }


    public static void loginRec() {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Log in as a current customer");
            System.out.println("2. Create an account");
            System.out.println("3. Go back to previous menu");
            int choice = intInput();
            switch (choice) {
                case 1 -> existingRec();
                case 2 -> createRec();

                default -> exit = true;
            }
        }
    }

    public static void createRec() {
        System.out.println("Your firstname: ");
        String firstName = sc.nextLine();
        System.out.println("Your lastname: ");
        String lastName = sc.nextLine();
        System.out.println("Choose a username: ");
        String userName = inputUserName();
        System.out.println("Choose a password: ");
        String password = sc.nextLine();

        Receptionist receptionist = new Receptionist(firstName, lastName, userName, password);
        Receptionist.getReceptionists().add(receptionist);
        Collections.swap(Receptionist.getReceptionists(), Receptionist.getReceptionists().indexOf(receptionist), 0);
        SaveToFile.saveFile2();
        receptionistTable();
    }

    public static void existingRec() {
        boolean exit = false;
        final boolean[] found = {false};
        do {
            Receptionist.getReceptionists().sort(Comparator.comparing(Receptionist::getUserName));
            Receptionist.getReceptionists().forEach(System.out::println);
            System.out.println("\n1. Login as an existing rec");
            System.out.println("0. Back to previous menu");
            int choice = intInput();
            if (choice == 1) {
                System.out.println("Type in your username: ");
                String userName = sc.nextLine();
                System.out.println("Type in your password: ");
                String password = sc.nextLine();
                Receptionist.getReceptionists().forEach((rec) -> {
                    if (userName.equalsIgnoreCase(rec.getUserName()) && password.equals(rec.getPassword())) {
                        Collections.swap(Receptionist.getReceptionists(), Receptionist.getReceptionists().indexOf(rec), 0);
                        found[0] = true;
                        receptionistTable();
                    }
                });
                if (!found[0]) {
                    System.out.println("\nThe receptionist with '" + userName + "' does not exsist or your password was wrong, please try again...\n");
                }
            }
        } while (!found[0]);
    }



    public static void existingAcc() {
        boolean exit = false;
        final boolean[] found = {false};
        do {
            System.out.println("\n1. Login as an customer");
            System.out.println("\n1. Login as an rec");


            Receptionist.getReceptionists().sort(Comparator.comparing(Receptionist::getUserName));
            Receptionist.getReceptionists().forEach(System.out::println);

            System.out.println("0. Back to previous menu");
            int choice = intInput();
            if (choice == 1) {
                System.out.println("Type in your username: ");
                String userName = sc.nextLine();
                System.out.println("Type in your password: ");
                String password = sc.nextLine();
                Accounts.accounts.forEach((acc) -> {
                    if (userName.equalsIgnoreCase(acc.getUserName()) && password.equals(acc.getPassword())) {
                        Collections.swap(Receptionist.getReceptionists(), Receptionist.getReceptionists().indexOf(acc), 0);
                        found[0] = true;
                        receptionistTable();
                    }
                });
                if (!found[0]) {
                    System.out.println("\nThe receptionist with '" + userName + "' does not exsist or your password was wrong, please try again...\n");
                }
            }
        } while (!found[0]);
    }







    protected static void receptionistTable() {
        boolean exit = false;
        while (!exit) {
            System.out.println("You are logged in as: " + Receptionist.getReceptionists().get(0).getUserName());
            System.out.println("================ Receptionist-view ================");
            System.out.println("1. Storing Customer Details");
            System.out.println("2. Searching Customer Details");
            System.out.println("3. Upgrade and delete details");
            System.out.println("4. Booking or upgrading room");
            System.out.println("5. Ordering Food for Particular Room");
            System.out.println("6. Check out for customer and showing bill");
            System.out.println("7. Go back to Menu");
            int choice = intInput();
            switch (choice) {
                //case 1 -> bookRoom();
                case 2 -> searchCustomer();

                default -> exit = true;
            }
        }
    }

    public static void searchCustomer() {
        boolean exit = false;
        while (!exit) {
            System.out.println("You are logged in as: " + Receptionist.getReceptionists().get(0).getUserName());
            System.out.println("================ Receptionist-search-view ================");
            System.out.println("1. Searching for customer username");
            System.out.println("2. Searching for customer with room");
            System.out.println("3. Go back to menu");
            int choice = intInput();
            switch (choice) {
                //case 1 -> bookRoom();
                case 1 -> {
                    System.out.println("Search for a customer by username: ");
                    String startsWith = sc.nextLine();
                    System.out.println("\nStarts with: " + startsWith);
                    Customer.getCustomers().forEach((s) -> {
                        if (s.getFirstName().startsWith(startsWith))
                            System.out.println(s);
                    });
                    amount();

                }
                case 2 -> {
                    System.out.println("Search for a customer by which room: ");
                    String startsWith = sc.nextLine();
                    System.out.println("\nStarts with: " + startsWith);
                    Customer.getCustomers().forEach((s) -> {
                        if (s.getRoomType().getTypeOfRoom().startsWith(startsWith))
                            System.out.println(s);
                    });
                    amount();
                }

                default -> exit = true;
            }
        }

    }

    public static void amount() {
        System.out.println("Amount found: " + Customer.getCustomers().stream().count());
    }

    public static void updateBill(int totalPrice) {
        System.out.println("The cost: " + totalPrice + ":- will be added to your bill.");
        int currentBill = Customer.getCustomers().get(0).getBill();
        Customer.getCustomers().get(0).setBill(currentBill + totalPrice);
        System.out.println("Your current bill is: " + Customer.getCustomers().get(0).getBill() + ":-");
    }

    public static int intInput() {
        int intInput;
        do {
            while (!sc.hasNextInt()) {
                String uInput = sc.next();
                System.out.printf("\"%s\" is not a number!\n", uInput);
            }
            intInput = sc.nextInt();
        } while (intInput <= 0);
        sc.nextLine();
        // System.out.printf("You have entered a positive number %d.\n" , intInput);
        return intInput;
    }

    public static String inputUserName() {
        String input;
        boolean alreadyTaken;
        do {
            alreadyTaken = false;
            input = sc.nextLine();
            for (Customer userName : Customer.getCustomers()) {
                if (input.equalsIgnoreCase(userName.getUserName())) {
                    alreadyTaken = true;
                    break;
                }
            }
            if (alreadyTaken) {
                System.out.println("Sorry, this username already exist, please choose another one..");
            }
        } while (alreadyTaken);
        return input;
    }
}

