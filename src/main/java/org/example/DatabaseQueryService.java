package org.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryService {
    private final Connection connection;

    public DatabaseQueryService(Connection connection) {
        this.connection = connection;
    }

    public List<MaxProjectCountClient> findMaxProjectsClient() {
        List<MaxProjectCountClient> result = new ArrayList<>();

        try {
            String sqlFilePath = "sql/find_max_projects_client.sql";
            String sql = readSqlFile(sqlFilePath);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("NAME");
                        int projectCount = resultSet.getInt("PROJECT_COUNT");

                        MaxProjectCountClient client = new MaxProjectCountClient(name, projectCount);
                        result.add(client);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String readSqlFile(String sqlFilePath) throws Exception {
        String content = Files.readString(Path.of(sqlFilePath));
        return content.replaceAll("\\s+", " ");
    }

    public static void main(String[] args) {
        try {
            Connection connection = Database.getInstance().getConnection();
            DatabaseQueryService queryService = new DatabaseQueryService(connection);

            // Example: Find max projects for clients
            List<MaxProjectCountClient> maxProjectCountClients = queryService.findMaxProjectsClient();

            for (MaxProjectCountClient client : maxProjectCountClients) {
                System.out.println("Client: " + client.getName() + ", Project Count: " + client.getProjectCount());
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
