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
import java.util.concurrent.*;
import java.util.logging.*;
import authentication.*;
import dao.*;
import model.*;

@WebServlet(urlPatterns = "/login", asyncSupported = true)
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private volatile UserDAO userDAO;
    private AdminAuthenticationHandler adminHandler;
    private CashierAuthenticationHandler cashierHandler;
    private CustomerAuthenticationHandler customerHandler;

    // Thread-safe queue to hold login requests
    private final BlockingQueue<LoginTask> loginQueue = new LinkedBlockingQueue<>();
    private final ExecutorService workerThreadPool = Executors.newFixedThreadPool(5);

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            this.userDAO = new UserDAO(connection);
            this.adminHandler = new AdminAuthenticationHandler(userDAO);
            this.cashierHandler = new CashierAuthenticationHandler(userDAO);
            this.customerHandler = new CustomerAuthenticationHandler(userDAO);
            
            adminHandler.setNextHandler(cashierHandler);
            cashierHandler.setNextHandler(customerHandler);

            // Start worker threads to process the queue
            for (int i = 0; i < 5; i++) {
                workerThreadPool.submit(this::processLoginQueue);
            }
            logger.info("LoginServlet initialized successfully");
        } catch (Exception e) {
            logger.severe("Error initializing LoginServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize LoginServlet", e);
        }
    }

    // Worker thread: Processes queued login requests
    private void processLoginQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                LoginTask task = loginQueue.take(); // Blocks if queue is empty
                handleLogin(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.severe("Error processing login queue: " + e.getMessage());
            }
        }
    }

    // Encapsulates a login request/response pair
    private static class LoginTask {
        final AsyncContext asyncContext;
        final String username;
        final String password;
        final String role;

        LoginTask(AsyncContext asyncContext, String username, String password, String role) {
            this.asyncContext = asyncContext;
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            if (username == null || password == null || role == null) {
                request.setAttribute("error", "Missing required login information");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // Start async context
            AsyncContext asyncContext = request.startAsync();
            asyncContext.setTimeout(10000); // 30 second timeout

            // Add the request to the queue
            loginQueue.add(new LoginTask(asyncContext, username, password, role));
            
            // Send immediate response
            response.setContentType("text/plain");
            response.getWriter().write("Login request accepted. Processing...");
            
        } catch (Exception e) {
            logger.severe("Error in doPost: " + e.getMessage());
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    // Actual login logic (called by worker threads)
    private void handleLogin(LoginTask task) {
        try {
            String result = adminHandler.handleRequest(task.username, task.password, task.role);
            
            if (result.equals("Success")) {
                HttpServletRequest request = (HttpServletRequest) task.asyncContext.getRequest();
                HttpServletResponse response = (HttpServletResponse) task.asyncContext.getResponse();
                
                HttpSession session = request.getSession();
                session.setAttribute("username", task.username);
                session.setAttribute("role", task.role);
                
                String redirectPath = getRedirectPath(task.role);
                logger.info("Login successful for user: " + task.username + ", redirecting to: " + redirectPath);
                response.sendRedirect(redirectPath);
            } else {
                logger.warning("Login failed for user: " + task.username + ", reason: " + result);
                HttpServletRequest request = (HttpServletRequest) task.asyncContext.getRequest();
                HttpServletResponse response = (HttpServletResponse) task.asyncContext.getResponse();
                
                request.setAttribute("error", result);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.severe("Error handling login: " + e.getMessage());
            try {
                HttpServletRequest request = (HttpServletRequest) task.asyncContext.getRequest();
                HttpServletResponse response = (HttpServletResponse) task.asyncContext.getResponse();
                request.setAttribute("error", "Internal server error: " + e.getMessage());
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.severe("Error forwarding to login page: " + ex.getMessage());
            }
        } finally {
            task.asyncContext.complete();
        }
    }

    private String getRedirectPath(String role) {
        if (role.equalsIgnoreCase("admin")) {
            return "admin/dashboard.jsp";
        } else if (role.equalsIgnoreCase("cashier")) {
            return "cashier/dashboard.jsp";
        } else if (role.equalsIgnoreCase("customer")) {
            return "jsp/customer/dashboard.jsp";
        }
        return "login.jsp";
    }

    @Override
    public void destroy() {
        logger.info("Shutting down LoginServlet");
        workerThreadPool.shutdown();
        try {
            if (!workerThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                workerThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            workerThreadPool.shutdownNow();
        }
    }
}

