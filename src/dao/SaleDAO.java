package dao;

import model.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    private Connection connection;


public SaleDAO(Connection connection) {
    this.connection = connection;
}

    public void addSale(Sale sale) {
        String query = "INSERT INTO Sales (sale_date, transaction_type) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(sale.getSaleDate().getTime()));
            stmt.setString(2, sale.getTransactionType());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    sale.setSaleId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String query = "SELECT * FROM Sales";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setSaleId(rs.getInt("sale_id"));
                sale.setSaleDate(rs.getDate("sale_date"));
                sale.setTransactionType(rs.getString("transaction_type"));
                sales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }
}

