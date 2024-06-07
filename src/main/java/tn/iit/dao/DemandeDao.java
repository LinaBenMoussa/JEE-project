package tn.iit.dao;

import tn.iit.model.demande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandeDao {

    private String jdbcURL = "jdbc:mysql://localhost:3306/tiragebd?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";

    private static final String INSERT_DEMANDE_SQL = "INSERT INTO demande (nomGroupe, nomMatiere, nbreEtudiant, document, date, etat) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SELECT_DEMANDE_BY_ID = "SELECT * FROM demande WHERE id = ?;";
    private static final String SELECT_ALL_DEMANDES = "SELECT * FROM demande;";
    private static final String DELETE_DEMANDE_SQL = "DELETE FROM demande WHERE id = ?;";
    private static final String UPDATE_DEMANDE_SQL = "UPDATE demande SET nomGroupe = ?, nomMatiere = ?, nbreEtudiant = ?, document = ?, date = ?, etat = ? WHERE id = ?;";
    private static final String DELETE_EXPIRED_DEMANDES_SQL = "DELETE FROM demande WHERE date < CURDATE();";
    private static final String SELECT_DEMANDES = "SELECT * FROM demande where etat =?;";

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

    public void insertDemande(demande demande) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DEMANDE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, demande.getNomGroupe());
            preparedStatement.setString(2, demande.getNomMatiere());
            preparedStatement.setInt(3, demande.getNbreEtudiant());
            preparedStatement.setString(4, demande.getDocument());
            preparedStatement.setDate(5, new java.sql.Date(demande.getDate().getTime()));
            preparedStatement.setInt(6, demande.getEtat());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        demande.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating demande failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException(e);
        }
    }

    public demande selectDemande(int id) {
        demande demande = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DEMANDE_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String nomGroupe = rs.getString("nomGroupe");
                String nomMatiere = rs.getString("nomMatiere");
                int nbreEtudiant = rs.getInt("nbreEtudiant");
                String document = rs.getString("document");
                Date date = rs.getDate("date");
                int etat = rs.getInt("etat");
                demande = new demande(id, nomGroupe, nomMatiere, nbreEtudiant, document, date, etat);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return demande;
    }

    public List<demande> selectAllDemandes() {
        List<demande> demandes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_DEMANDES)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomGroupe = rs.getString("nomGroupe");
                String nomMatiere = rs.getString("nomMatiere");
                int nbreEtudiant = rs.getInt("nbreEtudiant");
                String document = rs.getString("document");
                Date date = rs.getDate("date");
                int etat = rs.getInt("etat");
                demandes.add(new demande(id, nomGroupe, nomMatiere, nbreEtudiant, document, date, etat));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return demandes;
    }
    public List<demande> selectDemandes(int etat2) {
        List<demande> demandes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DEMANDES)) {
            preparedStatement.setInt(1,etat2);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomGroupe = rs.getString("nomGroupe");
                String nomMatiere = rs.getString("nomMatiere");
                int nbreEtudiant = rs.getInt("nbreEtudiant");
                String document = rs.getString("document");
                Date date = rs.getDate("date");
                int etat = rs.getInt("etat");
                demandes.add(new demande(id, nomGroupe, nomMatiere, nbreEtudiant, document, date, etat));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return demandes;
    }


    public boolean deleteDemande(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DEMANDE_SQL)) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateDemande(demande demande) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DEMANDE_SQL)) {
            preparedStatement.setString(1, demande.getNomGroupe());
            preparedStatement.setString(2, demande.getNomMatiere());
            preparedStatement.setInt(3, demande.getNbreEtudiant());
            preparedStatement.setString(4, demande.getDocument());
            preparedStatement.setDate(5, new java.sql.Date(demande.getDate().getTime()));
            preparedStatement.setInt(6, demande.getEtat());
            preparedStatement.setInt(7, demande.getId());

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
    public void deleteExpiredDemandes() throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EXPIRED_DEMANDES_SQL)) {
            preparedStatement.executeUpdate();
        }
    }

}
