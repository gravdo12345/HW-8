package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabasePopulateService {
    public static void main(String[] args) {
        try {
            Connection connection = Database.getInstance().getConnection();
            executeSqlScript(connection, "sql/populate_db.sql");
            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void executeSqlScript(Connection connection, String scriptPath) throws IOException {
        String content = readSqlFile(scriptPath);
        String[] queries = content.split(";");
        try (PreparedStatement statement = connection.prepareStatement(content)) {
            for (String query : queries) {
                statement.addBatch(query);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String readSqlFile(String sqlFilePath) throws IOException {
        String content = Files.readString(Path.of(sqlFilePath));
        return content.replaceAll("\\s+", " ");
    }
}
