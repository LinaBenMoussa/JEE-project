package tn.iit.dao;

import tn.iit.model.EnseignantGroupe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnseignantGroupeDao {
    private String jdbcURL = "jdbc:mysql://localhost:3306/tiragebd?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";
    private static final String INSERT_ENSEIGNANT_GROUPE_SQL = "INSERT INTO `enseignant-groupe` (idgroupe, idenseignant) VALUES (?, ?)";
    private static final String DELETE_ENSEIGNANT_GROUPE_SQL = "DELETE FROM `enseignant-groupe` WHERE idgroupe = ? AND idenseignant = ?";
    private static final String SELECT_ENSEIGNANT_GROUPE_BY_ID_GROUPE_SQL = "SELECT * FROM `enseignant-groupe` WHERE idgroupe = ?";
    private static final String SELECT_ENSEIGNANT_GROUPE_BY_ID_ENSEIGNANT_SQL = "SELECT * FROM `enseignant-groupe` WHERE idenseignant = ?";

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

    public void insertEnseignantGroupe(EnseignantGroupe enseignantGroupe) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ENSEIGNANT_GROUPE_SQL)) {
            preparedStatement.setInt(1, enseignantGroupe.getIdgroupe());
            preparedStatement.setInt(2, enseignantGroupe.getIdenseignant());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void deleteEnseignantGroupe(int idGroupe, int idEnseignant) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ENSEIGNANT_GROUPE_SQL)) {
            preparedStatement.setInt(1, idGroupe);
            preparedStatement.setInt(2, idEnseignant);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public List<Integer> getEnseignantByIdGroupe(int idGroupe) {
        List<Integer> enseignantGroupes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ENSEIGNANT_GROUPE_BY_ID_GROUPE_SQL)) {
            preparedStatement.setInt(1, idGroupe);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int idEnseignant = rs.getInt("id_enseignant");
                enseignantGroupes.add(idEnseignant);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return enseignantGroupes;
    }

    public List<Integer> getGroupesByIdEnseignant(int idEnseignant) {
        List<Integer> enseignantGroupes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ENSEIGNANT_GROUPE_BY_ID_ENSEIGNANT_SQL)) {
            preparedStatement.setInt(1, idEnseignant);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int idGroupe = rs.getInt("idgroupe");
                enseignantGroupes.add(idGroupe);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return enseignantGroupes;
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
