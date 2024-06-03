package tn.iit.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import tn.iit.model.User;
import tn.iit.dao.UserDao;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;

    public void init() {
        userDao = new UserDao();
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
                    insertUser(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateUser(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String roleParam = request.getParameter("role");
        List<User> listUser;

        if (roleParam != null && !roleParam.isEmpty()) {
            int role = Integer.parseInt(roleParam);
            listUser = userDao.selectUsersByRole(role);
        } else {
            listUser = userDao.selectAllUsers();
        }
        System.out.println("list here");
        getServletContext().setAttribute("listUser", listUser);
        if(roleParam.equals("0")){
        response.sendRedirect("../admin/list_Ensignent.jsp");} else {
            response.sendRedirect("../admin/List_Agent.jsp");
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/Add_User.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int role = Integer.parseInt(request.getParameter("role"));
        User existingUser = userDao.selectUser(id);
        getServletContext().setAttribute("user", existingUser);
        if (role==0){
        response.sendRedirect("../admin/Edit_Ensignent.jsp");}else {
            response.sendRedirect("../admin/Edit_Agent.jsp");
        }

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        Date dateNaissance = Date.valueOf(request.getParameter("dateNaissance"));
        int role = Integer.parseInt(request.getParameter("role"));
        String email = request.getParameter("email");
        String pw = request.getParameter("pw");
        String telephone = request.getParameter("telephone");
        User newUser = new User(nom, prenom, dateNaissance, role, email, pw, telephone);
        userDao.insertUser(newUser);
        response.sendRedirect(request.getContextPath() + "/user/list?role="+role);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        Date dateNaissance = Date.valueOf(request.getParameter("dateNaissance"));
        String role1=request.getParameter("role");
        System.out.println(role1);
        int role = Integer.parseInt(request.getParameter("role"));

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");
        String telephone = request.getParameter("telephone");

        User user = new User(id, nom, prenom, dateNaissance, role, email, pw, telephone);
        System.out.println(role);
        userDao.updateUser(user);
        response.sendRedirect(request.getContextPath() + "/user/list?role="+role);

    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int role= Integer.parseInt(request.getParameter("role"));
        userDao.deleteUser(id);
        System.out.println("delete");
        response.sendRedirect(request.getContextPath() + "/user/list?role="+role);

    }
}
