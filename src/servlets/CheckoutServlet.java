package servlets;

import dao.*;
import model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private DatabaseConnection dbConnection;
    private SaleDAO saleDAO;
    private BillDAO billDAO;
    private BillItemDAO billItemDAO;
    private WebStockDAO webStockDAO;

    @Override
    public void init() throws ServletException {
        dbConnection = DatabaseConnection.getInstance();
        Connection connection = null;
        try {
            connection = dbConnection.getConnection();
            saleDAO = new SaleDAO(connection);
            billDAO = new BillDAO(connection);
            billItemDAO = new BillItemDAO(connection);
            webStockDAO = new WebStockDAO(connection);
        } catch (Exception e) {
            throw new ServletException("Error initializing CheckoutServlet", e);
        } finally {
            if (connection != null) {
                dbConnection.releaseConnection(connection);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/webstore");
            return;
        }

        Connection connection = null;
        try {
            connection = dbConnection.getConnection();
            
            // Create Sale record
            Sale sale = new Sale();
            sale.setSaleDate(new Date());
            sale.setTransactionType("WEB");
            sale.setUserId((Integer) session.getAttribute("userId"));
            saleDAO.addSale(sale);

            // Create Bill record
            Bill bill = new Bill();
            bill.setSaleId(sale.getSaleId());
            bill.setBillDate(new Date());
            bill.setPaymentMethod(request.getParameter("paymentMethod"));
            
            double totalPrice = cart.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();
            bill.setTotalPrice(totalPrice);
            bill.setDiscount(0.0); // No discount for web store
            bill.setCashTendered(totalPrice);
            bill.setChangeAmount(0.0);
            
            billDAO.addBill(bill);

            // Create BillItems and update WebStock
            for (CartItem cartItem : cart) {
                // Create BillItem
                BillItem billItem = new BillItem();
                billItem.setBillId(bill.getSerialNumber());
                billItem.setItemId(cartItem.getItemId());
                billItem.setItemName(cartItem.getItemName());
                billItem.setQuantity(cartItem.getQuantity());
                billItem.setItemTotalPrice(cartItem.getTotalPrice());
                billItemDAO.addBillItem(billItem);

                // Update WebStock
                WebStock webStock = webStockDAO.getWebStockByItemId(cartItem.getItemId());
                webStock.setCurrentQuantity(webStock.getCurrentQuantity() - cartItem.getQuantity());
                webStockDAO.updateWebStock(webStock);
            }

            // Clear cart and set bill for display
            session.removeAttribute("cart");
            request.setAttribute("bill", bill);
            request.setAttribute("billItems", cart);
            
            // Forward to bill display page
            request.getRequestDispatcher("/jsp/webstore/bill.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error processing checkout: " + e.getMessage());
            request.getRequestDispatcher("/jsp/webstore/cart.jsp").forward(request, response);
        } finally {
            if (connection != null) {
                dbConnection.releaseConnection(connection);
            }
        }
    }

    @Override
    public void destroy() {
        try {
            dbConnection.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 