package command;

import dao.UserDAO;
import model.User;
import view.UserManagementView;

import java.util.List;

public class ViewAllUsersCommand implements Command {
    private UserDAO userDAO;
    private UserManagementView userManagementView;
    private String role;

    public ViewAllUsersCommand(UserDAO userDAO, UserManagementView userManagementView, String role) {
        this.userDAO = userDAO;
        this.userManagementView = userManagementView;
        this.role = role;
    }

    @Override
    public void execute() {
        List<User> users = userDAO.getAllUsers(role);
        userManagementView.displayAllUsers(users, role);
    }
}

//package command;
//
//import dao.UserDAO;
//import model.Cashier;
//import model.Supplier;
//import model.User;
//
//import java.util.List;
//
//public class ViewAllUsersCommand implements Command {
//    private UserDAO userDAO;
//    private String role;
//
//    public ViewAllUsersCommand(UserDAO userDAO, String role) {
//        this.userDAO = userDAO;
//        this.role = role;
//    }
//
//    @Override
//    public void execute() {
//        List<User> users = userDAO.getAllUsers(role);
//        System.out.println("List of all " + role + "s:");
//        for (User user : users) {
//            if (role.equalsIgnoreCase("cashier")) {
//                Cashier cashier = (Cashier) user;
//                System.out.println("Username: " + cashier.getUsername() + ", Full Name: " + cashier.getFullName() + ", Email: " + cashier.getEmail() + ", Mobile: " + cashier.getMobile());
//            } else if (role.equalsIgnoreCase("supplier")) {
//                Supplier supplier = (Supplier) user;
//                System.out.println("Username: " + supplier.getUsername() + ", Company Name: " + supplier.getCompanyName() + ", Contact Person: " + supplier.getContactPerson() + ", Email: " + supplier.getEmail() + ", Mobile: " + supplier.getMobile());
//            } else {
//                System.out.println("Username: " + user.getUsername() + ", Email: " + user.getEmail());
//            }
//        }
//    }
//}
