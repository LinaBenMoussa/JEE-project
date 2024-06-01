package tn.iit.web;


import tn.iit.dao.MatiereDao;
import tn.iit.model.Matiere;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/")
public class MatiereServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MatiereDao matiereDao;

    public void init() {
        matiereDao = new MatiereDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertMatiere(request, response);
                    break;
                case "/delete":
                    deleteMatiere(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateMatiere(request, response);
                    break;
                default:
                    listMatiere(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listMatiere(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Matiere> listMatiere = matiereDao.selectAllMatieres();
        System.out.println("Nombre de matières dans la liste : " + listMatiere.size());

        // Afficher les éléments nom et description dans la console
        for (Matiere matiere : listMatiere) {
            System.out.println("Nom: " + matiere.getNom() + ", Description: " + matiere.getDescription());
        }

        getServletContext().setAttribute("listMatiere", listMatiere);
        response.sendRedirect("admin/list_Matiere.jsp");
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/Add_Matiere.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Matiere existingMatiere = matiereDao.selectMatiere(id);

        request.setAttribute("matiere", existingMatiere);
        response.sendRedirect("admin/Add_Matiere.jsp");
    }

    private void insertMatiere(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String nom = request.getParameter("nom");
        String description = request.getParameter("description");
        Matiere newMatiere = new Matiere(nom, description);
        matiereDao.insertMatiere(newMatiere);
        response.sendRedirect("list");
    }

    private void updateMatiere(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nom = request.getParameter("nom");
        String description = request.getParameter("description");

        Matiere matiere = new Matiere(id, nom, description);
        matiereDao.updateMatiere(matiere);
        response.sendRedirect("list");
    }

    private void deleteMatiere(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        matiereDao.deleteMatiere(id);
        response.sendRedirect("list");
    }
}
