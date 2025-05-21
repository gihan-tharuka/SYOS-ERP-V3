package concurrency;

import dao.*;
import model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class CheckoutTask implements Callable<Boolean> {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final List<CartItem> cart;
    private final HttpSession session;
    private final DatabaseConnection dbConnection;
    private final SaleDAO saleDAO;
    private final BillDAO billDAO;
    private final BillItemDAO billItemDAO;
    private final WebStockDAO webStockDAO;

    public CheckoutTask(
        HttpServletRequest request,
        HttpServletResponse response,
        List<CartItem> cart,
        HttpSession session,
        DatabaseConnection dbConnection,
        SaleDAO saleDAO,
        BillDAO billDAO,
        BillItemDAO billItemDAO,
        WebStockDAO webStockDAO
    ) {
        this.request = request;
        this.response = response;
        this.cart = cart;
        this.session = session;
        this.dbConnection = dbConnection;
        this.saleDAO = saleDAO;
        this.billDAO = billDAO;
        this.billItemDAO = billItemDAO;
        this.webStockDAO = webStockDAO;
    }

    @Override
    public Boolean call() throws Exception {
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
            bill.setDiscount(0.0);
            bill.setCashTendered(totalPrice);
            bill.setChangeAmount(0.0);
            
            billDAO.addBill(bill);

            // Create BillItems and update WebStock
            for (CartItem cartItem : cart) {
                BillItem billItem = new BillItem();
                billItem.setBillId(bill.getSerialNumber());
                billItem.setItemId(cartItem.getItemId());
                billItem.setItemName(cartItem.getItemName());
                billItem.setQuantity(cartItem.getQuantity());
                billItem.setItemTotalPrice(cartItem.getTotalPrice());
                billItemDAO.addBillItem(billItem);

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
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error processing checkout: " + e.getMessage());
            request.getRequestDispatcher("/jsp/webstore/cart.jsp").forward(request, response);
            return false;
        } finally {
            if (connection != null) {
                dbConnection.releaseConnection(connection);
            }
        }
    }
} 