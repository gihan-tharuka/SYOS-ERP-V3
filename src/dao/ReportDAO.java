package dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private static ReportDAO instance;
    private Connection connection;

    public ReportDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static ReportDAO getInstance() {
        if (instance == null) {
            instance = new ReportDAO();
        }
        return instance;
    }


public List<BillItem> getDailySalesDetails(Date date) {
    List<BillItem> salesDetails = new ArrayList<>();
    String query = "SELECT bi.bill_item_id, bi.bill_id, bi.item_id, i.item_code, i.item_name, bi.quantity, (bi.quantity * i.price) AS item_total_price " +
            "FROM Bills b " +
            "JOIN bill_items bi ON b.serial_number = bi.bill_id " +
            "JOIN Items i ON bi.item_id = i.item_id " +
            "WHERE b.bill_date = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setDate(1, date);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            BillItem billItem = new BillItem(
                    rs.getInt("bill_item_id"),
                    rs.getInt("bill_id"),
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("item_total_price")
            );
            salesDetails.add(billItem);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return salesDetails;
}


public List<Object[]> getReorderItems() {
    List<Object[]> items = new ArrayList<>();
    String query = "SELECT i.item_code, i.item_name, rl.total_stock, rl.threshold_quantity " +
            "FROM Items i " +
            "JOIN reorder_levels rl ON i.item_id = rl.item_id " +
            "WHERE rl.total_stock < rl.threshold_quantity";
    try (PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            String itemCode = rs.getString("item_code");
            String itemName = rs.getString("item_name");
            int currentQuantity = rs.getInt("total_stock");
            int thresholdQuantity = rs.getInt("threshold_quantity");
            int quantityToReorder = thresholdQuantity - currentQuantity;

            items.add(new Object[]{itemCode, itemName, currentQuantity, quantityToReorder});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return items;
}


public List<Object[]> getStockReport() {
    List<Object[]> stocks = new ArrayList<>();
    String query = "SELECT i.item_code, i.item_name, ms.batch_code, ms.quantity, ms.expiry_date " +
            "FROM Items i " +
            "JOIN Main_Stock ms ON i.item_id = ms.item_id";
    try (PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            String itemCode = rs.getString("item_code");
            String itemName = rs.getString("item_name");
            String batchCode = rs.getString("batch_code");
            int quantity = rs.getInt("quantity");
            Date expiryDate = rs.getDate("expiry_date");

            stocks.add(new Object[]{itemCode, itemName, batchCode, quantity, expiryDate});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return stocks;
}

    public List<Bill> getBillReport() {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM Bills";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setSerialNumber(rs.getInt("serial_number"));
                bill.setSaleId(rs.getInt("sale_id"));
                bill.setTotalPrice(rs.getDouble("total_price"));
                bill.setDiscount(rs.getDouble("discount"));
                bill.setCashTendered(rs.getDouble("cash_tendered"));
                bill.setChangeAmount(rs.getDouble("change_amount"));
                bill.setBillDate(rs.getDate("bill_date"));
                bill.setPaymentMethod(rs.getString("payment_method"));
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }
}
