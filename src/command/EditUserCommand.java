package command;

import dao.UserDAO;
import model.User;
import view.UserManagementView;

public class EditUserCommand implements Command {
    private UserDAO userDAO;
    private UserManagementView userManagementView;
    private String role;

    public EditUserCommand(UserDAO userDAO, UserManagementView userManagementView, String role) {
        this.userDAO = userDAO;
        this.userManagementView = userManagementView;
        this.role = role;
    }

    @Override
    public void execute() {
        String username = userManagementView.getUsernameForEditing(role);

        User user = userDAO.getUserByUsername(username, role);
        if (user == null) {
            userManagementView.displayUserNotFoundMessage(role);
            return;
        }

        User updatedUser = userManagementView.collectUpdatedUserInfo(user, role);
        userDAO.updateUser(updatedUser, role);
        userManagementView.displayUserUpdatedMessage(role);
    }
}

//package command;
//
//import dao.UserDAO;
//import model.User;
//
//import java.util.Scanner;
//
//public class EditUserCommand implements Command {
//    private UserDAO userDAO;
//    private String role;
//
//    public EditUserCommand(UserDAO userDAO, String role) {
//        this.userDAO = userDAO;
//        this.role = role;
//    }
//
//    @Override
//    public void execute() {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter the username of the " + role + " to edit:");
//        String username = scanner.nextLine();
//
//        User user = userDAO.getUserByUsername(username, role);
//        if (user == null) {
//            System.out.println(role + " not found.");
//            return;
//        }
//
//        System.out.println("Editing " + role + " details:");
//        System.out.println("Leave field empty to keep the current value.");
//
//        System.out.println("New password (current: " + user.getPassword() + "):");
//        String password = scanner.nextLine();
//        if (!password.isEmpty()) user.setPassword(password);
//
//        System.out.println("New email (current: " + user.getEmail() + "):");
//        String email = scanner.nextLine();
//        if (!email.isEmpty()) user.setEmail(email);
//
//        userDAO.updateUser(user, role);
//        System.out.println(role + " updated successfully.");
//    }
//}
