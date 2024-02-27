import org.example.MaxProjectCountClient;

public List<MaxProjectCountClient> findMaxProjectsClient() throws SQLException {
    List<MaxProjectCountClient> clients = new ArrayList<>();
    String sql = "SELECT c.NAME, COUNT(p.ID) AS PROJECT_COUNT " +
                 "FROM client c " +
                 "JOIN project p ON c.ID = p.CLIENT_ID " +
                 "GROUP BY c.ID, c.NAME " +
                 "ORDER BY PROJECT_COUNT DESC " +
                 "LIMIT 1";
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {
        if (resultSet.next()) {
            String clientName = resultSet.getString("NAME");
            int projectCount = resultSet.getInt("PROJECT_COUNT");
            clients.add(new MaxProjectCountClient(clientName, projectCount));
        }
    }
    return clients;
}
