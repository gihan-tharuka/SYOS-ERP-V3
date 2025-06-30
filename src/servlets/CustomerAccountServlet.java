package servlets;

import model.Customer;
import dao.UserDAO;
import dao.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/customer-account")
public class CustomerAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null || !"customer".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        java.sql.Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            UserDAO userDAO = new UserDAO(conn);
            Customer customer = (Customer) userDAO.getUserByUsername(username, "customer");
            if (customer != null) {
                request.setAttribute("username", customer.getUsername());
                request.setAttribute("email", customer.getEmail());
                request.setAttribute("mobile", customer.getMobile());
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        } finally {
            if (conn != null) {
                DatabaseConnection.getInstance().releaseConnection(conn);
            }
        }
        request.getRequestDispatcher("jsp/customer/account.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null || !"customer".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String currentUsername = (String) session.getAttribute("username");

        java.sql.Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            UserDAO userDAO = new UserDAO(conn);
            boolean updated = userDAO.updateCustomerAccount(currentUsername, username, password, email, mobile);
            if (updated) {
                session.setAttribute("username", username);
                session.setAttribute("email", email);
                session.setAttribute("mobile", mobile);
                request.setAttribute("message", "Account updated successfully.");
            } else {
                request.setAttribute("error", "Failed to update account. Please try again.");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        } finally {
            if (conn != null) {
                DatabaseConnection.getInstance().releaseConnection(conn);
            }
        }
        // After update, show the latest info
        doGet(request, response);
    }
} 