package dao;

import model.Bill;

import java.sql.*;


public class BillDAO {
    private Connection connection;

public BillDAO(Connection connection) {
    this.connection = connection;
}

    public void addBill(Bill bill) {
        String query = "INSERT INTO Bills (sale_id, total_price, discount, cash_tendered, change_amount, bill_date, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, bill.getSaleId());
            stmt.setDouble(2, bill.getTotalPrice());
            stmt.setDouble(3, bill.getDiscount());
            stmt.setDouble(4, bill.getCashTendered());
            stmt.setDouble(5, bill.getChangeAmount());
            stmt.setDate(6, new java.sql.Date(bill.getBillDate().getTime()));
            stmt.setString(7, bill.getPaymentMethod());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bill.setSerialNumber(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Bill getBillBySerialNumber(int serialNumber) {
        String query = "SELECT * FROM Bills WHERE serial_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, serialNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Bill bill = new Bill();
                bill.setSerialNumber(rs.getInt("serial_number"));
                bill.setSaleId(rs.getInt("sale_id"));
                bill.setTotalPrice(rs.getDouble("total_price"));
                bill.setDiscount(rs.getDouble("discount"));
                bill.setCashTendered(rs.getDouble("cash_tendered"));
                bill.setChangeAmount(rs.getDouble("change_amount"));
                bill.setBillDate(rs.getDate("bill_date"));
                bill.setPaymentMethod(rs.getString("payment_method"));
                return bill;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bill getBillBySaleId(int saleId) {
        String query = "SELECT * FROM Bills WHERE sale_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, saleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Bill bill = new Bill();
                bill.setSerialNumber(rs.getInt("serial_number"));
                bill.setSaleId(rs.getInt("sale_id"));
                bill.setTotalPrice(rs.getDouble("total_price"));
                bill.setDiscount(rs.getDouble("discount"));
                bill.setCashTendered(rs.getDouble("cash_tendered"));
                bill.setChangeAmount(rs.getDouble("change_amount"));
                bill.setBillDate(rs.getDate("bill_date"));
                bill.setPaymentMethod(rs.getString("payment_method"));
                return bill;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

