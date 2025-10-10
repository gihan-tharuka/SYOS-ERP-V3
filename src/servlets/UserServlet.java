package servlets;

import dao.UserDAO;
import model.User;
import model.Admin;
import model.Cashier;
import model.Supplier;
import model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO(dao.DatabaseConnection.getInstance().getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                showNewForm(req, resp);
                break;
            case "edit":
                showEditForm(req, resp);
                break;
            case "delete":
                deleteUser(req, resp);
                break;
            default:
                listUsers(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "create";

        switch (action) {
            case "create":
                createUser(req, resp);
                break;
            case "update":
                updateUser(req, resp);
                break;
            default:
                resp.sendRedirect("user");
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> userList = new java.util.ArrayList<>();
        userList.addAll(userDAO.getAllUsers("admin"));
        userList.addAll(userDAO.getAllUsers("cashier"));
        userList.addAll(userDAO.getAllUsers("supplier"));
        userList.addAll(userDAO.getAllUsers("customer"));
        req.setAttribute("userList", userList);
        req.getRequestDispatcher("/jsp/user/user-list.jsp").forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/user/user-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String role = req.getParameter("role");
        if (role == null) role = "admin"; // default or handle as needed
        User user = userDAO.getUserByUsername(username, role);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/jsp/user/user-form.jsp").forward(req, resp);
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        User user = null;
        switch (role) {
            case "admin":
                user = new Admin(username, password, email);
                break;
            case "cashier": {
                String fullName = req.getParameter("fullName");
                String mobile = req.getParameter("mobile");
                user = new Cashier(username, password, fullName, email, mobile);
                break;
            }
            case "supplier": {
                String companyName = req.getParameter("companyName");
                String contactPerson = req.getParameter("contactPerson");
                String mobile = req.getParameter("mobile");
                user = new Supplier(username, password, companyName, contactPerson, email, mobile);
                break;
            }
            case "customer": {
                String mobile = req.getParameter("mobile");
                user = new Customer(username, password, email, mobile);
                break;
            }
        }
        if (user != null) {
            userDAO.addUser(user, role);
        }
        resp.sendRedirect("user");
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        User user = null;
        switch (role) {
            case "admin":
                user = new Admin(username, password, email);
                break;
            case "cashier": {
                String fullName = req.getParameter("fullName");
                String mobile = req.getParameter("mobile");
                user = new Cashier(username, password, fullName, email, mobile);
                break;
            }
            case "supplier": {
                String companyName = req.getParameter("companyName");
                String contactPerson = req.getParameter("contactPerson");
                String mobile = req.getParameter("mobile");
                user = new Supplier(username, password, companyName, contactPerson, email, mobile);
                break;
            }
            case "customer": {
                String mobile = req.getParameter("mobile");
                user = new Customer(username, password, email, mobile);
                break;
            }
        }
        if (user != null) {
            userDAO.updateUser(user, role);
        }
        resp.sendRedirect("user");
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String role = req.getParameter("role");
        if (role == null) role = "admin"; // default or handle as needed
        userDAO.deleteUser(username, role);
        resp.sendRedirect("user");
    }
}
