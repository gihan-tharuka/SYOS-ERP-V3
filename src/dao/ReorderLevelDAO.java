package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import model.ReorderLevel;
import model.Item;

public class ReorderLevelDAO {
    private Connection connection;

    public ReorderLevelDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Map<String, Object>> getAllReorderLevels() {
        List<Map<String, Object>> reorderLevels = new ArrayList<>();
        String query = "SELECT rl.*, i.item_code, i.item_name FROM reorder_levels rl " +
                      "JOIN items i ON rl.item_id = i.item_id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> level = new HashMap<>();
                level.put("reorderLevelId", rs.getInt("reorder_level_id"));
                level.put("itemId", rs.getInt("item_id"));
                level.put("itemCode", rs.getString("item_code"));
                level.put("itemName", rs.getString("item_name"));
                level.put("thresholdQuantity", rs.getInt("threshold_quantity"));
                level.put("totalStock", rs.getInt("total_stock"));
                reorderLevels.add(level);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reorderLevels;
    }

    public List<Item> getAvailableItems() {
        List<Item> items = new ArrayList<>();
        String query = "SELECT i.* FROM items i " +
                      "LEFT JOIN reorder_levels rl ON i.item_id = rl.item_id " +
                      "WHERE rl.item_id IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_code"),
                    rs.getString("item_name"),
                    rs.getDouble("price"),
                    rs.getDouble("discount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public ReorderLevel getReorderLevelById(int reorderLevelId) {
        String query = "SELECT * FROM reorder_levels WHERE reorder_level_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reorderLevelId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ReorderLevel(
                    rs.getInt("reorder_level_id"),
                    rs.getInt("item_id"),
                    rs.getInt("threshold_quantity"),
                    rs.getInt("total_stock")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addReorderLevel(int itemId, int thresholdQuantity) {
        String query = "INSERT INTO reorder_levels (item_id, threshold_quantity, total_stock) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.setInt(2, thresholdQuantity);
            stmt.setInt(3, 0); // Default total_stock
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReorderLevel(int reorderLevelId, int thresholdQuantity) {
        String query = "UPDATE reorder_levels SET threshold_quantity = ? WHERE reorder_level_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, thresholdQuantity);
            stmt.setInt(2, reorderLevelId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReorderLevel(int reorderLevelId) {
        String query = "DELETE FROM reorder_levels WHERE reorder_level_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reorderLevelId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTotalStock(int itemId, int quantity) {
        String query = "UPDATE reorder_levels SET total_stock = total_stock - ? WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
