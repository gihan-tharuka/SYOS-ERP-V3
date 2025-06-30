package servlets;

import dao.BillDAO;
import dao.BillItemDAO;
import dao.DatabaseConnection;
import model.Bill;
import model.BillItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/customer-bills")
public class CustomerBillsServlet extends HttpServlet {
    private BillDAO billDAO;
    private BillItemDAO billItemDAO;
    private DatabaseConnection dbConnection;

    @Override
    public void init() throws ServletException {
        dbConnection = DatabaseConnection.getInstance();
        try (Connection conn = dbConnection.getConnection()) {
            billDAO = new BillDAO(conn);
            billItemDAO = new BillItemDAO(conn);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize CustomerBillsServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        Integer userId = (Integer) session.getAttribute("userId");
        String serialNumber = request.getParameter("serialNumber");
        try (Connection conn = dbConnection.getConnection()) {
            if (serialNumber != null) {
                // Show single bill details
                Bill bill = billDAO.getBillBySerialNumber(Integer.parseInt(serialNumber));
                if (bill == null) {
                    response.sendRedirect(request.getContextPath() + "/customer-bills");
                    return;
                }
                Integer billUserId = getUserIdBySaleId(conn, bill.getSaleId());
                if (billUserId == null || !billUserId.equals(userId)) {
                    response.sendRedirect(request.getContextPath() + "/customer-bills");
                    return;
                }
                List<BillItem> billItems = billItemDAO.getBillItemsByBillId(bill.getSerialNumber());
                request.setAttribute("bill", bill);
                request.setAttribute("billItems", billItems);
                request.getRequestDispatcher("/jsp/webstore/bill.jsp").forward(request, response);
                return;
            } else {
                // Show all bills
                List<Bill> bills = getBillsByUserId(conn, userId);
                request.setAttribute("bills", bills);
                request.getRequestDispatcher("/jsp/customer/my-bills.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Unable to retrieve bills: " + e.getMessage());
            if (serialNumber != null) {
                request.getRequestDispatcher("/jsp/customer/my-bills.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/jsp/customer/my-bills.jsp").forward(request, response);
            }
        }
    }

    // Helper method to fetch bills for a userId by joining Bills and Sales
    private List<Bill> getBillsByUserId(Connection conn, Integer userId) throws Exception {
        List<Bill> bills = new java.util.ArrayList<>();
        String query = "SELECT b.* FROM Bills b JOIN Sales s ON b.sale_id = s.sale_id WHERE s.user_id = ? ORDER BY b.bill_date DESC";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
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
            }
        }
        return bills;
    }

    // Helper method to get userId from Sales table by saleId
    private Integer getUserIdBySaleId(Connection conn, int saleId) throws Exception {
        String query = "SELECT user_id FROM Sales WHERE sale_id = ?";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, saleId);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int uid = rs.getInt("user_id");
                    if (!rs.wasNull()) return uid;
                }
            }
        }
        return null;
    }
} 