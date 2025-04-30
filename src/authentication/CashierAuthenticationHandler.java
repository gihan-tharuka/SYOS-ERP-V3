package authentication;

import dao.UserDAO;
import model.User;

public class CashierAuthenticationHandler extends AuthenticationHandler {
    private UserDAO userDAO;

    public CashierAuthenticationHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public String handleRequest(String username, String password, String role) {
        if (role.equalsIgnoreCase("cashier")) {
            User user = userDAO.getUserByUsername(username, role);
            if (user == null) {
                return "Invalid username.";
            } else if (!user.getPassword().equals(password)) {
                return "Invalid password.";
            } else {
                System.out.println("Login successful! Navigating to Cashier interface.");
                return "Success";
            }
        }
        if (nextHandler != null) {
            return nextHandler.handleRequest(username, password, role);
        }
        return "Invalid role.";
    }
}

//package authentication;
//
//import dao.UserDAO;
//import model.User;
//
//public class CashierAuthenticationHandler extends AuthenticationHandler {
//    private UserDAO userDAO;
//
//    public CashierAuthenticationHandler(UserDAO userDAO) {
//        this.userDAO = userDAO;
//    }
//
//    @Override
//    public boolean handleRequest(String username, String password, String role) {
//        if (role.equalsIgnoreCase("cashier")) {
//            User user = userDAO.getUserByUsername(username, role);
//            if (user != null && user.getPassword().equals(password)) {
//                System.out.println("Login successful! Navigating to Cashier interface.");
//                return true;
//            }
//        }
//        if (nextHandler != null) {
//            return nextHandler.handleRequest(username, password, role);
//        }
//        return false;
//    }
//}
//
