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

    // робота PreparedStatement
    private ResultSet executePreparedStatement(String sqlFilePath, String... params) throws IOException {
        String sqlQuery = readSqlFile(sqlFilePath);
        try {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            return statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to execute prepared statement", e);
        }
    }

    // читає файли(букварик містний)
    private String readSqlFile(String sqlFilePath) throws IOException {
        try {
            return Files.readString(Path.of(sqlFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to read SQL file", e);
        }
    }


    public List<ProjectInfo> findLongestProject() {
        List<ProjectInfo> projects = new ArrayList<>();
        try {
            ResultSet resultSet = executePreparedStatement("find_longest_project.sql");
            while (resultSet.next()) {
                int projectId = resultSet.getInt("PROJECT_ID");
                int durationInDays = resultSet.getInt("DURATION_IN_DAYS");
                projects.add(new ProjectInfo(projectId, durationInDays));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }


    public List<ClientInfo> findMaxProjectsClient() {
        List<ClientInfo> clients = new ArrayList<>();
        try {
            ResultSet resultSet = executePreparedStatement("find_max_projects_client.sql");
            while (resultSet.next()) {
                String clientName = resultSet.getString("NAME");
                int projectCount = resultSet.getInt("PROJECT_COUNT");
                clients.add(new ClientInfo(clientName, projectCount));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }


    public List<WorkerInfo> findMaxSalaryWorker() {
        List<WorkerInfo> workers = new ArrayList<>();
        try {
            ResultSet resultSet = executePreparedStatement("find_max_salary_worker.sql");
            while (resultSet.next()) {
                String workerName = resultSet.getString("NAME");
                int salary = resultSet.getInt("SALARY");
                workers.add(new WorkerInfo(workerName, salary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workers;
    }


    public List<WorkerInfo> findYoungestAndEldestWorkers() {
        List<WorkerInfo> workers = new ArrayList<>();
        try {
            ResultSet resultSet = executePreparedStatement("find_youngest_eldest_workers.sql");
            while (resultSet.next()) {
                String workerName = resultSet.getString("NAME");
                String workerType = resultSet.getString("TYPE");
                String birthday = resultSet.getString("BIRTHDAY");
                workers.add(new WorkerInfo(workerName, workerType, birthday));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workers;
    }


    public List<ProjectPrice> printProjectPrices() {
        List<ProjectPrice> prices = new ArrayList<>();
        try {
            ResultSet resultSet = executePreparedStatement("print_project_prices.sql");
            while (resultSet.next()) {
                int projectId = resultSet.getInt("PROJECT_ID");
                int price = resultSet.getInt("PRICE");
                prices.add(new ProjectPrice(projectId, price));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prices;
    }


    public static class ProjectInfo {
        private final int projectId;
        private final int durationInDays;

        public ProjectInfo(int projectId, int durationInDays) {
            this.projectId = projectId;
            this.durationInDays = durationInDays;
        }

        public int getProjectId() {
            return projectId;
        }

        public int getDurationInDays() {
            return durationInDays;
        }
    }


    public static class ClientInfo {
        private final String clientName;
        private final int projectCount;

        public ClientInfo(String clientName, int projectCount) {
            this.clientName = clientName;
            this.projectCount = projectCount;
        }

        public String getClientName() {
            return clientName;
        }

        public int getProjectCount() {
            return projectCount;
        }
    }


    public static class WorkerInfo {
        private final String workerName;
        private final String workerType;
        private final String birthday;

        public WorkerInfo(String workerName, int salary) {
            this.workerName = workerName;
            this.workerType = "Worker with highest salary";
            this.birthday = "";
        }

        public WorkerInfo(String workerName, String birthday, String workerType) {
            this.workerName = workerName;
            this.workerType = workerType;
            this.birthday = birthday;
        }

        public String getWorkerName() {
            return workerName;
        }

        public String getWorkerType() {
            return workerType;
        }

        public String getBirthday() {
            return birthday;
        }
    }


    public static class ProjectPrice {
        private final int projectId;
        private final int price;

        public ProjectPrice(int projectId, int price) {
            this.projectId = projectId;
            this.price = price;
        }

        public int getProjectId() {
            return projectId;
        }

        public int getPrice() {
            return price;
        }
    }
}
