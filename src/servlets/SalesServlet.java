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
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo.equals("/create")) {
            // Create new sale
            salesController.handleSalesManagement(2); // 2 for createNewSale
            response.sendRedirect(request.getContextPath() + "/sales/viewAll");
        }
    }
}
