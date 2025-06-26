package servlets;

import dao.*;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/sales")
public class SalesServlet extends HttpServlet {
    private SaleDAO saleDAO;
    private ShelfStockDAO shelfStockDAO;
    private BillDAO billDAO;
    private BillItemDAO billItemDAO;
    private ItemDAO itemDAO;

    @Override
    public void init() {
        saleDAO = new SaleDAO(dao.DatabaseConnection.getInstance().getConnection());
        shelfStockDAO = new ShelfStockDAO(dao.DatabaseConnection.getInstance().getConnection());
        billDAO = new BillDAO(dao.DatabaseConnection.getInstance().getConnection());
        billItemDAO = new BillItemDAO(dao.DatabaseConnection.getInstance().getConnection());
        itemDAO = new ItemDAO(dao.DatabaseConnection.getInstance().getConnection(), null);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "new":
                showNewForm(req, resp);
                break;
            case "edit":
                // Optionally implement edit
                break;
            case "delete":
                deleteSale(req, resp);
                break;
            default:
                listSales(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "addItem":
                addItemToCart(req, resp);
                break;
            case "removeItem":
                removeItemFromCart(req, resp);
                break;
            case "checkout":
                showPaymentPage(req, resp);
                break;
            case "complete":
                completeSale(req, resp);
                break;
            default:
                doGet(req, resp);
        }
    }

    private void listSales(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Sale> sales = saleDAO.getAllSales();
        req.setAttribute("sales", sales);
        req.getRequestDispatcher("/jsp/sales/sales-list.jsp").forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<BillItem> cart = (List<BillItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/jsp/sales/sales-form.jsp").forward(req, resp);
    }

    private void addItemToCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemCode = req.getParameter("itemCode");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        Item item = itemDAO.getItemByCode(itemCode);
        if (item == null) {
            req.setAttribute("error", "Item not found.");
            showNewForm(req, resp);
            return;
        }
        BillItem billItem = new BillItem();
        billItem.setItemId(item.getItemId());
        billItem.setItemName(item.getItemName());
        billItem.setQuantity(quantity);
        billItem.setItemTotalPrice(item.getPrice() * quantity);

        HttpSession session = req.getSession();
        List<BillItem> cart = (List<BillItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        cart.add(billItem);
        session.setAttribute("cart", cart);

        resp.sendRedirect("sales?action=new");
    }

    private void removeItemFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int index = Integer.parseInt(req.getParameter("index"));
        HttpSession session = req.getSession();
        List<BillItem> cart = (List<BillItem>) session.getAttribute("cart");
        if (cart != null && index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
        session.setAttribute("cart", cart);
        resp.sendRedirect("sales?action=new");
    }

    private void showPaymentPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<BillItem> cart = (List<BillItem>) session.getAttribute("cart");
        double total = 0;
        if (cart != null) {
            for (BillItem item : cart) {
                total += item.getItemTotalPrice();
            }
        }
        req.setAttribute("total", total);
        req.getRequestDispatcher("/jsp/sales/sales-payment.jsp").forward(req, resp);
    }

    private void completeSale(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<BillItem> cart = (List<BillItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            req.setAttribute("error", "No items in cart.");
            showNewForm(req, resp);
            return;
        }
        double total = 0;
        for (BillItem item : cart) total += item.getItemTotalPrice();
        double cashTendered = Double.parseDouble(req.getParameter("cashTendered"));
        double balance = cashTendered - total;

        // 1. Create Sale
        Sale sale = new Sale();
        sale.setSaleDate(new java.util.Date());
        sale.setTransactionType("CASH");
        saleDAO.addSale(sale);

        // 2. Create Bill
        Bill bill = new Bill();
        bill.setSaleId(sale.getSaleId());
        bill.setTotalPrice(total);
        bill.setCashTendered(cashTendered);
        bill.setChangeAmount(balance);
        bill.setBillDate(new java.util.Date());
        bill.setPaymentMethod("CASH");
        billDAO.addBill(bill);

        // 3. Add Bill Items
        for (BillItem item : cart) {
            item.setBillId(bill.getSerialNumber());
            billItemDAO.addBillItem(item);

            // 4. Update Shelf Stock
            ShelfStock shelfStock = shelfStockDAO.getShelfStockByItemId(item.getItemId());
            if (shelfStock != null) {
                shelfStock.setCurrentQuantity(shelfStock.getCurrentQuantity() - item.getQuantity());
                shelfStockDAO.updateShelfStock(shelfStock);
            }
        }

        // 5. Clear cart
        session.removeAttribute("cart");

        // 6. Show bill
        req.setAttribute("bill", bill);
        req.setAttribute("billItems", cart);
        req.setAttribute("balance", balance);
        req.getRequestDispatcher("/jsp/sales/sales-bill.jsp").forward(req, resp);
    }

    private void deleteSale(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int saleId = Integer.parseInt(req.getParameter("id"));
        // Optionally, delete bill and bill items as well
        // saleDAO.deleteSale(saleId);
        resp.sendRedirect("sales");
    }
}
