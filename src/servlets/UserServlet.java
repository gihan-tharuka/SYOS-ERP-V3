package servlets;

import dao.UserDAO;
import model.User;
import model.Admin;
import model.Cashier;
import model.Supplier;
import model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
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
        List<User> userList = userDAO.getAllUsers();
        req.setAttribute("userList", userList);
        req.getRequestDispatcher("/jsp/user/user-list.jsp").forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/user/user-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        User user = userDAO.getUserByUsername(username);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/jsp/user/user-form.jsp").forward(req, resp);
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        User user = null;
        switch (role) {
            case "admin":
                user = new Admin(username, password, name, email);
                break;
            case "cashier":
                user = new Cashier(username, password, name, email);
                break;
            case "supplier":
                user = new Supplier(username, password, name, email);
                break;
            case "customer":
                user = new Customer(username, password, name, email);
                break;
        }
        if (user != null) {
            userDAO.addUser(user);
        }
        resp.sendRedirect("user");
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        User user = null;
        switch (role) {
            case "admin":
                user = new Admin(username, password, name, email);
                break;
            case "cashier":
                user = new Cashier(username, password, name, email);
                break;
            case "supplier":
                user = new Supplier(username, password, name, email);
                break;
            case "customer":
                user = new Customer(username, password, name, email);
                break;
        }
        if (user != null) {
            userDAO.updateUser(user);
        }
        resp.sendRedirect("user");
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        userDAO.deleteUser(username);
        resp.sendRedirect("user");
    }
}
