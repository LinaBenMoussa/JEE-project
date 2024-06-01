package tn.iit.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
@WebServlet("/list_Matiere")
public class ListeMatiere extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> matieres = new ArrayList<>();

        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir une connexion
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiragebd", "root", "");

            // Créer une déclaration
            Statement stmt = conn.createStatement();

            // Exécuter une requête
            ResultSet rs = stmt.executeQuery("SELECT nom FROM matiere");

            // Extraire les données du ResultSet
            while (rs.next()) {
                matieres.add(rs.getString("nom"));
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mettre les données dans la requête
        req.setAttribute("matieres", matieres);

        // Rediriger vers la JSP
        resp.sendRedirect("admin/list_Matiere.jsp");
    }
    }

