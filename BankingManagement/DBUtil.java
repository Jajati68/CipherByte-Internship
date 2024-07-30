package com.cipherbytetechnologyBankapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Utility class for database connection
public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/bankingsystem";
    private static final String USER = "AdvJava";
    private static final String PASSWORD = "AdvJava283";

    // Method to get database connection
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}