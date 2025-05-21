package servlets;

import dao.DatabaseConnection;
import dao.UserDAO;
import model.Customer;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/customer/register")
public class CustomerRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        try {
            // Get database connection from singleton
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            if (!dbConnection.isConnectionValid()) {
                throw new ServletException("Database connection is not valid");
            }
            
            userDAO = new UserDAO(dbConnection.getConnection());
        } catch (Exception e) {
            throw new ServletException("Error initializing CustomerRegisterServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        try {
            // Create new customer
            User customer = new Customer(username, password, email, mobile);
            
            // Add customer directly using UserDAO
            userDAO.addUser(customer, "customer");

            // Set session attributes
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", "customer");

            // Redirect to customer dashboard
            response.sendRedirect(request.getContextPath() + "/jsp/customer/dashboard.jsp");
        } catch (Exception e) {
            // Handle registration error
            request.setAttribute("error", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("/jsp/customer/register.jsp").forward(request, response);
        }
    }
}
