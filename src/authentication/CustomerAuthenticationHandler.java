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
        
        // If this handler can't handle the request, pass it to the next handler
        if (nextHandler != null) {
            return nextHandler.handleRequest(username, password, role);
        }
        
        return "Invalid role";
    }
}
