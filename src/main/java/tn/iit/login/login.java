package tn.iit.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uemail = req.getParameter("email");
        String upwd = req.getParameter("pass");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiragebd?useSSL=false", "root", "");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE email=? AND pw=?");
            ps.setString(1, uemail);
            ps.setString(2, upwd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int role = rs.getInt("role");
                if (role == 0) {
                    // Redirect to Dashboard.jsp if role equals 0
                    resp.sendRedirect(req.getContextPath() + "/enseignant/Dashboard.jsp");
                }else{
                    if (role == 1) {
                        // Redirect to Dashboard.jsp if role equals 0
                        resp.sendRedirect(req.getContextPath() + "/agent/Dashboard.jsp");
                    }
                 else {
                    // Set attribute and forward to index.jsp for other roles
                    String email = rs.getString("email");
                    req.setAttribute("email", email);
                        resp.sendRedirect(req.getContextPath() + "/admin/Dashboard.jsp");
                }}
            } else {
                System.out.println("Authentication failed: invalid email or password.");

                // Set error message and forward back to login.jsp
                req.setAttribute("errorMessage", "Invalid email or password.");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);

        }

    }}