package org.example;

import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseQueryService {

    private final Database database;

    public DatabaseQueryService(Database database) {
        this.database = database;
    }

    // Method to find longest project
    public List<ProjectInfo> findLongestProject() {
        List<ProjectInfo> projects = new ArrayList<>();
        try {
            ResultSet resultSet = database.executePreparedStatement("SELECT pr.ID AS PROJECT_ID, DATEDIFF(pr.FINISH_DATE, pr.START_DATE) AS DURATION_IN_DAYS FROM project pr ORDER BY DURATION_IN_DAYS DESC LIMIT 1");
            while (resultSet.next()) {
                int projectId = resultSet.getInt("PROJECT_ID");
                int durationInDays = resultSet.getInt("DURATION_IN_DAYS");
                projects.add(new ProjectInfo(projectId, durationInDays));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    // Method to find client with the maximum number of projects(king of barbarian)
    public List<ClientInfo> findMaxProjectsClient() {
        List<ClientInfo> clients = new ArrayList<>();
        try {
            ResultSet resultSet = database.executePreparedStatement("SELECT c.NAME, COUNT(p.ID) AS PROJECT_COUNT FROM client c JOIN project p ON c.ID = p.CLIENT_ID GROUP BY c.ID, c.NAME ORDER BY PROJECT_COUNT DESC LIMIT 1");
            while (resultSet.next()) {
                String clientName = resultSet.getString("NAME");
                int projectCount = resultSet.getInt("PROJECT_COUNT");
                clients.add(new ClientInfo(clientName, projectCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    // Method to find worker with the highest salary(bro's money's confused)
    public List<WorkerInfo> findMaxSalaryWorker() {
        List<WorkerInfo> workers = new ArrayList<>();
        try {
            ResultSet resultSet = database.executePreparedStatement("SELECT NAME, SALARY FROM worker ORDER BY SALARY DESC LIMIT 1");
            while (resultSet.next()) {
                String workerName = resultSet.getString("NAME");
                int salary = resultSet.getInt("SALARY");
                workers.add(new WorkerInfo(workerName, salary));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workers;
    }

    // Method to find youngest and eldest workers(newbite's and OG's)
    public List<WorkerInfo> findYoungestAndEldestWorkers() {
        List<WorkerInfo> workers = new ArrayList<>();
        try {
            ResultSet resultSet = database.executePreparedStatement("SELECT 'YOUNGEST' AS TYPE, NAME, BIRTHDAY FROM worker ORDER BY BIRTHDAY ASC LIMIT 1 UNION SELECT 'ELDEST' AS TYPE, NAME, BIRTHDAY FROM worker ORDER BY BIRTHDAY DESC LIMIT 1");
            while (resultSet.next()) {
                String workerName = resultSet.getString("NAME");
                String workerType = resultSet.getString("TYPE");
                String birthday = resultSet.getString("BIRTHDAY");
                workers.add(new WorkerInfo(workerName, workerType, birthday));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workers;
    }

    // Method to print project prices(better print them moneyzzz)
    public List<ProjectPrice> printProjectPrices() {
        List<ProjectPrice> prices = new ArrayList<>();
        try {
            ResultSet resultSet = database.executePreparedStatement("SELECT pr.ID AS PROJECT_ID, SUM(w.SALARY * DATEDIFF(pr.FINISH_DATE, pr.START_DATE)) AS PRICE FROM project pr JOIN project_worker pw ON pr.ID = pw.PROJECT_ID JOIN worker w ON pw.WORKER_ID = w.ID GROUP BY pr.ID ORDER BY PRICE DESC");
            while (resultSet.next()) {
                int projectId = resultSet.getInt("PROJECT_ID");
                int price = resultSet.getInt("PRICE");
                prices.add(new ProjectPrice(projectId, price));
            }
        } catch (SQLException e) {
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
