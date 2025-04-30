package dao;

import model.Item;
import observer.ReorderSubject;

import java.sql.*;
import java.util.*;

public class ItemDAO {
    private Connection connection;
    private ReorderSubject reorderSubject;

public ItemDAO(Connection connection, ReorderSubject reorderSubject) {
    this.connection = connection;
    this.reorderSubject = reorderSubject;
}

    public void addItem(Item item) {
        String query = "INSERT INTO items (item_code, item_name, price, discount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getItemCode());
            stmt.setString(2, item.getItemName());
            stmt.setDouble(3, item.getPrice());
            stmt.setDouble(4, item.getDiscount());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int itemId = generatedKeys.getInt(1);
                    item = new Item(itemId, item.getItemCode(), item.getItemName(), item.getPrice(), item.getDiscount());
                    reorderSubject.itemAdded(item);// not working properly
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM items";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                items.add(new Item(rs.getInt("item_id"), rs.getString("item_code"), rs.getString("item_name"), rs.getDouble("price"), rs.getDouble("discount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public String getItemNameById(int itemId) {
        String query = "SELECT item_name FROM Items WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("item_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getItemCodeById(int itemId) {
        String query = "SELECT item_code FROM Items WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("item_code");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Item getItemByCode(String itemCode) {
        String query = "SELECT * FROM Items WHERE item_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, itemCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Item(
                        rs.getInt("item_id"),
                        rs.getString("item_code"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getDouble("discount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteItemByCode(String itemCode) {
        String query = "DELETE FROM Items WHERE item_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, itemCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItem(Item item) {
        String query = "UPDATE Items SET item_name = ?, price = ?, discount = ? WHERE item_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, item.getItemName());
            stmt.setDouble(2, item.getPrice());
            stmt.setDouble(3, item.getDiscount());
            stmt.setString(4, item.getItemCode());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getItemIdByCode(String itemCode) {
        String query = "SELECT item_id FROM Items WHERE item_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, itemCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("item_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the item code is invalid
    }
    public Map<Integer, String> getItemNamesByItemIds(Set<Integer> itemIds) {
        Map<Integer, String> itemNames = new HashMap<>();
        if (itemIds.isEmpty()) {
            return itemNames;
        }
        String query = "SELECT item_id, item_name FROM Items WHERE item_id IN (" +
                String.join(",", itemIds.stream().map(String::valueOf).toArray(String[]::new)) + ")";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                itemNames.put(rs.getInt("item_id"), rs.getString("item_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemNames;
    }
}
