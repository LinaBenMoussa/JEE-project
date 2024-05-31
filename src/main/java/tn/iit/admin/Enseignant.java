package tn.iit.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/addEnseignant")
public class Enseignant extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uemail = req.getParameter("email");
        String upwd = req.getParameter("password");
        String nom = req.getParameter("nom");
        String prenom = req.getParameter("prenom");
        String date = req.getParameter("date");

        // JDBC driver name and database URL
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/tiragebd?useSSL=false";

        // Database credentials
        String USER = "root";
        String PASS = "";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            con = DriverManager.getConnection(DB_URL, USER, PASS);

            // Create a prepared statement to insert data into the enseignant table
            String sql = "INSERT INTO user (email, pw, nom, prenom, dateNaissance,role,telephone) VALUES (?, ?, ?, ?, ?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, uemail);
            ps.setString(2, upwd);
            ps.setString(3, nom);
            ps.setString(4, prenom);
            ps.setString(5, date);
            ps.setString(6, "0");
            ps.setString(7, "20524652");

            // Execute the statement
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Enseignant ajouté avec succès !");

            } else {
                System.out.println("Échec de l'ajout de l'enseignant.");
                // Vous pouvez rediriger vers une page d'échec ou faire toute autre opération nécessaire ici
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Close resources
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
