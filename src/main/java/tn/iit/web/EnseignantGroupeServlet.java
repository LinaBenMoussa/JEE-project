package tn.iit.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tn.iit.dao.EnseignantGroupeDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/EG/*")
public class EnseignantGroupeServlet extends HttpServlet {  // Hérite de HttpServlet
    private static final long serialVersionUID = 1L;
    private EnseignantGroupeDao EG;

    public void init() {
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
                    // showNewForm(request, response);
                    break;
                // case "/insert":
                //     insertGroupe(request, response);
                //     break;
                // case "/delete":
                //     deleteGroupe(request, response);
                //     break;
                // case "/edit":
                //     showEditForm(request, response);
                //     break;
                // case "/update":
                //     updateGroupe(request, response);
                //     break;
                default:
                    listGroupes(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listGroupes(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        List<Integer> listGroupes = EG.getGroupesByIdEnseignant(id);
        for(Integer i: listGroupes){System.out.println(i);}
        // Utilisez request pour stocker l'attribut
        request.getServletContext().setAttribute("listGroupes", listGroupes);
        System.out.println(id);

        response.sendRedirect("../groupes/list");

    }

}
