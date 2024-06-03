package tn.iit.dao;

import tn.iit.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private String jdbcURL = "jdbc:mysql://localhost:3306/tiragebd?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";

    private static final String INSERT_USER_SQL = "INSERT INTO user (nom, prenom, dateNaissance, role, email, pw, telephone) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "SELECT id, nom, prenom, dateNaissance, role, email, pw, telephone FROM user WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM user";
    private static final String DELETE_USER_SQL = "DELETE FROM user WHERE id = ?";
    private static final String UPDATE_USER_SQL = "UPDATE user SET nom = ?, prenom = ?, dateNaissance = ?, role = ?, email = ?, pw = ?, telephone = ? WHERE id = ?";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertUser(User user) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setDate(3, new java.sql.Date(user.getDateNaissance().getTime()));
            preparedStatement.setInt(4, user.getRole());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPw());
            preparedStatement.setString(7, user.getTelephone());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public User selectUser(int id) {
        User user = null;
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                Date dateNaissance = rs.getDate("dateNaissance");
                int role = rs.getInt("role");
                String email = rs.getString("email");
                String pw = rs.getString("pw");
                String telephone = rs.getString("telephone");
                user = new User(id, nom, prenom, dateNaissance, role, email, pw, telephone);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                Date dateNaissance = rs.getDate("dateNaissance");
                int role = rs.getInt("role");
                String email = rs.getString("email");
                String pw = rs.getString("pw");
                String telephone = rs.getString("telephone");
                users.add(new User(id, nom, prenom, dateNaissance, role, email, pw, telephone));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USER_SQL)) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setDate(3, new java.sql.Date(user.getDateNaissance().getTime()));
            statement.setInt(4, user.getRole());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPw());
            statement.setString(7, user.getTelephone());
            statement.setInt(8, user.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    // Additional methods to handle role-specific logic
    public List<User> selectUsersByRole(int role) {
        List<User> users = new ArrayList<>();
        String SELECT_USERS_BY_ROLE = "SELECT * FROM user WHERE role = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_BY_ROLE)) {
            preparedStatement.setInt(1, role);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                Date dateNaissance = rs.getDate("dateNaissance");
                String email = rs.getString("email");
                String pw = rs.getString("pw");
                String telephone = rs.getString("telephone");
                users.add(new User(id, nom, prenom, dateNaissance, role, email, pw, telephone));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }
}
