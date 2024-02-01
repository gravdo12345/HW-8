package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class DatabasePopulateService {

    private final Connection connection;

    public DatabasePopulateService(Connection connection) {
        this.connection = connection;
    }

    public void populateDatabase() {
        try {
            // Read and execute populate_db.sql
            String sqlFilePath = "src/main/resources/sql/populate_db.sql";
            String sql = readSqlFile(sqlFilePath);

            // Execute SQL query
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or throw a new exception if needed
        }
    }

    private String readSqlFile(String sqlFilePath) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sqlFilePath);

        try {
            if (inputStream != null) {
                try (Scanner scanner = new Scanner(inputStream)) {
                    StringBuilder sqlStatements = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        sqlStatements.append(scanner.nextLine()).append("\n");
                    }
                    return sqlStatements.toString();
                }
            } else {
                throw new IOException("SQL file not found: " + sqlFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception or throw a new exception if needed
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            // Replace with your database connection details
            Connection connection = DriverManager.getConnection("jdbc:your_database_url", "username", "password");

            DatabasePopulateService populateService = new DatabasePopulateService(connection);

            // Example: Populate the database
            populateService.populateDatabase();

            // Close the connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
