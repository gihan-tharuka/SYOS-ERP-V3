package dao;

import model.Admin;
import model.Cashier;
import model.Customer;
import model.Supplier;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User getUserByUsername(String username, String role) {
        String tableName;
        switch (role.toLowerCase()) {
            case "admin":
                tableName = "admins";
                break;
            case "cashier":
                tableName = "cashiers";
                break;
            case "supplier":
                tableName = "suppliers";
                break;
            case "customer":
                tableName = "customers";
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
        String query = "SELECT * FROM " + tableName + " WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                switch (role.toLowerCase()) {
                    case "admin":
                        return new Admin(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    case "cashier":
                        return new Cashier(rs.getString("username"), rs.getString("password"), rs.getString("full_name"), rs.getString("email"), rs.getString("mobile"));
                    case "supplier":
                        return new Supplier(rs.getString("username"), rs.getString("password"), rs.getString("company_name"), rs.getString("contact_person"), rs.getString("email"), rs.getString("mobile"));
                    case "customer":
                        return new Customer(rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("mobile"));
                    default:
                        return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUser(User user, String role) {
        String tableName;
        switch (role.toLowerCase()) {
            case "admin":
                tableName = "admins";
                break;
            case "cashier":
                tableName = "cashiers";
                break;
            case "supplier":
                tableName = "suppliers";
                break;
            case "customer":
                tableName = "customers";
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (username, password, email");
        if (role.equalsIgnoreCase("cashier")) {
            query.append(", full_name, mobile");
        } else if (role.equalsIgnoreCase("supplier")) {
            query.append(", company_name, contact_person, mobile");
        } else if (role.equalsIgnoreCase("customer")) {
            query.append(", mobile");
        }
        query.append(") VALUES (?, ?, ?");
        if (role.equalsIgnoreCase("cashier")) {
            query.append(", ?, ?");
        } else if (role.equalsIgnoreCase("supplier")) {
            query.append(", ?, ?, ?");
        } else if (role.equalsIgnoreCase("customer")) {
            query.append(", ?");
        }
        query.append(")");

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            if (role.equalsIgnoreCase("cashier")) {
                Cashier cashier = (Cashier) user;
                stmt.setString(4, cashier.getFullName());
                stmt.setString(5, cashier.getMobile());
            } else if (role.equalsIgnoreCase("supplier")) {
                Supplier supplier = (Supplier) user;
                stmt.setString(4, supplier.getCompanyName());
                stmt.setString(5, supplier.getContactPerson());
                stmt.setString(6, supplier.getMobile());
            } else if (role.equalsIgnoreCase("customer")) {
                Customer customer = (Customer) user;
                stmt.setString(4, customer.getMobile());
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers(String role) {
        List<User> users = new ArrayList<>();
        String tableName;
        switch (role.toLowerCase()) {
            case "admin":
                tableName = "admins";
                break;
            case "cashier":
                tableName = "cashiers";
                break;
            case "supplier":
                tableName = "suppliers";
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
        String query = "SELECT * FROM " + tableName;
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                switch (role.toLowerCase()) {
                    case "admin":
                        users.add(new Admin(rs.getString("username"), rs.getString("password"), rs.getString("email")));
                        break;
                    case "cashier":
                        users.add(new Cashier(rs.getString("username"), rs.getString("password"), rs.getString("full_name"), rs.getString("email"), rs.getString("mobile")));
                        break;
                    case "supplier":
                        users.add(new Supplier(rs.getString("username"), rs.getString("password"), rs.getString("company_name"), rs.getString("contact_person"), rs.getString("email"), rs.getString("mobile")));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void updateUser(User user, String role) {
        String tableName;
        switch (role.toLowerCase()) {
            case "admin":
                tableName = "admins";
                break;
            case "cashier":
                tableName = "cashiers";
                break;
            case "supplier":
                tableName = "suppliers";
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        String query = "UPDATE " + tableName + " SET password = ?, email = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteUser(String username, String role) {
        String tableName;
        switch (role.toLowerCase()) {
            case "admin":
                tableName = "admins";
                break;
            case "cashier":
                tableName = "cashiers";
                break;
            case "supplier":
                tableName = "suppliers";
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        String query = "DELETE FROM " + tableName + " WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer getSupplierIdByUsername(String username) {
        String query = "SELECT supplier_id FROM suppliers WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("supplier_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsernameById(int userId) {
        String query = "SELECT username FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

