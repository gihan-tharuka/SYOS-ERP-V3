package servlets;

import controller.SalesManagementController;
import dao.*;
import factory.PaymentFactory;
import model.*;
import view.SalesManagementView;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/sales/*")
public class SalesServlet extends HttpServlet {
    private SalesManagementController salesController;
    private SaleDAO saleDAO;
    private BillDAO billDAO;
    private BillItemDAO billItemDAO;
    private ShelfStockDAO shelfStockDAO;
    private ItemDAO itemDAO;

    @Override
    public void init() throws ServletException {
        try {
            // Get database connection using singleton
            Connection connection = DatabaseConnection.getInstance().getConnection();
            
            // Initialize DAOs
            saleDAO = new SaleDAO(connection);
            billDAO = new BillDAO(connection);
            billItemDAO = new BillItemDAO(connection);
            shelfStockDAO = new ShelfStockDAO(connection);
            itemDAO = new ItemDAO(connection, null); // ReorderSubject will be handled separately
            
            // Initialize view and controller
            SalesManagementView view = new SalesManagementView();
            salesController = new SalesManagementController(view, saleDAO, billDAO, billItemDAO, shelfStockDAO, itemDAO);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize SalesServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Show sales management menu
            request.getRequestDispatcher("/jsp/sales/salesMenu.jsp").forward(request, response);
        } else if (pathInfo.equals("/viewAll")) {
            // View all sales
            salesController.handleSalesManagement(1); // 1 for viewAllSales
            List<Sale> sales = saleDAO.getAllSales();
            request.setAttribute("sales", sales);
            request.getRequestDispatcher("/jsp/sales/viewAllSales.jsp").forward(request, response);
        } else if (pathInfo.equals("/viewBill")) {
            // View specific bill
            String billId = request.getParameter("billId");
            if (billId != null) {
                salesController.handleSalesManagement(3); // 3 for viewBill
                request.getRequestDispatcher("/jsp/sales/viewBill.jsp").forward(request, response);
            }
        } else if (pathInfo.equals("/create")) {
            // Show create sale form
            request.getRequestDispatcher("/jsp/sales/createSale.jsp").forward(request, response);
        } else if (pathInfo.equals("/getItemPrice")) {
            // Get item price
            String itemCode = request.getParameter("itemCode");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            try {
                Item item = itemDAO.getItemByCode(itemCode);
                if (item != null) {
                    response.getWriter().write(String.format("{\"price\": %.2f}", item.getPrice()));
                } else {
                    response.getWriter().write("{\"error\": \"Item not found\"}");
                }
            } catch (Exception e) {
                response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo.equals("/create")) {
            try {
                // Get form data
                String transactionType = request.getParameter("transactionType");
                if (transactionType == null || transactionType.trim().isEmpty()) {
                    throw new IllegalArgumentException("Transaction type is required");
                }
                
                // Create new sale
                Sale sale = new Sale();
                sale.setTransactionType(transactionType);
                sale.setSaleDate(new java.util.Date());
                saleDAO.addSale(sale);
                
                // Get items from form
                String[] itemCodes = request.getParameterValues("itemCodes");
                String[] quantities = request.getParameterValues("quantities");
                
                System.out.println("Debug - Item Codes: " + (itemCodes != null ? String.join(", ", itemCodes) : "null"));
                System.out.println("Debug - Quantities: " + (quantities != null ? String.join(", ", quantities) : "null"));
                
                if (itemCodes == null || quantities == null || itemCodes.length == 0 || quantities.length == 0) {
                    throw new IllegalArgumentException("At least one item is required");
                }
                
                double totalPrice = 0;
                List<BillItem> billItems = new ArrayList<>();
                
                // Process each item
                for (int i = 0; i < itemCodes.length; i++) {
                    String itemCode = itemCodes[i];
                    String quantityStr = quantities[i];
                    
                    System.out.println("Debug - Processing item: " + itemCode + " with quantity: " + quantityStr);
                    
                    if (itemCode == null || itemCode.trim().isEmpty() || quantityStr == null || quantityStr.trim().isEmpty()) {
                        System.out.println("Debug - Skipping empty entry at index " + i);
                        continue; // Skip empty entries
                    }
                    
                    int quantity;
                    try {
                        quantity = Integer.parseInt(quantityStr);
                        if (quantity <= 0) {
                            throw new IllegalArgumentException("Quantity must be greater than 0");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid quantity value: " + quantityStr);
                    }
                    
                    Item item = itemDAO.getItemByCode(itemCode);
                    if (item == null) {
                        throw new IllegalArgumentException("Item not found: " + itemCode);
                    }
                    
                    BillItem billItem = new BillItem();
                    billItem.setItemId(item.getItemId());
                    billItem.setItemName(item.getItemName());
                    billItem.setQuantity(quantity);
                    billItem.setItemTotalPrice(quantity * item.getPrice());
                    billItems.add(billItem);
                    totalPrice += billItem.getItemTotalPrice();
                    
                    System.out.println("Debug - Added item to bill: " + item.getItemName() + " with quantity: " + quantity);
                }
                
                if (billItems.isEmpty()) {
                    throw new IllegalArgumentException("No valid items were added to the sale");
                }

                // Get cash tendered
                String cashTenderedStr = request.getParameter("cashTendered");
                double cashTendered;
                try {
                    cashTendered = Double.parseDouble(cashTenderedStr);
                    if (cashTendered < totalPrice) {
                        throw new IllegalArgumentException("Cash tendered must be greater than or equal to the total price");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid cash tendered value");
                }
                
                // Create bill
                Bill bill = new Bill();
                bill.setSaleId(sale.getSaleId());
                bill.setTotalPrice(totalPrice);
                bill.setDiscount(0); // No discount for now
                bill.setCashTendered(cashTendered);
                bill.setChangeAmount(cashTendered - totalPrice);
                bill.setBillDate(new java.util.Date());
                bill.setPaymentMethod("Cash");
                
                billDAO.addBill(bill);
                
                // Add bill items
                for (BillItem billItem : billItems) {
                    billItem.setBillId(bill.getSerialNumber());
                    billItemDAO.addBillItem(billItem);
                    reduceShelfStock(billItem.getItemId(), billItem.getQuantity());
                }
                
                response.sendRedirect(request.getContextPath() + "/sales/viewAll");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Failed to create sale: " + e.getMessage());
                request.getRequestDispatcher("/jsp/sales/createSale.jsp").forward(request, response);
            }
        }
    }
    
    private void reduceShelfStock(int itemId, int quantity) {
        ShelfStock shelfStock = shelfStockDAO.getShelfStockByItemId(itemId);
        if (shelfStock != null) {
            shelfStock.setCurrentQuantity(shelfStock.getCurrentQuantity() - quantity);
            shelfStockDAO.updateShelfStock(shelfStock);
        }
    }
}
