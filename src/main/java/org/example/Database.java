package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String JDBC_URL = "jdbc:h2:./database";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Database instance;
    private Connection connection;

    private Database() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Handle errors as needed
        }
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
