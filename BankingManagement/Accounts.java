package com.cipherbytetechnologyBankapp;

import java.sql.*;
import java.util.Scanner;

// Accounts class to handle account operations
public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to open a new account
    public long openAccount(String email) {
        if (!accountExists(email)) {
            scanner.nextLine(); // Consume the newline left-over
            System.out.print("Enter Full Name: ");
            String fullName = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline left-over
            System.out.print("Enter Security Pin: ");
            String securityPin = scanner.nextLine();

            // Insert new account into database
            String query = "INSERT INTO accounts (account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                long accountNumber = generateAccountNumber();
                preparedStatement.setLong(1, accountNumber);
                preparedStatement.setString(2, fullName);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, securityPin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Account created successfully. Your account number is: " + accountNumber);
                    return accountNumber;
                } else {
                    System.out.println("Account creation failed!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Account already exists for this email!");
        }
        return 0;
    }

    // Generate a new account number
    private long generateAccountNumber() {
        String query = "SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getLong("account_number") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 4229365110;
    }

    // Check if account exists for the given email
    public boolean accountExists(String email) {
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get account number for the given email
    public long getAccountNumber(String email) {
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
