package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitService {

    public static void main(String[] args) {
        try {
            String sql = new String(Files.readAllBytes(Paths.get("sql/init_db.sql")));
            executeSql(sql);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void executeSql(String sql) {
        try (Connection connection = Database.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle errors as needed
        }
    }
}
