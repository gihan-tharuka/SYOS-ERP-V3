package dao;

import model.BatchSelection;
import model.MainStock;
import model.WebStock;
import strategy.BatchSelectionStrategy;
import strategy.ClosestExpiryDateStrategy;
import strategy.OldestBatchStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebStockDAO {
    private Connection connection;

    public WebStockDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addWebStock(WebStock webStock) {
        if (doesWebStockExistByItemId(webStock.getItemId())) {
            System.out.println("A web stock record with this item ID already exists.");
            return false;
        }

        String query = "INSERT INTO Web_Stock (item_id, web_capacity, current_quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, webStock.getItemId());
            stmt.setInt(2, webStock.getWebCapacity());
            stmt.setInt(3, webStock.getCurrentQuantity());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    webStock.setStockId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean doesWebStockExistByItemId(int itemId) {
        String query = "SELECT COUNT(*) FROM Web_Stock WHERE item_id = ?";
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

    public List<WebStock> getAllWebStocks() {
        List<WebStock> webStocks = new ArrayList<>();
        String query = "SELECT * FROM Web_Stock";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                WebStock webStock = new WebStock();
                webStock.setStockId(rs.getInt("web_id"));
                webStock.setItemId(rs.getInt("item_id"));
                webStock.setWebCapacity(rs.getInt("web_capacity"));
                webStock.setCurrentQuantity(rs.getInt("current_quantity"));
                webStocks.add(webStock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return webStocks;
    }

    public Map<WebStock, String> getAllWebStocksWithItemCodes() {
        Map<WebStock, String> webStocksWithItemCodes = new HashMap<>();
        String query = "SELECT ws.web_id, ws.item_id, ws.web_capacity, ws.current_quantity, i.item_code " +
                "FROM Web_Stock ws JOIN Items i ON ws.item_id = i.item_id";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                WebStock webStock = new WebStock();
                webStock.setStockId(rs.getInt("web_id"));
                webStock.setItemId(rs.getInt("item_id"));
                webStock.setWebCapacity(rs.getInt("web_capacity"));
                webStock.setCurrentQuantity(rs.getInt("current_quantity"));
                String itemCode = rs.getString("item_code");
                webStocksWithItemCodes.put(webStock, itemCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return webStocksWithItemCodes;
    }

    public WebStock getWebStockByItemId(int itemId) {
        String query = "SELECT * FROM Web_Stock WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                WebStock webStock = new WebStock();
                webStock.setStockId(rs.getInt("web_id"));
                webStock.setItemId(rs.getInt("item_id"));
                webStock.setWebCapacity(rs.getInt("web_capacity"));
                webStock.setCurrentQuantity(rs.getInt("current_quantity"));
                return webStock;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteWebStockByItemId(int itemId) {
        String query = "DELETE FROM Web_Stock WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWebStock(WebStock webStock) {
        String query = "UPDATE Web_Stock SET web_capacity = ?, current_quantity = ? WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, webStock.getWebCapacity());
            stmt.setInt(2, webStock.getCurrentQuantity());
            stmt.setInt(3, webStock.getItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, List<BatchSelection>> getReshelvingInfo(List<WebStock> allWebStocks, MainStockDAO mainStockDAO) {
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        for (WebStock webStock : allWebStocks) {
            int itemId = webStock.getItemId();
            int reshelfQuantity = webStock.getWebCapacity() - webStock.getCurrentQuantity();

            if (reshelfQuantity <= 0) {
                continue;
            }

            List<MainStock> mainStocks = mainStockDAO.getMainStocksByItemId(itemId);
            BatchSelectionStrategy strategy;

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

    public Map<Integer, Integer> getReshelfQuantities(List<WebStock> allWebStocks) {
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        for (WebStock webStock : allWebStocks) {
            int itemId = webStock.getItemId();
            int reshelfQuantity = webStock.getWebCapacity() - webStock.getCurrentQuantity();
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
            WebStock webStock = getWebStockByItemId(itemId);
            int reshelfQuantity = reshelfQuantities.get(itemId);

            webStock.setCurrentQuantity(webStock.getCurrentQuantity() + reshelfQuantity);
            updateWebStock(webStock);

            for (BatchSelection selection : selectedBatches) {
                mainStockDAO.updateMainStock(selection.getBatch());
            }

            reorderLevelDAO.updateTotalStock(itemId, reshelfQuantity);
        }
    }
}
