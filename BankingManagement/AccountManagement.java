package com.cipherbytetechnologyBankapp;

import java.sql.*;
import java.util.Scanner;

// AccountManagement class to handle account transactions
public class AccountManagement {
    private Connection connection;
    private Scanner scanner;

    public AccountManagement(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to credit money to the account
    public void deposit(long accountNumber) {
        scanner.nextLine(); // Consume the newline left-over
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline left-over
        System.out.print("Enter Security Pin: ");
        String securityPin = scanner.nextLine();

        // Update account balance
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ? AND security_pin = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setLong(2, accountNumber);
            preparedStatement.setString(3, securityPin);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Amount credited successfully!" : "Transaction failed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to debit money from the account
    public void withdrawal(long accountNumber) {
        scanner.nextLine(); // Consume the newline left-over
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline left-over
        System.out.print("Enter Security Pin: ");
        String securityPin = scanner.nextLine();

        // Check account balance
        String query = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("balance");
                if (currentBalance >= amount) {
                    // Update account balance
                    String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setDouble(1, amount);
                        updateStatement.setLong(2, accountNumber);
                        int rowsAffected = updateStatement.executeUpdate();
                        System.out.println(rowsAffected > 0 ? "Amount debited successfully!" : "Transaction failed!");
                    }
                } else {
                    System.out.println("Insufficient balance!");
                }
            } else {
                System.out.println("Invalid security pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 // Method to transfer money between accounts
    public void transfer(long sourceAccountNumber) {
        scanner.nextLine(); // Consume the newline left-over

        // Prompt for receiver account number
        System.out.print("Enter Receiver Account Number: ");
        long targetAccountNumber = scanner.nextLong();

        // Check if the receiver account exists
        String checkAccountQuery = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        try (PreparedStatement checkAccountStatement = connection.prepareStatement(checkAccountQuery)) {
            checkAccountStatement.setLong(1, targetAccountNumber);
            ResultSet accountResultSet = checkAccountStatement.executeQuery();
            if (accountResultSet.next() && accountResultSet.getInt(1) == 0) {
                System.out.println("Wrong Account Number!");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Prompt for amount
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline left-over

        // Prompt for security pin
        System.out.print("Enter Security Pin: ");
        String securityPin = scanner.nextLine();

        // Check source account balance and security pin
        String query = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, sourceAccountNumber);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("balance");

                // Check if there is sufficient balance
                if (currentBalance >= amount) {
                    // Debit from source account
                    String debitQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    try (PreparedStatement debitStatement = connection.prepareStatement(debitQuery)) {
                        debitStatement.setDouble(1, amount);
                        debitStatement.setLong(2, sourceAccountNumber);
                        int debitRowsAffected = debitStatement.executeUpdate();

                        // Check if debit was successful
                        if (debitRowsAffected > 0) {
                            // Credit to receiver account
                            String creditQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                            try (PreparedStatement creditStatement = connection.prepareStatement(creditQuery)) {
                                creditStatement.setDouble(1, amount);
                                creditStatement.setLong(2, targetAccountNumber);
                                int creditRowsAffected = creditStatement.executeUpdate();
                                System.out.println(creditRowsAffected > 0 ? "Transfer successful!" : "Transfer failed!");
                            }
                        } else {
                            System.out.println("Transfer failed!");
                        }
                    }
                } else {
                    System.out.println("Insufficient balance!");
                }
            } else {
                System.out.println("Wrong Security Pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view account balance
    public void checkBalance(long accountNumber) {
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Current Balance: " + resultSet.getDouble("balance"));
            } else {
                System.out.println("Account not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}