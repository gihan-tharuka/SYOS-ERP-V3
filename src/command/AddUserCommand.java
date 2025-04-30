package command;

import dao.UserDAO;
import model.Admin;
import model.Cashier;
import model.Supplier;
import model.User;
import view.UserManagementView;

public class AddUserCommand implements Command {
    private UserDAO userDAO;
    private UserManagementView userManagementView;
    private String role;

    public AddUserCommand(UserDAO userDAO, UserManagementView userManagementView, String role) {
        this.userDAO = userDAO;
        this.userManagementView = userManagementView;
        this.role = role;
    }

    @Override
    public void execute() {
        User user = userManagementView.collectUserInfo(role);

        if (user != null) {
            userDAO.addUser(user, role);
            System.out.println("New " + role + " added successfully.");
        } else {
            System.out.println("Failed to add new " + role + ".");
        }
    }
}

//package command;
//
//import dao.UserDAO;
//import model.Admin;
//import model.Cashier;
//import model.Supplier;
//import model.User;
//
//import java.util.Scanner;
//
//public class AddUserCommand implements Command {
//    private UserDAO userDAO;
//    private Scanner scanner = new Scanner(System.in);
//    private String role;
//
//    public AddUserCommand(UserDAO userDAO, String role) {
//        this.userDAO = userDAO;
//        this.role = role;
//    }
//
//    @Override
//    public void execute() {
//        System.out.println("Enter new " + role + " username: ");
//        String username = scanner.nextLine();
//
//        System.out.println("Enter new " + role + " password: ");
//        String password = scanner.nextLine();
//
//        System.out.println("Enter new " + role + " email: ");
//        String email = scanner.nextLine();
//
//        User user;
//        if (role.equalsIgnoreCase("cashier")) {
//            System.out.println("Enter new cashier full name: ");
//            String fullName = scanner.nextLine();
//
//            System.out.println("Enter new cashier mobile: ");
//            String mobile = scanner.nextLine();
//
//            user = new Cashier(username, password, fullName, email, mobile);
//        } else if (role.equalsIgnoreCase("supplier")) {
//            System.out.println("Enter new supplier company name: ");
//            String companyName = scanner.nextLine();
//
//            System.out.println("Enter new supplier contact person: ");
//            String contactPerson = scanner.nextLine();
//
//            System.out.println("Enter new supplier mobile: ");
//            String mobile = scanner.nextLine();
//
//            user = new Supplier(username, password, companyName, contactPerson, email, mobile);
//        } else {
//            user = new Admin(username, password, email);
//        }
//
//        userDAO.addUser(user, role);
//        System.out.println("New " + role + " added successfully.");
//    }
//}
