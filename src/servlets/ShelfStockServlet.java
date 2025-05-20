package servlets;

import controller.ShelfStockManagementController;
import dao.DatabaseConnection;
import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ReorderLevelDAO;
import dao.ShelfStockDAO;
import model.BatchSelection;
import model.ShelfStock;
import observer.ReorderSubject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@WebServlet("/shelfstock/*")
public class ShelfStockServlet extends HttpServlet {
    private ShelfStockManagementController controller;
    private Connection connection;
    private ReorderSubject reorderSubject;

    @Override
    public void init() throws ServletException {
        try {
            // Get connection using DatabaseConnection singleton
            connection = DatabaseConnection.getInstance().getConnection();
            if (connection == null) {
                throw new ServletException("Database connection failed");
            }
            
            // Initialize DAOs
            ShelfStockDAO shelfStockDAO = new ShelfStockDAO(connection);
            MainStockDAO mainStockDAO = new MainStockDAO(connection);
            ItemDAO itemDAO = new ItemDAO(connection, reorderSubject);
            ReorderLevelDAO reorderLevelDAO = new ReorderLevelDAO(connection);
            
            // Initialize controller with null view since we're using JSP
            controller = new ShelfStockManagementController(null, shelfStockDAO, mainStockDAO, itemDAO, reorderLevelDAO);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize ShelfStockServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null) {
            action = "/view";
        }
        
        switch (action) {
            case "/view":
                viewAllShelfStocks(request, response);
                break;
            case "/add":
                showAddForm(request, response);
                break;
            case "/reshelf":
                showReshelfForm(request, response);
                break;
            case "/delete":
                showDeleteForm(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/shelfstock/view");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null) {
            action = "/view";
        }
        
        switch (action) {
            case "/add":
                addShelfStock(request, response);
                break;
            case "/reshelf":
                reshelfStock(request, response);
                break;
            case "/delete":
                deleteShelfStock(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/shelfstock/view");
        }
    }

    private void viewAllShelfStocks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Map<ShelfStock, String> shelfStocks = controller.getShelfStockDAO().getAllShelfStocksWithItemCodes();
        request.setAttribute("shelfStocks", shelfStocks);
        request.getRequestDispatcher("/jsp/shelfstock/view.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/shelfstock/add.jsp").forward(request, response);
    }

    private void showReshelfForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get reshelving info before showing the form
        Map<Integer, List<BatchSelection>> reshelvingInfo = controller.getShelfStockDAO().getReshelvingInfo(
            controller.getShelfStockDAO().getAllShelfStocks(),
            controller.getMainStockDAO()
        );
        Map<Integer, Integer> reshelfQuantities = controller.getShelfStockDAO().getReshelfQuantities(
            controller.getShelfStockDAO().getAllShelfStocks()
        );
        Map<Integer, String> itemNames = controller.getItemDAO().getItemNamesByItemIds(reshelfQuantities.keySet());

        request.setAttribute("reshelvingInfo", reshelvingInfo);
        request.setAttribute("reshelfQuantities", reshelfQuantities);
        request.setAttribute("itemNames", itemNames);
        request.getRequestDispatcher("/jsp/shelfstock/reshelf.jsp").forward(request, response);
    }

    private void showDeleteForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/shelfstock/delete.jsp").forward(request, response);
    }

    private void addShelfStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String itemCode = request.getParameter("itemCode");
        int shelfCapacity = Integer.parseInt(request.getParameter("shelfCapacity"));
        
        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(controller.getItemDAO().getItemIdByCode(itemCode));
        shelfStock.setShelfCapacity(shelfCapacity);
        shelfStock.setCurrentQuantity(0);
        
        boolean success = controller.getShelfStockDAO().addShelfStock(shelfStock);
        
        if (success) {
            request.setAttribute("message", "Shelf stock added successfully!");
        } else {
            request.setAttribute("error", "Failed to add shelf stock. Item may already exist.");
        }
        
        response.sendRedirect(request.getContextPath() + "/shelfstock/view");
    }

    private void reshelfStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Map<Integer, List<BatchSelection>> reshelvingInfo = controller.getShelfStockDAO().getReshelvingInfo(
            controller.getShelfStockDAO().getAllShelfStocks(),
            controller.getMainStockDAO()
        );
        Map<Integer, Integer> reshelfQuantities = controller.getShelfStockDAO().getReshelfQuantities(
            controller.getShelfStockDAO().getAllShelfStocks()
        );

        controller.getShelfStockDAO().confirmReshelving(
            reshelvingInfo,
            reshelfQuantities,
            controller.getMainStockDAO(),
            controller.getReorderLevelDAO()
        );

        request.setAttribute("message", "Shelf stock reshelved successfully!");
        response.sendRedirect(request.getContextPath() + "/shelfstock/view");
    }

    private void deleteShelfStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        controller.getShelfStockDAO().deleteShelfStockByItemId(itemId);
        request.setAttribute("message", "Shelf stock deleted successfully!");
        response.sendRedirect(request.getContextPath() + "/shelfstock/view");
    }
}
