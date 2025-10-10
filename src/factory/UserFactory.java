package factory;

import model.Admin;
import model.Cashier;
import model.Customer;
import model.Supplier;
import model.User;

public class UserFactory {
    public static User createUser(String role, String username, String password, String email, String fullName, String mobile, String companyName, String contactPerson) {
        switch (role.toLowerCase()) {
            case "admin":
                return new Admin(username, password, email);
            case "cashier":
                return new Cashier(username, password, fullName, email, mobile);
            case "supplier":
                return new Supplier(username, password, companyName, contactPerson, email, mobile);
            case "customer":
                return new Customer(username, password, email, mobile);
            default:
                return null;
        }
    }
}

