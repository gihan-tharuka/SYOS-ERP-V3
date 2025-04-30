package servlets;

//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import authentication.*;
import dao.*;
import model.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;
    private AdminAuthenticationHandler adminHandler;
    private CashierAuthenticationHandler cashierHandler;

    @Override
    public void init() throws ServletException {
        // Initialize database connection and handlers
        Connection connection = DatabaseConnection.getInstance().getConnection();
        userDAO = new UserDAO(connection);
        adminHandler = new AdminAuthenticationHandler(userDAO);
        cashierHandler = new CashierAuthenticationHandler(userDAO);
        adminHandler.setNextHandler(cashierHandler);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        String result = adminHandler.handleRequest(username, password, role);
        
        if (result.equals("Success")) {
            // Store user information in session
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", role);
            
            // Redirect based on role
            if (role.equalsIgnoreCase("admin")) {
                response.sendRedirect("admin/dashboard.jsp");
            } else if (role.equalsIgnoreCase("cashier")) {
                response.sendRedirect("cashier/dashboard.jsp");
            }
        } else {
            // Set error message and redirect back to login
            request.setAttribute("error", result);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
