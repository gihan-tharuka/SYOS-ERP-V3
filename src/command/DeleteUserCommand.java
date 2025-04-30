package command;

import dao.UserDAO;
import view.UserManagementView;

public class DeleteUserCommand implements Command {
    private UserDAO userDAO;
    private UserManagementView userManagementView;
    private String role;

    public DeleteUserCommand(UserDAO userDAO, UserManagementView userManagementView, String role) {
        this.userDAO = userDAO;
        this.userManagementView = userManagementView;
        this.role = role;
    }

    @Override
    public void execute() {
        String username = userManagementView.getUsernameForDeletion(role);

        if (username != null) {
            boolean success = userDAO.deleteUser(username, role);
            userManagementView.displayDeletionResult(success, role);
        } else {
            System.out.println("Failed to delete " + role + ".");
        }
    }
}

//package command;
//
//import dao.UserDAO;
//
//import java.util.Scanner;
//
//public class DeleteUserCommand implements Command {
//    private UserDAO userDAO;
//    private String role;
//
//    public DeleteUserCommand(UserDAO userDAO, String role) {
//        this.userDAO = userDAO;
//        this.role = role;
//    }
//
//    @Override
//    public void execute() {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter the username of the " + role + " to delete:");
//        String username = scanner.nextLine();
//
//        boolean success = userDAO.deleteUser(username, role);
//        if (success) {
//            System.out.println(role + " deleted successfully.");
//        } else {
//            System.out.println(role + " not found or could not be deleted.");
//        }
//    }
//}
