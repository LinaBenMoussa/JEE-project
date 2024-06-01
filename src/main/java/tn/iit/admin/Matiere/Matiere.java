package tn.iit.admin.Matiere;

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

@WebServlet("/Add_Matiere")
public class Matiere extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nom = req.getParameter("nom");
        String description = req.getParameter("description");

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
            String sql = "INSERT INTO matiere (nom, description) VALUES (?, ?)";
            ps = con.prepareStatement(sql);

            ps.setString(2, description);
            ps.setString(1, nom);

            // Execute the statement
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                resp.sendRedirect("admin/Add_Matiere.jsp?success=true");
            } else {
                resp.sendRedirect("admin/Add_Matiere.jsp?success=false");
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
