package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReorderLevelDAO {
    private Connection connection;

public ReorderLevelDAO(Connection connection) {
    this.connection = connection;
}

    public void addReorderLevel(int itemId) {
        String query = "INSERT INTO reorder_levels (item_id, threshold_quantity, total_stock) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.setInt(2, 50); // Default threshold_quantity
            stmt.setInt(3, 0); // Default total_stock
            stmt.executeUpdate();
            System.out.println("Reorder level added for item ID: " + itemId);
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
            System.out.println("Total stock updated for item ID: " + itemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
