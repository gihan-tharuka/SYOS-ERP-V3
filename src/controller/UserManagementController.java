package controller;

import command.*;
import dao.UserDAO;
import view.UserManagementView;

import java.util.Map;

public class UserManagementController {
    private Map<Integer, Command> adminCommands;
    private Map<Integer, Command> cashierCommands;
    private Map<Integer, Command> supplierCommands;

    public UserManagementController(UserDAO userDAO, UserManagementView userManagementView) {
        this.adminCommands = Map.of(
                1, new AddUserCommand(userDAO, userManagementView, "admin"),
                2, new ViewAllUsersCommand(userDAO, userManagementView, "admin"),
                3, new EditUserCommand(userDAO, userManagementView, "admin"),
                4, new DeleteUserCommand(userDAO, userManagementView, "admin")
        );

        this.cashierCommands = Map.of(
                1, new AddUserCommand(userDAO, userManagementView, "cashier"),
                2, new ViewAllUsersCommand(userDAO, userManagementView, "cashier"),
                3, new EditUserCommand(userDAO, userManagementView, "cashier"),
                4, new DeleteUserCommand(userDAO, userManagementView, "cashier")
        );

        this.supplierCommands = Map.of(
                1, new AddUserCommand(userDAO, userManagementView, "supplier"),
                2, new ViewAllUsersCommand(userDAO, userManagementView, "supplier"),
                3, new EditUserCommand(userDAO, userManagementView, "supplier"),
                4, new DeleteUserCommand(userDAO, userManagementView, "supplier")
        );
    }

    public void handleUserManagement(String role, int choice) {
        Map<Integer, Command> commands;
        switch (role.toLowerCase()) {
            case "admin":
                commands = adminCommands;
                break;
            case "cashier":
                commands = cashierCommands;
                break;
            case "supplier":
                commands = supplierCommands;
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        if (commands.containsKey(choice)) {
            commands.get(choice).execute();
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}

//package controller;
//
//import command.*;
//import dao.UserDAO;
//import view.UserManagementView;
//
//import java.util.Map;
//
//public class UserManagementController {
//    private Map<Integer, Command> adminCommands;
//    private Map<Integer, Command> cashierCommands;
//    private Map<Integer, Command> supplierCommands;
//
//    public UserManagementController(UserDAO userDAO, UserManagementView userManagementView) {
//        this.adminCommands = Map.of(
//                1, new AddUserCommand(userDAO, userManagementView, "admin"),
//                2, new ViewAllUsersCommand(userDAO, userManagementView, "admin"),
//                3, new EditUserCommand(userDAO, "admin"),
//                4, new DeleteUserCommand(userDAO, userManagementView, "admin")
//        );
//
//        this.cashierCommands = Map.of(
//                1, new AddUserCommand(userDAO, userManagementView, "cashier"),
//                2, new ViewAllUsersCommand(userDAO, userManagementView, "cashier"),
//                3, new EditUserCommand(userDAO, "cashier"),
//                4, new DeleteUserCommand(userDAO, userManagementView, "cashier")
//        );
//
//        this.supplierCommands = Map.of(
//                1, new AddUserCommand(userDAO, userManagementView, "supplier"),
//                2, new ViewAllUsersCommand(userDAO, userManagementView, "supplier"),
//                3, new EditUserCommand(userDAO, "supplier"),
//                4, new DeleteUserCommand(userDAO, userManagementView, "supplier")
//        );
//    }
//
//    public void handleUserManagement(String role, int choice) {
//        Map<Integer, Command> commands;
//        switch (role.toLowerCase()) {
//            case "admin":
//                commands = adminCommands;
//                break;
//            case "cashier":
//                commands = cashierCommands;
//                break;
//            case "supplier":
//                commands = supplierCommands;
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid role: " + role);
//        }
//
//        if (commands.containsKey(choice)) {
//            commands.get(choice).execute();
//        } else {
//            System.out.println("Invalid choice. Please try again.");
//        }
//    }
//}

//package controller;
//
//import command.*;
//import dao.UserDAO;
//import view.UserManagementView;
//
//import java.util.Map;
//
//public class UserManagementController {
//    private Map<Integer, Command> adminCommands;
//    private Map<Integer, Command> cashierCommands;
//    private Map<Integer, Command> supplierCommands;
//
//    public UserManagementController(UserDAO userDAO, UserManagementView userManagementView) {
//        this.adminCommands = Map.of(
//                1, new AddUserCommand(userDAO, userManagementView, "admin"),
//                2, new ViewAllUsersCommand(userDAO, "admin"),
//                3, new EditUserCommand(userDAO, "admin"),
//                4, new DeleteUserCommand(userDAO, userManagementView, "admin")
//        );
//
//        this.cashierCommands = Map.of(
//                1, new AddUserCommand(userDAO, userManagementView, "cashier"),
//                2, new ViewAllUsersCommand(userDAO, "cashier"),
//                3, new EditUserCommand(userDAO, "cashier"),
//                4, new DeleteUserCommand(userDAO, userManagementView, "cashier")
//        );
//
//        this.supplierCommands = Map.of(
//                1, new AddUserCommand(userDAO, userManagementView, "supplier"),
//                2, new ViewAllUsersCommand(userDAO, "supplier"),
//                3, new EditUserCommand(userDAO, "supplier"),
//                4, new DeleteUserCommand(userDAO, userManagementView, "supplier")
//        );
//    }
//
//    public void handleUserManagement(String role, int choice) {
//        Map<Integer, Command> commands;
//        switch (role.toLowerCase()) {
//            case "admin":
//                commands = adminCommands;
//                break;
//            case "cashier":
//                commands = cashierCommands;
//                break;
//            case "supplier":
//                commands = supplierCommands;
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid role: " + role);
//        }
//
//        if (commands.containsKey(choice)) {
//            commands.get(choice).execute();
//        } else {
//            System.out.println("Invalid choice. Please try again.");
//        }
//    }
//}

//package controller;
//
//import command.*;
//import dao.UserDAO;
//import view.UserManagementView;
//
//import java.util.Map;
//
//public class UserManagementController {
//    private Map<Integer, Command> adminCommands;
//    private Map<Integer, Command> cashierCommands;
//    private Map<Integer, Command> supplierCommands;
//
//    public UserManagementController(UserDAO userDAO, UserManagementView userManagementView) {
//        this.adminCommands = Map.of(
//                1, new AddUserCommand(userDAO, userManagementView, "admin"),
//                2, new ViewAllUsersCommand(userDAO, "admin"),
//                3, new EditUserCommand(userDAO, "admin"),
//                4, new DeleteUserCommand(userDAO, "admin")
//        );
//
//        this.cashierCommands = Map.of(
//                1, new AddUserCommand(userDAO, userManagementView, "cashier"),
//                2, new ViewAllUsersCommand(userDAO, "cashier"),
//                3, new EditUserCommand(userDAO, "cashier"),
//                4, new DeleteUserCommand(userDAO, "cashier")
//        );
//
//        this.supplierCommands = Map.of(
//                1, new AddUserCommand(userDAO, userManagementView, "supplier"),
//                2, new ViewAllUsersCommand(userDAO, "supplier"),
//                3, new EditUserCommand(userDAO, "supplier"),
//                4, new DeleteUserCommand(userDAO, "supplier")
//        );
//    }
//
//    public void handleUserManagement(String role, int choice) {
//        Map<Integer, Command> commands;
//        switch (role.toLowerCase()) {
//            case "admin":
//                commands = adminCommands;
//                break;
//            case "cashier":
//                commands = cashierCommands;
//                break;
//            case "supplier":
//                commands = supplierCommands;
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid role: " + role);
//        }
//
//        if (commands.containsKey(choice)) {
//            commands.get(choice).execute();
//        } else {
//            System.out.println("Invalid choice. Please try again.");
//        }
//    }
//}

//package controller;
//
//import command.*;
//import dao.UserDAO;
//
//import java.util.Map;
//
//public class UserManagementController {
//    private Map<Integer, Command> adminCommands;
//    private Map<Integer, Command> cashierCommands;
//    private Map<Integer, Command> supplierCommands;
//
//    public UserManagementController(UserDAO userDAO) {
//        this.adminCommands = Map.of(
//                1, new AddUserCommand(userDAO, "admin"),
//                2, new ViewAllUsersCommand(userDAO, "admin"),
//                3, new EditUserCommand(userDAO, "admin"),
//                4, new DeleteUserCommand(userDAO, "admin")
//        );
//
//        this.cashierCommands = Map.of(
//                1, new AddUserCommand(userDAO, "cashier"),
//                2, new ViewAllUsersCommand(userDAO, "cashier"),
//                3, new EditUserCommand(userDAO, "cashier"),
//                4, new DeleteUserCommand(userDAO, "cashier")
//        );
//
//        this.supplierCommands = Map.of(
//                1, new AddUserCommand(userDAO, "supplier"),
//                2, new ViewAllUsersCommand(userDAO, "supplier"),
//                3, new EditUserCommand(userDAO, "supplier"),
//                4, new DeleteUserCommand(userDAO, "supplier")
//        );
//    }
//
//
//    public void handleUserManagement(String role, int choice) {
//        Map<Integer, Command> commands;
//        switch (role.toLowerCase()) {
//            case "admin":
//                commands = adminCommands;
//                break;
//            case "cashier":
//                commands = cashierCommands;
//                break;
//            case "supplier":
//                commands = supplierCommands;
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid role: " + role);
//        }
//
//        if (commands.containsKey(choice)) {
//            commands.get(choice).execute();
//        } else {
//            System.out.println("Invalid choice. Please try again.");
//        }
//    }
//}
//
