package dao;

import model.BatchSelection;
import model.MainStock;
import model.ShelfStock;
import strategy.BatchSelectionStrategy;
import strategy.ClosestExpiryDateStrategy;
import strategy.OldestBatchStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShelfStockDAO {
    private Connection connection;

public ShelfStockDAO(Connection connection) {
    this.connection = connection;
}

//    public void addShelfStock(ShelfStock shelfStock) {
//        String query = "INSERT INTO Shelf_Stock (item_id, shelf_capacity, current_quantity) VALUES (?, ?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setInt(1, shelfStock.getItemId());
//            stmt.setInt(2, shelfStock.getShelfCapacity());
//            stmt.setInt(3, shelfStock.getCurrentQuantity());
//            stmt.executeUpdate();
//
//            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    shelfStock.setStockId(generatedKeys.getInt(1));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
public boolean addShelfStock(ShelfStock shelfStock) {
    // Check if a shelf stock with the same item ID already exists
    if (doesShelfStockExistByItemId(shelfStock.getItemId())) {
        System.out.println("A shelf stock record with this item ID already exists.");
        return false; // Return false to indicate that the shelf stock was not added
    }

    // If it doesn't exist, proceed to add the new shelf stock
    String query = "INSERT INTO Shelf_Stock (item_id, shelf_capacity, current_quantity) VALUES (?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, shelfStock.getItemId());
        stmt.setInt(2, shelfStock.getShelfCapacity());
        stmt.setInt(3, shelfStock.getCurrentQuantity());
        stmt.executeUpdate();

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                shelfStock.setStockId(generatedKeys.getInt(1));
            }
        }
        return true; // Return true to indicate success
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Return false to indicate failure
    }
}

    public boolean doesShelfStockExistByItemId(int itemId) {
        String query = "SELECT COUNT(*) FROM Shelf_Stock WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ShelfStock> getAllShelfStocks() {
        List<ShelfStock> shelfStocks = new ArrayList<>();
        String query = "SELECT * FROM Shelf_Stock";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShelfStock shelfStock = new ShelfStock();
                shelfStock.setStockId(rs.getInt("shelf_id"));
                shelfStock.setItemId(rs.getInt("item_id"));
                shelfStock.setShelfCapacity(rs.getInt("shelf_capacity"));
                shelfStock.setCurrentQuantity(rs.getInt("current_quantity"));
                shelfStocks.add(shelfStock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shelfStocks;
    }

    public Map<ShelfStock, String> getAllShelfStocksWithItemCodes() {
        Map<ShelfStock, String> shelfStocksWithItemCodes = new HashMap<>();
        String query = "SELECT ss.shelf_id, ss.item_id, ss.shelf_capacity, ss.current_quantity, i.item_code " +
                "FROM Shelf_Stock ss JOIN Items i ON ss.item_id = i.item_id";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShelfStock shelfStock = new ShelfStock();
                shelfStock.setStockId(rs.getInt("shelf_id"));
                shelfStock.setItemId(rs.getInt("item_id"));
                shelfStock.setShelfCapacity(rs.getInt("shelf_capacity"));
                shelfStock.setCurrentQuantity(rs.getInt("current_quantity"));
                String itemCode = rs.getString("item_code");
                shelfStocksWithItemCodes.put(shelfStock, itemCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shelfStocksWithItemCodes;
    }


    public ShelfStock getShelfStockByItemId(int itemId) {
        String query = "SELECT * FROM Shelf_Stock WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ShelfStock shelfStock = new ShelfStock();
                shelfStock.setStockId(rs.getInt("shelf_id"));
                shelfStock.setItemId(rs.getInt("item_id"));
                shelfStock.setShelfCapacity(rs.getInt("shelf_capacity"));
                shelfStock.setCurrentQuantity(rs.getInt("current_quantity"));
                return shelfStock;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void deleteShelfStockByItemId(int itemId) {
        String query = "DELETE FROM Shelf_Stock WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateShelfStock(ShelfStock shelfStock) {
        String query = "UPDATE Shelf_Stock SET shelf_capacity = ?, current_quantity = ? WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, shelfStock.getShelfCapacity());
            stmt.setInt(2, shelfStock.getCurrentQuantity());
            stmt.setInt(3, shelfStock.getItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, List<BatchSelection>> getReshelvingInfo(List<ShelfStock> allShelfStocks, MainStockDAO mainStockDAO) {
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        for (ShelfStock shelfStock : allShelfStocks) {
            int itemId = shelfStock.getItemId();
            int reshelfQuantity = shelfStock.getShelfCapacity() - shelfStock.getCurrentQuantity();

            if (reshelfQuantity <= 0) {
                continue;
            }

            List<MainStock> mainStocks = mainStockDAO.getMainStocksByItemId(itemId);
            BatchSelectionStrategy strategy;

            // Determine the appropriate strategy based on whether the items have an expiry date
            boolean hasExpiryDate = mainStocks.stream().anyMatch(stock -> stock.getExpiryDate() != null);
            if (hasExpiryDate) {
                strategy = new ClosestExpiryDateStrategy();
            } else {
                strategy = new OldestBatchStrategy();
            }

            List<BatchSelection> selectedBatches = strategy.selectBatches(mainStocks, reshelfQuantity);

            if (!selectedBatches.isEmpty()) {
                reshelvingInfo.put(itemId, selectedBatches);
            }
        }
        return reshelvingInfo;
    }

    public Map<Integer, Integer> getReshelfQuantities(List<ShelfStock> allShelfStocks) {
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        for (ShelfStock shelfStock : allShelfStocks) {
            int itemId = shelfStock.getItemId();
            int reshelfQuantity = shelfStock.getShelfCapacity() - shelfStock.getCurrentQuantity();
            if (reshelfQuantity > 0) {
                reshelfQuantities.put(itemId, reshelfQuantity);
            }
        }
        return reshelfQuantities;
    }

    public void confirmReshelving(Map<Integer, List<BatchSelection>> reshelvingInfo, Map<Integer, Integer> reshelfQuantities, MainStockDAO mainStockDAO, ReorderLevelDAO reorderLevelDAO) {
        for (Map.Entry<Integer, List<BatchSelection>> entry : reshelvingInfo.entrySet()) {
            int itemId = entry.getKey();
            List<BatchSelection> selectedBatches = entry.getValue();
            ShelfStock shelfStock = getShelfStockByItemId(itemId);
            int reshelfQuantity = reshelfQuantities.get(itemId);

            shelfStock.setCurrentQuantity(shelfStock.getCurrentQuantity() + reshelfQuantity);
            updateShelfStock(shelfStock);

            for (BatchSelection selection : selectedBatches) {
                mainStockDAO.updateMainStock(selection.getBatch());
            }

            // Update the total stock in the reorder levels table
            reorderLevelDAO.updateTotalStock(itemId, reshelfQuantity);
        }
    }



}

