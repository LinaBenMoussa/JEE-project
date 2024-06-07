package tn.iit.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import tn.iit.dao.EnseignantGroupeDao;



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
        System.out.println("id dans EG"+id);
        List<Integer> listGroupes = EG.getGroupesByIdEnseignant(id);
        for(Integer i: listGroupes){System.out.println("i"+i);}

        request.getServletContext().setAttribute("idEnseignant",id);
        request.getServletContext().setAttribute("listGroupes", listGroupes);
        response.sendRedirect("../groupes/list");

    }

}
