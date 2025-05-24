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
        String sql = "SELECT rl.*, i.item_code, i.item_name, " +
                    "(SELECT COALESCE(SUM(quantity), 0) FROM main_stock WHERE item_id = i.item_id) as total_stock " +
                    "FROM reorder_level rl " +
                    "JOIN items i ON rl.item_id = i.item_id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> reorderLevel = new HashMap<>();
                reorderLevel.put("reorderLevelId", rs.getInt("reorder_level_id"));
                reorderLevel.put("itemId", rs.getInt("item_id"));
                reorderLevel.put("thresholdQuantity", rs.getInt("threshold_quantity"));
                reorderLevel.put("itemCode", rs.getString("item_code"));
                reorderLevel.put("itemName", rs.getString("item_name"));
                reorderLevel.put("totalStock", rs.getInt("total_stock"));
                reorderLevels.add(reorderLevel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reorderLevels;
    }

    public List<Item> getAvailableItems() {
        List<Item> items = new ArrayList<>();
        String query = "SELECT i.* FROM items i " +
                      "LEFT JOIN reorder_level rl ON i.item_id = rl.item_id " +
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
        String query = "SELECT rl.*, i.item_code, i.item_name, " +
                      "(SELECT COALESCE(SUM(quantity), 0) FROM main_stock WHERE item_id = i.item_id) as total_stock " +
                      "FROM reorder_level rl " +
                      "JOIN items i ON rl.item_id = i.item_id " +
                      "WHERE rl.reorder_level_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reorderLevelId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ReorderLevel(
                    rs.getInt("reorder_level_id"),
                    rs.getInt("item_id"),
                    rs.getInt("threshold_quantity"),
                    rs.getString("item_code"),
                    rs.getString("item_name"),
                    rs.getInt("total_stock")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addReorderLevel(int itemId, int thresholdQuantity) {
        String query = "INSERT INTO reorder_level (item_id, threshold_quantity) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.setInt(2, thresholdQuantity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReorderLevel(int reorderLevelId, int thresholdQuantity) {
        String query = "UPDATE reorder_level SET threshold_quantity = ? WHERE reorder_level_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, thresholdQuantity);
            stmt.setInt(2, reorderLevelId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReorderLevel(int reorderLevelId) {
        String query = "DELETE FROM reorder_level WHERE reorder_level_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reorderLevelId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTotalStock(int itemId, int quantity) {
        String query = "UPDATE reorder_level SET total_stock = total_stock - ? WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReorderLevel getReorderLevelByItemId(int itemId) {
        String sql = "SELECT rl.*, i.item_code, i.item_name, " +
                    "(SELECT COALESCE(SUM(quantity), 0) FROM main_stock WHERE item_id = i.item_id) as total_stock " +
                    "FROM reorder_level rl " +
                    "JOIN items i ON rl.item_id = i.item_id " +
                    "WHERE rl.item_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                ReorderLevel reorderLevel = new ReorderLevel();
                reorderLevel.setReorderLevelId(rs.getInt("reorder_level_id"));
                reorderLevel.setItemId(rs.getInt("item_id"));
                reorderLevel.setThresholdQuantity(rs.getInt("threshold_quantity"));
                reorderLevel.setItemCode(rs.getString("item_code"));
                reorderLevel.setItemName(rs.getString("item_name"));
                reorderLevel.setTotalStock(rs.getInt("total_stock"));
                return reorderLevel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getReorderLevelIdByItemId(int itemId) {
        String sql = "SELECT reorder_level_id FROM reorder_level WHERE item_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("reorder_level_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
