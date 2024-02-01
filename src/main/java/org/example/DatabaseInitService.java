package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitService {
    public static void main(String[] args) {
        try {
            Connection connection = Database.getInstance().getConnection();
            executeSqlScript(connection, "sql/init_db.sql");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeSqlScript(Connection connection, String scriptPath) {
        try {
            String content = Files.readString(Path.of(scriptPath));
            String[] queries = content.split(";");
            try (Statement statement = connection.createStatement()) {
                for (String query : queries) {
                    statement.addBatch(query);
                }
                statement.executeBatch();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
