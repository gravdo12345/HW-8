package org.example;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseQueryService {
    private final Connection connection;

    public DatabaseQueryService(Connection connection) {
        this.connection = connection;
    }

    public class MaxProjectCountClient {
        private String name;
        private int projectCount;

        public MaxProjectCountClient(String name, int projectCount) {
            this.name = name;
            this.projectCount = projectCount;
        }

        public String getName() {
            return name;
        }

        public int getProjectCount() {
            return projectCount;
        }
    }


    public List<MaxProjectCountClient> findMaxProjectsClient() {
        List<MaxProjectCountClient> result = new ArrayList<>();

        try {
            // Read and execute find_max_projects_client.sql
            String sqlFilePath = "sql/find_max_projects_client.sql";
            String sql = readSqlFile(sqlFilePath);

            // Execute SQL query
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
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sqlFilePath);

        if (inputStream != null) {
            try (Scanner scanner = new Scanner(inputStream)) {
                StringBuilder sqlStatements = new StringBuilder();
                while (scanner.hasNextLine()) {
                    sqlStatements.append(scanner.nextLine()).append("\n");
                }

                return sqlStatements.toString();
            }
        } else {
            throw new Exception("SQL file not found: " + sqlFilePath);
        }
    }

    public static void main(String[] args) {
        try {
            
            Connection connection = DriverManager.getConnection("jdbc:your_database_url", "username", "password");

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
