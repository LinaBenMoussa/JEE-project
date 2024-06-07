package tn.iit.web;

import tn.iit.dao.DemandeDao;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tn.iit.model.demande;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/demandes/*")
public class DemandeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DemandeDao demandeDao;

    public void init() {
        demandeDao = new DemandeDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertDemande(request, response);
                    break;
                case "/delete":
                    deleteDemande(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateDemande(request, response);
                    break;
                case "/list2":
                    listDemande2(request, response);
                    break;
                default:
                    listDemande(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listDemande(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
           demandeDao.deleteExpiredDemandes();  // Supprimer les demandes expirées
            List<demande> listDemande = demandeDao.selectAllDemandes();
            request.setAttribute("listDemande", listDemande);

            // Log the list of demands in console
            for (demande d : listDemande) {
                System.out.println("ID: " + d.getId());
                System.out.println("Nom Groupe: " + d.getNomGroupe());
                System.out.println("Nom Matière: " + d.getNomMatiere());
                System.out.println("Nombre Étudiants: " + d.getNbreEtudiant());
                System.out.println("Document: " + d.getDocument());
                System.out.println("Date: " + d.getDate());
                System.out.println("État: " + d.getEtat());
                System.out.println("-------------------------------------");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/agent/listImpression.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
    private void listDemande2(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int etat = Integer.parseInt(request.getSession().getAttribute("id").toString());
        List<demande> listDemande = demandeDao.selectDemandes(etat);

        request.getServletContext().setAttribute("listDemande",listDemande);

        response.sendRedirect("../enseignant/listImpression.jsp");
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/Add_Demande.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        demande existingDemande = demandeDao.selectDemande(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/Edit_Demande.jsp");
        request.setAttribute("demande", existingDemande);
        dispatcher.forward(request, response);
    }

    private void insertDemande(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String nomGroupe = request.getParameter("nomGroupe");
        String nomMatiere = request.getParameter("nomMatiere");
        int nbreEtudiant = Integer.parseInt(request.getParameter("nbreEtudiant"));
        String document = request.getParameter("document");
        java.sql.Date date = java.sql.Date.valueOf(request.getParameter("date"));
        int etat = Integer.parseInt(request.getParameter("etat"));
        demande newDemande = new demande(nomGroupe, nomMatiere, nbreEtudiant, document, etat, date);
        demandeDao.insertDemande(newDemande);
        response.sendRedirect("list");
    }

    private void updateDemande(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nomGroupe = request.getParameter("nomGroupe");
        String nomMatiere = request.getParameter("nomMatiere");
        int nbreEtudiant = Integer.parseInt(request.getParameter("nbreEtudiant"));
        String document = request.getParameter("document");
        java.sql.Date date = java.sql.Date.valueOf(request.getParameter("date"));
        int etat = Integer.parseInt(request.getParameter("etat"));

        demande demande = new demande(id, nomGroupe, nomMatiere, nbreEtudiant, document, date, etat);
        demandeDao.updateDemande(demande);
        response.sendRedirect("list");
    }

    private void deleteDemande(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        demandeDao.deleteDemande(id);
        response.sendRedirect("list");
    }
}
