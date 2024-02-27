package org.example;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class ClientService {

    private final Connection connection;

    public ClientService(Connection connection) {
        this.connection = connection;
    }

    public long create(String name) throws SQLException {
        String sql = "INSERT INTO client (NAME) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating client failed, no ID obtained.");
            }
        }
    }

    public String getById(long id) throws SQLException {
        String sql = "SELECT NAME FROM client WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("NAME");
                } else {
                    throw new SQLException("Client not found with id: " + id);
                }
            }
        }
    }

    public void setName(long id, String name) throws SQLException {
        String sql = "UPDATE client SET NAME = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setLong(2, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating client failed, no rows affected.");
            }
        }
    }

    public void deleteById(long id) throws SQLException {
        String sql = "DELETE FROM client WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting client failed, no rows affected.");
            }
        }
    }

    public List<Client> listAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT ID, NAME FROM client";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                clients.add(new Client(id, name));
            }
        }
        return clients;
    }

    public static class Client {
        private final long id;
        private final String name;

        public Client(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
