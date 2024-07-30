package com.cipherbytetechnologyBankapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

// User class to handle user registration and login
public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to register a new user
    public void register() {
        scanner.nextLine(); // Consume the newline left-over
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Check if user already exists
        if (userExists(email)) {
            System.out.println("User already exists with this email address!");
            return;
        }

        // Insert new user into database
        String query = "INSERT INTO user (full_name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectedRows = preparedStatement.executeUpdate();
            System.out.println(affectedRows > 0 ? "Registration successful!" : "Registration failed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method for user login
    public String login() {
        scanner.nextLine(); // Consume the newline left-over
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Verify email and password
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return email;
            } else {
                System.out.println("Incorrect email or password!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check if user exists in the database
    private boolean userExists(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
