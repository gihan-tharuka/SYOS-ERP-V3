package dao;

import model.MainStock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainStockDAO {
    private Connection connection;

public MainStockDAO(Connection connection) {
    this.connection = connection;
}

    public void addMainStock(MainStock mainStock) {
        String query = "INSERT INTO Main_Stock (item_id, supplier_id, batch_code, purchase_date, purchase_price, quantity, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, mainStock.getItemId());
            stmt.setInt(2, mainStock.getSupplierId());
            stmt.setString(3, mainStock.getBatchCode());
            stmt.setDate(4, new java.sql.Date(mainStock.getPurchaseDate().getTime()));
            stmt.setDouble(5, mainStock.getPurchasePrice());
            stmt.setInt(6, mainStock.getQuantity());

            // Handle NULL expiry date
            if (mainStock.getExpiryDate() != null) {
                stmt.setDate(7, new java.sql.Date(mainStock.getExpiryDate().getTime()));
            } else {
                stmt.setNull(7, java.sql.Types.DATE);
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mainStock.setStockId(generatedKeys.getInt(1));
                    adjustTotalStock(mainStock.getItemId(), mainStock.getQuantity());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doesMainStockExist(int itemId, String batchCode) {
        String query = "SELECT COUNT(*) FROM Main_Stock WHERE item_id = ? AND batch_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.setString(2, batchCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public List<MainStock> getAllMainStocks() {
        List<MainStock> mainStocks = new ArrayList<>();
        String query = "SELECT * FROM Main_Stock";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                MainStock mainStock = new MainStock();
                mainStock.setStockId(rs.getInt("stock_id"));
                mainStock.setItemId(rs.getInt("item_id"));
                mainStock.setSupplierId(rs.getInt("supplier_id"));
                mainStock.setBatchCode(rs.getString("batch_code"));
                mainStock.setPurchaseDate(rs.getDate("purchase_date"));
                mainStock.setPurchasePrice(rs.getDouble("purchase_price"));
                mainStock.setQuantity(rs.getInt("quantity"));
                mainStock.setExpiryDate(rs.getDate("expiry_date"));
                mainStocks.add(mainStock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mainStocks;
    }
    public void editMainStock(MainStock mainStock) {
        MainStock existingStock = getMainStockById(mainStock.getStockId());
        if (existingStock == null) return;

        String query = "UPDATE Main_Stock SET item_id = ?, supplier_id = ?, batch_code = ?, purchase_date = ?, purchase_price = ?, quantity = ?, expiry_date = ? WHERE stock_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, mainStock.getItemId());
            stmt.setInt(2, mainStock.getSupplierId());
            stmt.setString(3, mainStock.getBatchCode());
            stmt.setDate(4, new java.sql.Date(mainStock.getPurchaseDate().getTime()));
            stmt.setDouble(5, mainStock.getPurchasePrice());
            stmt.setInt(6, mainStock.getQuantity());
            stmt.setDate(7, new java.sql.Date(mainStock.getExpiryDate().getTime()));
            stmt.setInt(8, mainStock.getStockId());
            stmt.executeUpdate();

            int quantityDifference = mainStock.getQuantity() - existingStock.getQuantity();
            adjustTotalStock(mainStock.getItemId(), quantityDifference);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public MainStock getMainStockById(int stockId) {
        String query = "SELECT * FROM Main_Stock WHERE stock_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, stockId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                MainStock mainStock = new MainStock();
                mainStock.setStockId(rs.getInt("stock_id"));
                mainStock.setItemId(rs.getInt("item_id"));
                mainStock.setSupplierId(rs.getInt("supplier_id"));
                mainStock.setBatchCode(rs.getString("batch_code"));
                mainStock.setPurchaseDate(rs.getDate("purchase_date"));
                mainStock.setPurchasePrice(rs.getDouble("purchase_price"));
                mainStock.setQuantity(rs.getInt("quantity"));
                mainStock.setExpiryDate(rs.getDate("expiry_date"));
                return mainStock;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void deleteMainStock(int stockId) {
        MainStock mainStock = getMainStockById(stockId);
        if (mainStock == null) return;

        String query = "DELETE FROM Main_Stock WHERE stock_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, stockId);
            stmt.executeUpdate();
            adjustTotalStock(mainStock.getItemId(), -mainStock.getQuantity());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void updateTotalStock(int itemId, int quantity) {
        String query = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
            System.out.println("Total stock updated for item ID: " + itemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adjustTotalStock(int itemId, int quantityDifference) {
        String query = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantityDifference);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
            System.out.println("Total stock updated for item ID: " + itemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //codes for reshelf
    public List<MainStock> getMainStocksByItemId(int itemId) {
        List<MainStock> mainStocks = new ArrayList<>();
        String query = "SELECT * FROM Main_Stock WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MainStock mainStock = new MainStock();
                mainStock.setStockId(rs.getInt("stock_id"));
                mainStock.setItemId(rs.getInt("item_id"));
                mainStock.setSupplierId(rs.getInt("supplier_id"));
                mainStock.setBatchCode(rs.getString("batch_code"));
                mainStock.setPurchaseDate(rs.getDate("purchase_date"));
                mainStock.setPurchasePrice(rs.getDouble("purchase_price"));
                mainStock.setQuantity(rs.getInt("quantity"));
                mainStock.setExpiryDate(rs.getDate("expiry_date"));
                mainStocks.add(mainStock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mainStocks;
    }

    public void updateMainStock(MainStock mainStock) {
        String query = "UPDATE Main_Stock SET quantity = ? WHERE stock_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, mainStock.getQuantity());
            stmt.setInt(2, mainStock.getStockId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalStockForItem(int itemId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) as total_stock FROM main_stock WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

