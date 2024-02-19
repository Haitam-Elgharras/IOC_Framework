package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSingletonDb {
    private static String url = "jdbc:mysql://localhost:3306/javabase";
    private static String username = "java";
    private static String password = "azerty";
    public static Connection connection;

    // static means that this block will be executed once when the class is loaded
     static {
        System.out.println("Connecting database ...");
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
     }
}
