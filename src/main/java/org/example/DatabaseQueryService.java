package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryService {

    private final Connection connection;

    public DatabaseQueryService(Connection connection) {
        this.connection = connection;
    }

    public List<ProjectInfo> findLongestProject() {
        return executeQueryFromFile("find_longest_project.sql");
    }

    private List<ProjectInfo> executeQueryFromFile(String sqlFilePath) {
        List<ProjectInfo> results = new ArrayList<>();

        try {
            String sqlQuery = readSqlFile(sqlFilePath);

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String projectName = resultSet.getString("PROJECT_NAME");
                        int monthCount = resultSet.getInt("MONTH_COUNT");

                        ProjectInfo projectInfo = new ProjectInfo(projectName, monthCount);
                        results.add(projectInfo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    // Метод для зчитування SQL-запиту з файлу
    private String readSqlFile(String sqlFilePath) throws IOException {
        return Files.readString(Path.of(sqlFilePath));
    }

    // це для читання(букварик типу, ну, жарт короче)
    public static class ProjectInfo {
        private final String projectName;
        private final int monthCount;

        public ProjectInfo(String projectName, int monthCount) {
            this.projectName = projectName;
            this.monthCount = monthCount;
        }

        public String getProjectName() {
            return projectName;
        }

        public int getMonthCount() {
            return monthCount;
        }
    }
}
