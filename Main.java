package com.company;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class Main {
    public static Scanner sc = new Scanner(System.in);



    public static void main(String[] args){
        SaveToFile.readFile();
        SaveToFile.readFile2();
        try {
            Database.dbConnect(Database.login());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        createRooms();
        custOrRep();

    }

    public static void createRooms() {

    }


    protected static void custOrRep(){
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
    public static void loginCust() {
        boolean exit = false;
        while (!exit) {
            System.out.println(Customer.getCustomers());
        System.out.println("Are you already a registered customer at this hotel?");
        System.out.println("1. Log in as a current customer");
        System.out.println("2. Create an account");
        int choice = intInput();
        switch (choice) {
            case 1 -> existingCust();
            case 2 -> createCust();

            default -> exit = true;
        }
        }

    }

    public static void existingCust() {
        boolean exit = false;
        final boolean[] found = {false};
        do {
            //Customer.getCustomers().sort(Comparator.comparing(Customer::getUserName));
            Customer.getCustomers().forEach(System.out::println);
            System.out.println("\n1. Login as an existing customer");
            System.out.println("0. Back to previous menu");
            int choice = intInput();
            if (choice == 1) {
                System.out.println("Type in your username: ");
                String userName = sc.nextLine();
                System.out.println("Type in your password: ");
                String password = sc.nextLine();
                Customer.getCustomers().forEach((cust)->{
                    if(userName.equalsIgnoreCase(cust.getUserName()) && password.equals(cust.getPassword()))
                        Collections.swap(Customer.getCustomers(), Customer.getCustomers().indexOf(cust), 0);
                    found[0] = true;
                    custTable();
                });

                if (!found[0]) {
                    System.out.println("\nThe customer '" + userName + "' does not exsist or your password was wrong, please try again...\n");
                }
            }


        } while (!found[0]);
    }

    public static void createCust() {
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
        Customer customer = new Customer(firstName,lastName,userName,password,telephone,0, null);
        Customer.getCustomers().add(customer);
        try {
            Database.createCust(table, firstName, lastName,telephone,userName,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Collections.swap(Customer.getCustomers(), Customer.getCustomers().indexOf(customer), 0);
        chooseRoom();

    }


    protected static void custTable(){
        SaveToFile.saveFile();
        boolean exit = false;
        while (!exit) {
            System.out.println("You are logged in as: "+ Customer.getCustomers().get(0).getUserName());
            System.out.println("================ Customer view ================");
            System.out.println("1. Display room details");
            System.out.println("2. Display room availability");
            System.out.println("3. Upgrade");
            System.out.println("4. Order food");
            System.out.println("5. Checkout");
            System.out.println("6. Exit");
            int choice = intInput();

            switch (choice) {
                //case 3 -> chooseRoom();
                case 4 -> buyFood();

                default -> exit = true;
            }
        }
    }

    public static void chooseRoom() {
        System.out.println("================ Customer view Rooms ================");
        try {
            Database.bookAroom();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        int currentBill = Customer.getCustomers().get(0).getBill();


        SaveToFile.saveFile();
        }


        public static void seeRoomInfo() {
            System.out.println("1. Luxury Double Room");
            System.out.println("2. Luxury Single Room");
            System.out.println("3. Deluxe Double Room");
            System.out.println("4. Deluxe Single Room");
        }
        public static void buyFood() {
        boolean exit = false;






        while (!exit) {
        System.out.println("------ Room Service -----");
        System.out.println("1. Buy a sandwich (150 :-)");
        System.out.println("2. Buy a drink (30 :-)");
        System.out.println("3. Buy Pasta");
        System.out.println("4. Buy Noodles");
        System.out.println("5. Exit");

        int choice = intInput();
        int currentBill = Customer.getCustomers().get(0).getBill();
        //Food currentBought = Customer.getCustomers().get(0).getFoodBought();
                 switch (choice) {
                    case 1 -> {
                        Food sandwich = new Food("Sandwich", 150,0);
                       // Customer.boughtFood.add(sandwich);
                    //Customer.getCustomers().get(0).setFoodBought(currentBought,sandwich);
                    Customer.getCustomers().get(0).setBill(currentBill+sandwich.getPrice());
                    Customer.getCustomers().get(0).getBoughtFood().add(new Food("Sandwich", 150,0));

                    //Customer.getBoughtFood().add(sandwich);


                    }
                case 2 -> {
                    Food drink = new Food("Drink", 30,0);
                    //Customer.getCustomers().get(0).setFoodBought(drink);
                    Customer.getCustomers().get(0).setBill(currentBill+drink.getPrice());
                }
                case 3 -> {
                    Food pasta = new Food("Pasta", 110,0);
                    //Customer.getCustomers().get(0).setFoodBought(currentBought, pasta);
                    Customer.getCustomers().get(0).setBill(currentBill+pasta.getPrice());
                }
                case 4 -> {
                    Food noodles = new Food("Noodles", 110,0);
//                    Customer.getCustomers().get(0).setFoodBought(currentBought, noodles);
                    Customer.getCustomers().get(0).setBill(currentBill+noodles.getPrice());
                }
                default -> exit = true;
        }
        }
            SaveToFile.saveFile();
        System.out.println(Customer.getCustomers());
            showCust();
        SaveToFile.saveFile();
    }

public static void showCust() {

    System.out.println(" ----- Spelade omgångar ----- ");
    for (int k = 0; k < Customer.getCustomers().size(); k++) {
        System.out.println(Customer.getCustomers().get(k).getUserName()+ " Köpt mat: ");

        System.out.println("--------------------");
        System.out.println(Customer.getCustomers().get(k).getBill());
    }
    for (Customer food: Customer.getCustomers()) {
        //System.out.println(Customer.getCustomers().get(0).getFoodBought());

        //food.getBoughtFood();

    }
}

    //public static <food> void totalBill(food) {

public static void loginRec() {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Log in as a current customer");
            System.out.println("2. Create an account");
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

        Receptionist receptionist = new Receptionist(firstName,lastName,userName,password);
        Receptionist.getReceptionists().add(receptionist);
        Collections.swap(Receptionist.getReceptionists(), Receptionist.getReceptionists().indexOf(receptionist), 0);
        SaveToFile.saveFile2();
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
                Receptionist.getReceptionists().forEach((rec)->{
                    if(userName.equalsIgnoreCase(rec.getUserName()) && password.equals(rec.getPassword()))
                        Collections.swap(Receptionist.getReceptionists(), Receptionist.getReceptionists().indexOf(rec), 0);
                    found[0] = true;
                    receptionistTable();
                });
                if (!found[0]) {
                    System.out.println("\nThe customer '" + userName + "' does not exsist or your password was wrong, please try again...\n");
                }
            }
        } while (!found[0]);
    }


    protected static void receptionistTable(){
        boolean exit = false;
        while (!exit) {
            System.out.println("You are logged in as: "+ Receptionist.getReceptionists().get(0).getUserName());
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
                case 2-> searchCustomer();

                default -> exit = true;
            }
        }
    }
    public static void searchCustomer() {
        boolean exit = false;
        while (!exit) {
            System.out.println("You are logged in as: "+ Receptionist.getReceptionists().get(0).getUserName());
            System.out.println("================ Receptionist-search-view ================");
            System.out.println("1. Searching for customer username");
            System.out.println("2. Searching for customer with room");
            System.out.println("3. Go back to menu");
            int choice = intInput();
            switch (choice) {
                //case 1 -> bookRoom();
                case 1 -> {
                    System.out.println("Search for a customer by username: ");
                    String startsWith= sc.nextLine();
                    System.out.println("\nStarts with: "+startsWith);
                    Customer.getCustomers().forEach((s)->{
                        if(s.getFirstName().startsWith(startsWith))
                            System.out.println(s);
                    });
                    amount();

                }
                case 2 -> {
                    System.out.println("Search for a customer by which room: ");
                    String startsWith= sc.nextLine();
                    System.out.println("\nStarts with: "+startsWith);
                    Customer.getCustomers().forEach((s)->{
                        if(s.getRoomType().getTypeOfRoom().startsWith(startsWith))
                            System.out.println(s);
                    });
                    amount();
                }

                default -> exit = true;
            }
        }

    }
    public static void amount() {
        System.out.println("Amount found: "+ Customer.getCustomers().stream().count());

    }
    public static int intInput(){
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

