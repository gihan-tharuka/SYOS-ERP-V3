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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import authentication.*;
import dao.*;
import model.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private volatile UserDAO userDAO;
    private AdminAuthenticationHandler adminHandler;
    private CashierAuthenticationHandler cashierHandler;
    private CustomerAuthenticationHandler customerHandler;
    private final BlockingQueue<LoginTask> loginQueue = new LinkedBlockingQueue<>();
    private final ExecutorService workerThreadPool = Executors.newFixedThreadPool(5);

    @Override
    public void init() throws ServletException {
        try {
            this.userDAO = new UserDAO();
            this.adminHandler = new AdminAuthenticationHandler(userDAO);
            this.cashierHandler = new CashierAuthenticationHandler(userDAO);
            this.customerHandler = new CustomerAuthenticationHandler(userDAO);
            
            adminHandler.setNextHandler(cashierHandler);
            cashierHandler.setNextHandler(customerHandler);

            // Start worker threads
            for (int i = 0; i < 5; i++) {
                workerThreadPool.submit(this::processLoginQueue);
            }
        } catch (Exception e) {
            throw new ServletException("Failed to initialize servlet", e);
        }
    }

    private void processLoginQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                LoginTask task = loginQueue.take();
                processLoginTask(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void processLoginTask(LoginTask task) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            userDAO.setConnection(connection);
            
            String username = task.request.getParameter("username");
            String password = task.request.getParameter("password");
            String role = task.request.getParameter("role");

            String result = adminHandler.handleRequest(username, password, role);
            
            if (result.equals("Success")) {
                handleSuccessfulLogin(task, username, role);
            } else {
                handleFailedLogin(task, result);
            }
        } catch (SQLException e) {
            handleDatabaseError(task, e);
        } finally {
            if (connection != null) {
                DatabaseConnection.getInstance().releaseConnection(connection);
            }
        }
    }

    private void handleSuccessfulLogin(LoginTask task, String username, String role) 
            throws IOException {
        HttpSession session = task.request.getSession();
        session.setAttribute("username", username);
        session.setAttribute("role", role);
        
        String redirectPath = getRedirectPath(role);
        task.response.sendRedirect(redirectPath);
    }

    private String getRedirectPath(String role) {
        if (role.equalsIgnoreCase("admin")) {
            return "admin/dashboard.jsp";
        } else if (role.equalsIgnoreCase("cashier")) {
            return "cashier/dashboard.jsp";
        } else {
            return "jsp/customer/dashboard.jsp";
        }
    }

    private void handleFailedLogin(LoginTask task, String error) {
        try {
            task.request.setAttribute("error", error);
            task.request.getRequestDispatcher("login.jsp").forward(task.request, task.response);
        } catch (Exception e) {
            sendErrorResponse(task.response, "Login processing failed");
        }
    }

    private void handleDatabaseError(LoginTask task, SQLException e) {
        DatabaseConnection.getInstance().handleSQLException(e);
        try {
            task.request.setAttribute("error", "Database error occurred. Please try again.");
            task.request.getRequestDispatcher("login.jsp").forward(task.request, task.response);
        } catch (Exception ex) {
            sendErrorResponse(task.response, "Database error");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) {
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        loginQueue.add(new LoginTask(request, response));
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        response.getWriter().write("Login request queued. Processing...");
    }

    @Override
    public void destroy() {
        workerThreadPool.shutdownNow();
        DatabaseConnection.getInstance().shutdown();
    }

    private static class LoginTask {
        final HttpServletRequest request;
        final HttpServletResponse response;
        
        LoginTask(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }
    }
}