package tn.iit.dao;

import tn.iit.model.Matiere;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatiereDao {

    private String jdbcURL = "jdbc:mysql://localhost:3306/tiragebd?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";
    private static final String INSERT_MATIERE_SQL = "INSERT INTO matiere (nom, description) VALUES (?, ?);";
    private static final String SELECT_MATIERE_BY_ID = "SELECT id, nom, description FROM matiere WHERE id = ?;";
    private static final String SELECT_ALL_MATIERES = "SELECT * FROM matiere;";
    private static final String DELETE_MATIERE_SQL = "DELETE FROM matiere WHERE id = ?;";
    private static final String UPDATE_MATIERE_SQL = "UPDATE matiere SET nom = ?, description = ? WHERE id = ?;";

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

    public void insertMatiere(Matiere matiere) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MATIERE_SQL)) {
            preparedStatement.setString(1, matiere.getNom());
            preparedStatement.setString(2, matiere.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public Matiere selectMatiere(int id) {
        Matiere matiere = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MATIERE_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                matiere = new Matiere(id, nom, description);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return matiere;
    }

    public List<Matiere> selectAllMatieres() {
        List<Matiere> matieres = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_MATIERES)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                matieres.add(new Matiere(id, nom, description));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return matieres;
    }

    public boolean deleteMatiere(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_MATIERE_SQL)) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateMatiere(Matiere matiere) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MATIERE_SQL)) {
            preparedStatement.setString(1, matiere.getNom());
            preparedStatement.setString(2, matiere.getDescription());
            preparedStatement.setInt(3, matiere.getId());

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
