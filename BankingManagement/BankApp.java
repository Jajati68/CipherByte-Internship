package com.cipherbytetechnologyBankapp;

import java.sql.Connection;
import java.util.Scanner;

public class BankApp {
    public static void main(String[] args) {
        // Establish the database connection
        Connection connection = DBUtil.getConnection();
        Scanner scanner = new Scanner(System.in);

        // Check if the connection is successful
        if (connection != null) {
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManagement accountManagement = new AccountManagement(connection, scanner);

            boolean runApp = true;

            while (runApp) {
                // Display main menu
                System.out.println("\n*** Welcome to the Bank Application! ***");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                String userEmail = null;

                switch (choice) {
                    case 1:
                        // Register a new user
                        user.register();
                        break;
                    case 2:
                        // Login an existing user
                        userEmail = user.login();
                        break;
                    case 3:
                        // Exit the application
                        runApp = false;
                        System.out.println("Exiting the application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                        break;
                }

                // If user successfully logs in
                if (userEmail != null) {
                    boolean loggedIn = true;

                    while (loggedIn) {
                        // Display user menu
                        System.out.println("\nMenu:");
                        System.out.println("1. Open Account");
                        System.out.println("2. Diposite Money");
                        System.out.println("3. Withdrawal Money");
                        System.out.println("4. Transfer Money");
                        System.out.println("5. Check Balance");
                        System.out.println("6. Logout");
                        System.out.print("Choose an option: ");

                        int menuChoice = scanner.nextInt();
                        long accountNumber = accounts.getAccountNumber(userEmail);

                        switch (menuChoice) {
                            case 1:
                                // Open a new account
                                accounts.openAccount(userEmail);
                                break;
                            case 2:
                                // Credit money to the account
                                accountManagement.deposit(accountNumber);
                                break;
                            case 3:
                                // Debit money from the account
                                accountManagement.withdrawal(accountNumber);
                                break;
                            case 4:
                                // Transfer money to another account
                                accountManagement.transfer(accountNumber);
                                break;
                            case 5:
                                // Check account balance
                                accountManagement.checkBalance(accountNumber);
                                break;
                            case 6:
                                // Logout
                                loggedIn = false;
                                System.out.println("Logged out successfully.");
                                break;
                            default:
                                System.out.println("Invalid choice! Please choose again.");
                                break;
                        }
                    }
                }
            }
        } else {
            System.out.println("Database connection failed!");
        }

        scanner.close();
    }
}
