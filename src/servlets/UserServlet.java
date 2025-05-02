package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import controller.UserManagementController;
import dao.UserDAO;
import dao.DatabaseConnection;
import view.UserManagementView;
import java.sql.Connection;
import model.User;
import model.Admin;
import model.Cashier;
import model.Supplier;
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {
    private UserManagementController userController;
    private UserDAO userDAO;
    private UserManagementView userManagementView;

    @Override
    public void init() throws ServletException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        userDAO = new UserDAO(connection);
        userManagementView = new UserManagementView();
        userController = new UserManagementController(userDAO, userManagementView);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null) {
            // List all users
            String role = request.getParameter("role");
            if (role == null) role = "admin"; // Default to admin view
            List<User> users = userDAO.getAllUsers(role);
            request.setAttribute("users", users);
            request.setAttribute("role", role);
            request.getRequestDispatcher("/jsp/users/list.jsp").forward(request, response);
        } else if (action.equals("/add")) {
            // Show add user form
            String role = request.getParameter("role");
            if (role == null) role = "admin"; // Default to admin view
            request.setAttribute("role", role);
            request.getRequestDispatcher("/jsp/users/add.jsp").forward(request, response);
        } else if (action.equals("/edit")) {
            // Show edit user form
            String username = request.getParameter("username");
            String role = request.getParameter("role");
            if (role == null) role = "admin"; // Default to admin view
            User user = userDAO.getUserByUsername(username, role);
            request.setAttribute("user", user);
            request.setAttribute("role", role);
            request.getRequestDispatcher("/jsp/users/edit.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        String role = request.getParameter("role");
        if (role == null) role = "admin"; // Default to admin view

        if (action == null || action.equals("/add")) {
            // Add new user
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            
            User user;
            switch (role.toLowerCase()) {
                case "admin":
                    user = new Admin(username, password, email);
                    break;
                case "cashier":
                    String fullName = request.getParameter("fullName");
                    String mobile = request.getParameter("mobile");
                    user = new Cashier(username, password, fullName, email, mobile);
                    break;
                case "supplier":
                    String companyName = request.getParameter("companyName");
                    String contactPerson = request.getParameter("contactPerson");
                    mobile = request.getParameter("mobile");
                    user = new Supplier(username, password, companyName, contactPerson, email, mobile);
                    break;
                default:
                    throw new ServletException("Invalid role: " + role);
            }
            
            userDAO.addUser(user, role);
            response.sendRedirect(request.getContextPath() + "/users?role=" + role);
            
        } else if (action.equals("/edit")) {
            // Update existing user
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            
            User existingUser = userDAO.getUserByUsername(username, role);
            if (existingUser == null) {
                throw new ServletException("User not found: " + username);
            }
            
            existingUser.setPassword(password);
            existingUser.setEmail(email);
            
            if (role.equalsIgnoreCase("cashier")) {
                Cashier cashier = (Cashier) existingUser;
                cashier.setFullName(request.getParameter("fullName"));
                cashier.setMobile(request.getParameter("mobile"));
            } else if (role.equalsIgnoreCase("supplier")) {
                Supplier supplier = (Supplier) existingUser;
                supplier.setCompanyName(request.getParameter("companyName"));
                supplier.setContactPerson(request.getParameter("contactPerson"));
                supplier.setMobile(request.getParameter("mobile"));
            }
            
            userDAO.updateUser(existingUser, role);
            response.sendRedirect(request.getContextPath() + "/users?role=" + role);
            
        } else if (action.equals("/delete")) {
            // Delete user
            String username = request.getParameter("username");
            userDAO.deleteUser(username, role);
            response.sendRedirect(request.getContextPath() + "/users?role=" + role);
        }
    }
}
