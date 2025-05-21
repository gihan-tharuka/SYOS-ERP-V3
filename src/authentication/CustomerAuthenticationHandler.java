package authentication;

import dao.UserDAO;
import model.Customer;
import model.User;

public class CustomerAuthenticationHandler extends AuthenticationHandler {
    private UserDAO userDAO;

    public CustomerAuthenticationHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public String handleRequest(String username, String password, String role) {
        if (role.equalsIgnoreCase("customer")) {
            User user = userDAO.getUserByUsername(username, role);
            if (user != null && user.getPassword().equals(password)) {
                return "Success";
            }
            return "Invalid username or password";
        }
        return super.handleRequest(username, password, role);
    }
}
