package controller;

import authentication.AuthenticationHandler;
import view.UserAuthenticationView;

public class AuthenticationController {
    private UserAuthenticationView view;
    private AuthenticationHandler authHandler;
    private AdminMenuController adminMenuController;
    private CashierMenuController cashierMenuController;

    public AuthenticationController(UserAuthenticationView view,
                                    AuthenticationHandler authHandler,
                                    AdminMenuController adminMenuController,
                                    CashierMenuController cashierMenuController) {
        this.view = view;
        this.authHandler = authHandler;
        this.adminMenuController = adminMenuController;
        this.cashierMenuController = cashierMenuController;
    }

    public void authenticateUser() {
        boolean authenticated = false;
        while (!authenticated) {
            String[] credentials = view.getUserCredentials();
            String role = credentials[0];
            String username = credentials[1];
            String password = credentials[2];

            String result = authHandler.handleRequest(username, password, role);
            if (result.equals("Success")) {
                authenticated = true;
                if (role.equalsIgnoreCase("admin")) {
                    adminMenuController.handleMenu();
                } else if (role.equalsIgnoreCase("cashier")) {
                    cashierMenuController.handleMenu();
                } else {
                    System.out.println("Welcome, " + role + "!");
                }
            } else {
                System.out.println("Authentication failed: " + result);
                System.out.println("Do you want to retry? (yes/no): ");
                String retry = view.getScanner().nextLine();
                if (retry.equalsIgnoreCase("no")) {
                    break;
                }
            }
        }
    }
}
/// /////////////////////////////////////////////////////////////////////////////////////////////////
//package controller;
//
//import authentication.AuthenticationHandler;
//import view.UserAuthenticationView;
//
//public class AuthenticationController {
//    private UserAuthenticationView view;
//    private AuthenticationHandler authHandler;
//    private AdminMenuController adminMenuController;
//    private CashierMenuController cashierMenuController;
//
//    public AuthenticationController(UserAuthenticationView view,
//                                    AuthenticationHandler authHandler,
//                                    AdminMenuController adminMenuController,
//                                    CashierMenuController cashierMenuController) {
//        this.view = view;
//        this.authHandler = authHandler;
//        this.adminMenuController = adminMenuController;
//        this.cashierMenuController = cashierMenuController;
//    }
//
//    public void authenticateUser() {
//        boolean authenticated = false;
//        while (!authenticated) {
//            String[] credentials = view.getUserCredentials();
//            String role = credentials[0];
//            String username = credentials[1];
//            String password = credentials[2];
//
//            if (authHandler.handleRequest(username, password, role)) {
//                System.out.println("Authenticated successfully.");
//                authenticated = true;
//                if (role.equalsIgnoreCase("admin")) {
//                    adminMenuController.handleMenu();
//                } else if (role.equalsIgnoreCase("cashier")) {
//                    cashierMenuController.handleMenu();
//                } else {
//                    System.out.println("Welcome, " + role + "!");
//                }
//            } else {
//                System.out.println("Authentication failed. Please try again.");
//                System.out.println("Do you want to retry? (yes/no): ");
//                String retry = view.getScanner().nextLine();
//                if (retry.equalsIgnoreCase("no")) {
//                    break;
//                }
//            }
//        }
//    }
//}
/// ///////////////////////////////////////////////////////////////////////////////////
//package controller;
//
//import authentication.AuthenticationHandler;
//import view.UserAuthenticationView;
//
//public class AuthenticationController {
//    private UserAuthenticationView view;
//    private AuthenticationHandler authHandler;
//    private AdminMenuController adminMenuController;
//    private CashierMenuController cashierMenuController;
//
//    public AuthenticationController(UserAuthenticationView view,
//                                    AuthenticationHandler authHandler,
//                                    AdminMenuController adminMenuController,
//                                    CashierMenuController cashierMenuController) {
//        this.view = view;
//        this.authHandler = authHandler;
//        this.adminMenuController = adminMenuController;
//        this.cashierMenuController = cashierMenuController;
//    }
//
//    public void authenticateUser() {
//        String[] credentials = view.getUserCredentials();
//        String role = credentials[0];
//        String username = credentials[1];
//        String password = credentials[2];
//
//        if (authHandler.handleRequest(username, password, role)) {
//            System.out.println("Authenticated successfully.");
//            if (role.equalsIgnoreCase("admin")) {
//                adminMenuController.handleMenu();
//            } else if (role.equalsIgnoreCase("cashier")) {
//                cashierMenuController.handleMenu();
//            } else {
//                System.out.println("Welcome, " + role + "!");
//            }
//        } else {
//            System.out.println("Authentication failed.");
//        }
//    }
//}
//
