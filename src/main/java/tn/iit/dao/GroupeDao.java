package tn.iit.dao;


import tn.iit.model.Groupe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupeDao {

    private String jdbcURL = "jdbc:mysql://localhost:3306/tiragebd?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";
    private static final String INSERT_GROUPE_SQL = "INSERT INTO groupe (nom, nbre) VALUES (?, ?);";
    private static final String SELECT_GROUPE_BY_ID = "SELECT id, nom, nbre FROM groupe WHERE id = ?;";
    private static final String SELECT_ALL_GROUPES = "SELECT * FROM groupe;";
    private static final String DELETE_GROUPE_SQL = "DELETE FROM groupe WHERE id = ?;";
    private static final String UPDATE_GROUPE_SQL = "UPDATE groupe SET nom = ?, nbre = ? WHERE id = ?;";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertGroupe(Groupe groupe) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GROUPE_SQL)) {
            preparedStatement.setString(1, groupe.getNom());
            preparedStatement.setInt(2, groupe.getNbre());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public Groupe selectGroupe(int id) {
        Groupe groupe = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GROUPE_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom");
                int nbre = rs.getInt("nbre");
                groupe = new Groupe(id, nom, nbre);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return groupe;
    }

    public List<Groupe> selectAllGroupes() {
        List<Groupe> groupes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_GROUPES)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                int nbre = rs.getInt("nbre");
                groupes.add(new Groupe(id, nom, nbre));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return groupes;
    }

    public boolean deleteGroupe(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_GROUPE_SQL)) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateGroupe(Groupe groupe) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_GROUPE_SQL)) {
            preparedStatement.setString(1, groupe.getNom());
            preparedStatement.setInt(2, groupe.getNbre());
            preparedStatement.setInt(3, groupe.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;
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
                    System.err.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
