package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabasePopulateService {

    private final Connection connection;

    public DatabasePopulateService(Connection connection) {
        this.connection = connection;
    }

    public void populateDatabase() {
        try {
            // Read and execute populate_db.sql
            String sqlFilePath = "sql/populate_db.sql";
            String sql = readSqlFile(sqlFilePath);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readSqlFile(String sqlFilePath) {
        try {
            String content = Files.readString(Path.of(sqlFilePath));
            return content.replaceAll("\\s+", " ");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception or throw a new exception if needed
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            Connection connection = Database.getInstance().getConnection();

            DatabasePopulateService populateService = new DatabasePopulateService(connection);
            populateService.populateDatabase();

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
