package tn.iit.web;

import tn.iit.dao.EnseignantGroupeDao;
import tn.iit.dao.GroupeDao;
import tn.iit.model.EnseignantGroupe;
import tn.iit.model.Groupe;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/groupes/*")
public class GroupeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private GroupeDao groupeDao;
    private EnseignantGroupeDao EG;
    public void init() {
        groupeDao = new GroupeDao();
        EG = new EnseignantGroupeDao();
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
                    insertGroupe(request, response);
                    break;
                case "/delete":
                    deleteGroupe(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateGroupe(request, response);
                    break;
                default:
                    listGroupe(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    private void listGroupe(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        Integer id = (Integer) getServletContext().getAttribute("idEnseignant");
        System.out.println("dans servlet id=" + id);
        List<Groupe> listGroupe = new ArrayList<>();
        List<Integer> listId = (List<Integer>) getServletContext().getAttribute("listGroupes");

        if (listId != null) {  // Vérification de null
            for (Integer g : listId) {
                System.out.println(g);
            }

            if (!listId.isEmpty()) {
                for (Integer i : listId) {
                    Groupe groupe = groupeDao.selectGroupe(i);
                    if (groupe != null) {
                        listGroupe.add(groupe);
                    } else {
                        System.out.println("Groupe avec ID " + i + " est null");
                    }
                }
                for (Groupe g : listGroupe) {
                    System.out.println(g.getNom());
                }
            } else {
                listGroupe = groupeDao.selectAllGroupes();

            }
        } else {

            System.out.println("listId est null");
            listGroupe = groupeDao.selectAllGroupes();

        }
        getServletContext().setAttribute("listGroupe", listGroupe);
        response.sendRedirect("../admin/list_Group.jsp?id=" + id);
    }


    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/Add_Group.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Groupe existingGroupe = groupeDao.selectGroupe(id);
        getServletContext().setAttribute("groupe", existingGroupe);
        response.sendRedirect("../admin/Edit_Group.jsp");

    }

    private void insertGroupe(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String nom = request.getParameter("nom");
        int nbre = Integer.parseInt(request.getParameter("nbre"));
        int id = 0;
        String idParam = request.getParameter("id");
        System.out.println("l'id ="+idParam);
        if (idParam != null && !idParam.equals("null")) {
            id = Integer.parseInt(idParam);
        }

        Groupe newGroupe = new Groupe(nom, nbre);
        groupeDao.insertGroupe(newGroupe);

        if (id != 0 && newGroupe.getId() !=0) {
            EnseignantGroupe enseignantGroupe = new EnseignantGroupe(newGroupe.getId(), id);
            System.out.println("groupeId"+newGroupe.getId());
            EG.insertEnseignantGroupe(enseignantGroupe);
        }
        response.sendRedirect("list");
    }



    private void updateGroupe(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nom = request.getParameter("nom");
        int nbre = Integer.parseInt(request.getParameter("nbre"));

        Groupe groupe = new Groupe(id, nom, nbre);
        groupeDao.updateGroupe(groupe);
        response.sendRedirect("list");
    }
    private void deleteGroupe(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        groupeDao.deleteGroupe(id);
        response.sendRedirect("list");
    }
}
