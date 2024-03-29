package com.company;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Arrays.stream;


public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static List<CurrentRoomBillItems> crBI = new ArrayList<>();


    public static void main(String[] args) {

        SaveToFile.readFile();
        System.out.println(Customer.getCustomers());
        System.out.println(Receptionist.getAccount());
        System.out.println(Accounts.account());
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

    public static int goDate() {
        LocalDate localDate = LocalDate.now();
        String pattern = "yyyyMMdd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return  Integer.parseInt(localDate.format(formatter));
    }

    public static void seeRoombillItem(){
        String gname = Customer.getCustomers().get(0).getUserName();
        crBI.stream()
                .filter(s-> s.getUserName().equals(gname))
                .forEach(System.out::println);
        int sum = crBI.stream()
                        .filter(p -> p.getUserName().equals(gname))
                        .mapToInt(CurrentRoomBillItems::getbPrice)
                        .sum();
        System.out.println("Total bill for the food you bought: " +sum);
    }
    protected static void custOrRep() throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Log in as current user");
            System.out.println("2. Create a new account");
            System.out.println("3. Exit");
            int choice = intInput();
            switch (choice) {
                case 1 -> loginAcc();
                case 2 -> createCust();
                case 3-> createRec();
                default -> exit = true;
            }
        }
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
        Customer customer = new Customer(userName, firstName, lastName, telephone, password, 0 ,"",0,1,0);
        Database.createCust(table, userName, firstName, lastName, telephone, password);
        Customer.getCustomers().add(customer);
        Accounts.account().add(customer);
        Collections.swap(Customer.getCustomers(), Customer.getCustomers().indexOf(customer), 0);
        chooseRoom(userName);
        custTable();
    }
    protected static void custTable() throws SQLException {
        String userName =Customer.getCustomers().get(0).getUserName();
        boolean exit = false;
        while (!exit) {
            System.out.println("\n\nYou are logged in as customer: " + userName);
            System.out.println("================ Customer view ================");
            System.out.println("1. Display room details");
            System.out.println("2. Display room availability");
            System.out.println("3. Upgrade");
            System.out.println("4. Order food");
            System.out.println("5. Checkout");
            System.out.println("6. My bill");
            System.out.println("7. My information");
            System.out.println("8. Exit");
            int choice = intInput();
            switch (choice) {
                case 1 -> seeRooms();
                case 2 -> Database.availableRooms();
                case 3 -> chooseRoom(userName);
                case 4 -> {
                    try {
                        buyFood(userName);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                case 5 -> checkOut();
                case 6 -> Database.customerInfo();
                case 7 -> System.out.println(Customer.getCustomers().get(0));
                default -> exit = true;
            }
            Database.updateInfo();
            SaveToFile.saveFile();
        }
    }

public static void seeRooms() {

}
    public static void chooseRoom(String userName) throws SQLException {
        System.out.println("================ Customer view Rooms ================");
        Database.availableRooms();
        System.out.println("\nChoose a room-number that is available.");
        int roomChoice = Main.intInput();

        String sql = "SELECT room_nmbr from roomnumber Where room_nmbr = ?;";
        int check = Database.check(sql, roomChoice);
        if (check == 0) {
            System.out.println("This room does not exsist!");
        }else {
        System.out.println("Amount of days: ");
        int days = Main.intInput();
        Database.checkBefore(userName, roomChoice, days);
        Customer.getCustomers().get(0).setCheckIn(goDate());
        SaveToFile.saveFile();
        }

    }
    public static void checkOut() throws SQLException {
        String userName = Customer.getCustomers().get(0).getUserName();
        System.out.println("You have to pay: "+ Customer.getCustomers().get(0).getBill()+":-");
        System.out.println("1. Check out and pay");
        int choice = intInput();
        if (choice == 1) {
            checkAmountOfDays(userName);
            System.out.println("You payed: "+Customer.getCustomers().get(0).getBill());
            System.out.println("You are now checked-out!");
            Database.checkOut(userName);
            checkOutSet(userName);
        }
    }
    public static void checkAmountOfDays(String userName)throws SQLException {
        String go1 = "SELECT * from reservationBooked WHERE userName='"+userName+"';";
        String check = "price";
        int price = Database.valueDBLookUp(go1,check);
        check = "amount_days";
        int days = Database.valueDBLookUp(go1,check);
        int totalP = price*days;
        System.out.println("You had plan to stay "+days+" days and therefore the cost for the room is: "+price*days+":-");
        Customer.getCustomers().forEach((s) -> {
            if (s.getUserName().equalsIgnoreCase(userName))
                System.out.println(s);
            int checkIn = s.getCheckIn(); int checkOut = goDate();
            int total = checkIn-checkOut;
            if(total == 0) {
                total++;
                }
                int realPrice =price*total;
                s.setBill(s.getBill()-totalP+realPrice);
        });
    }
    public static void buyFood(String userName) throws SQLException, InterruptedException {
            System.out.println("------ Room Service -----");
            Database.seeItems();
            System.out.println("\nType in the item_id to buy it.");
            int item_choice = intInput();
            String sql = "SELECT item_id from items Where item_id = ?;";
            int check = Database.check(sql, item_choice);
            if (check == 0) {
                System.out.println("This food does not exist at the hotel!");
            }else {
            System.out.println("How many do you want to buy? ");
            int amount = intInput();

            Database.buyFood(item_choice, amount, userName);
            System.out.println("\nThank you for your order! We will get it for you as soon as possible "+ userName+"....\n");
            Thread.sleep(1500);
            String searchValue = "item_price";
            String go1 = "SELECT * FROM items WHERE item_id = " + item_choice + ";";
            int price = Database.valueDBLookUp(go1, searchValue); // get priceInfo
            int totalPrice = price * amount;
                if(item_choice == 1){
                    crBI.add(new CurrentRoomBillItems(userName,"Pasta ", amount, amount*150));
                }else if (item_choice == 2){
                    crBI.add(new CurrentRoomBillItems(userName, "Noodles ", amount, amount*100));
                }else if (item_choice == 3){
                    crBI.add(new CurrentRoomBillItems(userName,"Drink ", amount, amount*30));
                }else if (item_choice == 4){
                    crBI.add(new CurrentRoomBillItems(userName,"Sandwich ", amount, amount*130));
                }
            seeRoombillItem();
            updateBill(totalPrice);
            SaveToFile.saveFile();
        }
    }
    public static void createRec() throws SQLException {
        System.out.println("Your firstname: ");
        String firstName = sc.nextLine();
        System.out.println("Your lastname: ");
        String lastName = sc.nextLine();
        System.out.println("Choose a username: ");
        String userName = inputUserName();
        System.out.println("Choose a password: ");
        String password = sc.nextLine();

        Receptionist receptionist = new Receptionist(firstName, lastName, userName, password,0);
        Receptionist.getAccount().add(receptionist);
        Accounts.account().add(receptionist);
        Collections.swap(Receptionist.getAccount(), Receptionist.getAccount().indexOf(receptionist), 0);
        SaveToFile.saveFile();
        receptionistTable();
    }
    public static void loginAcc() {
        boolean exit = false;
        final boolean[] found = {false};
        do {
            System.out.println("Login with your account:");
           // Receptionist.getReceptionists().sort(Comparator.comparing(Receptionist::getUserName));
           // Receptionist.getReceptionists().forEach(System.out::println);

                System.out.println("Type in your username: ");
                String userName = sc.nextLine();
                System.out.println("Type in your password: ");
                String password = sc.nextLine();
                if(user01(userName) == 0) {
                    Receptionist.getAccount().forEach((rec) -> {
                        if (userName.equalsIgnoreCase(rec.getUserName()) && password.equals(rec.getPassword())) {
                            found[0] = true;
                            Collections.swap(Receptionist.account(), Receptionist.getAccount().indexOf(rec),0);
                            try {
                                receptionistTable();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    });
                } else {
                    Customer.getCustomers().forEach((cust) ->{
                        if (userName.equalsIgnoreCase(cust.getUserName()) && password.equals(cust.getPassword())) {
                            found[0] = true;
                            Collections.swap(Customer.getCustomers(), Customer.getCustomers().indexOf(cust),0);
                            try {
                                custTable();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    });
                }
                if (!found[0]) {
                System.out.println("\nThe account with does not exsist or your password was wrong, please try again...\n");
            }
        }while (!found[0]);
        }

    protected static void receptionistTable() throws SQLException {
        SaveToFile.saveFile();
        boolean exit = false;
        while (!exit) {
            System.out.println("You are logged in as: "+ Receptionist.getAccount().get(0).getUserName());
            System.out.println("================ Receptionist-view ================");
            System.out.println("1. Storing Customer Details");
            System.out.println("2. Searching Customer Details");
            System.out.println("3. Upgrade and delete details");
            System.out.println("4. Booking or upgrading room");
            System.out.println("5. Ordering Food for Particular customer");
            System.out.println("6. Check out for customer and showing bill");
            System.out.println("7. Create a receptions account");
            System.out.println("8. Go back to Menu");
            int choice = intInput();
            switch (choice) {
                case 1 -> customerDetails();
                case 2 -> searchCustomer();
                case 4 -> {
                    System.out.println("Type in the username that you want to choose a room for: ");
                    String userName = inputUserExist();
                    chooseRoom(userName);
                }
                case 5 -> {
                    System.out.println("Type in the username that you want to choose a room for: ");
                    String userName = inputUserExist();
                    try {
                        buyFood(userName);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                case 6-> checkOutCust();
                case 7->  createRec();
                default -> exit = true;
            }
        }
    }
    public static void retrivePassForCust() {
        System.out.println("Type in the username of the customer:");
        String userName = inputUserExist();
        Customer.getCustomers().forEach((s) -> {
            if (s.getUserName().equalsIgnoreCase(userName)){
                System.out.println(s.getUserName()+" password is: "+ s.getPassword());
            }


        });
    }
    public static void customerDetails(){
        boolean exit = false;
        while (!exit) {
        System.out.println("1. See all customers");
        System.out.println("2. Retrive password for user");
        System.out.println("3. Change password for user");
        System.out.println("4. ");
            int choice = intInput();
            switch (choice) {
                case 1 -> System.out.println(Customer.getCustomers());
                case 2->retrivePassForCust();

                default -> exit = true;
            }

    }
    }
    protected static void checkOutCust() {
        System.out.println("Check out for customer");
        System.out.println(Customer.getCustomers());
        System.out.println("Type in the username of the customer that is going to check out: ");
        String userName = inputUserExist();
                Customer.getCustomers().forEach((s) -> {
            if (s.getUserName().equalsIgnoreCase(userName))
                System.out.println("The customer has to pay: "+s.getBill());
        System.out.println("1. Check out and pay");
        System.out.println("2. Go back");
        int choice = intInput();
        if (choice == 1) {
            try {
                checkAmountOfDays(userName);
                Database.checkOut(userName);
                checkOutSet(userName);
                System.out.println(userName + " is now checked-out!");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            try {
                receptionistTable();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        });
    }
    public static void checkOutSet(String userName) {
        Customer.getCustomers().forEach((s) -> {
            if (s.getUserName().equalsIgnoreCase(userName))
                s.setRoomNumber(0);
                s.setRoomType(null);
                s.setBill(0);
        });
        crBI.removeIf(p ->p.getUserName().equals(userName));
        SaveToFile.saveFile();
    }
    public static void searchCustomer() {
        boolean exit = false;
        while (!exit) {
            System.out.println("You are logged in as: " + Receptionist.getAccount().get(0).getUserName());
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
                        if (s.getRoomType().startsWith(startsWith))
                            System.out.println(s);
                    });
                    amount();
                }
                case 3 -> {
                    System.out.println("The customer with the higest bill: ");

                    //Customer.getCustomers().stream().
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
    public static int user01(String userName) {
            for (Accounts userType : Accounts.account) {
                if (userName.equalsIgnoreCase(userType.getUserName())) {
                    if(userType.getAccountType() == 0)
                    return 0;
                }
    }
        return 1;
    }
    public static String inputUserExist() {
        String input;
        boolean exist;
        do {
            exist = false;
            input = sc.nextLine();
            for (Customer userName : Customer.getCustomers()) {
                if (!input.equalsIgnoreCase(userName.getUserName())) {
                    exist = true;
                    break;
                }
            }
                if (exist) {
                    System.out.println("Sorry, this username does not exist, please choose another one..");
                }
        } while (exist);
        return input;
    }





}