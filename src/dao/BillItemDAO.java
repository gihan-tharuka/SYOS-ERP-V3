package dao;

import model.BillItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillItemDAO {
    private Connection connection;

    public BillItemDAO(Connection connection) {
        this.connection = connection;
    }

    public void addBillItem(BillItem billItem) {
        String query = "INSERT INTO Bill_Items (bill_id, item_id, item_name, quantity, item_total_price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, billItem.getBillId());
            stmt.setInt(2, billItem.getItemId());
            stmt.setString(3, billItem.getItemName());
            stmt.setInt(4, billItem.getQuantity());
            stmt.setDouble(5, billItem.getItemTotalPrice());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    billItem.setBillItemId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BillItem> getBillItemsBySerialNumber(int serialNumber) {
        List<BillItem> billItems = new ArrayList<>();
        String query = "SELECT * FROM Bill_Items WHERE bill_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, serialNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BillItem billItem = new BillItem();
                billItem.setBillItemId(rs.getInt("bill_item_id"));
                billItem.setBillId(rs.getInt("bill_id"));
                billItem.setItemId(rs.getInt("item_id"));
                billItem.setItemName(rs.getString("item_name"));
                billItem.setQuantity(rs.getInt("quantity"));
                billItem.setItemTotalPrice(rs.getDouble("item_total_price"));
                billItems.add(billItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billItems;
    }

    public List<BillItem> getBillItemsByBillId(int billId) {
        List<BillItem> billItems = new ArrayList<>();
        String query = "SELECT bi.*, i.price as unit_price FROM Bill_Items bi JOIN Items i ON bi.item_id = i.item_id WHERE bi.bill_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BillItem billItem = new BillItem();
                billItem.setBillItemId(rs.getInt("bill_item_id"));
                billItem.setBillId(rs.getInt("bill_id"));
                billItem.setItemId(rs.getInt("item_id"));
                billItem.setItemName(rs.getString("item_name"));
                billItem.setQuantity(rs.getInt("quantity"));
                billItem.setItemTotalPrice(rs.getDouble("item_total_price"));
                billItem.setPrice(rs.getDouble("unit_price"));
                billItems.add(billItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billItems;
    }

    public void updateBillItem(BillItem billItem) {
        String query = "UPDATE Bill_Items SET item_id = ?, item_name = ?, quantity = ?, item_total_price = ? WHERE bill_item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, billItem.getItemId());
            stmt.setString(2, billItem.getItemName());
            stmt.setInt(3, billItem.getQuantity());
            stmt.setDouble(4, billItem.getItemTotalPrice());
            stmt.setInt(5, billItem.getBillItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBillItem(int billItemId) {
        String query = "DELETE FROM Bill_Items WHERE bill_item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, billItemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
