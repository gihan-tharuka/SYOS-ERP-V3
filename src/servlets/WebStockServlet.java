package servlets;

import controller.WebStockManagementController;
import dao.DatabaseConnection;
import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ReorderLevelDAO;
import dao.WebStockDAO;
import model.BatchSelection;
import model.Item;
import model.WebStock;
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

@WebServlet("/webstock/*")
public class WebStockServlet extends HttpServlet {
    private WebStockManagementController controller;
    private Connection connection;
    private ReorderSubject reorderSubject;

    @Override
    public void init() throws ServletException {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            if (connection == null) {
                throw new ServletException("Database connection failed");
            }
            
            WebStockDAO webStockDAO = new WebStockDAO(connection);
            MainStockDAO mainStockDAO = new MainStockDAO(connection);
            ItemDAO itemDAO = new ItemDAO(connection, reorderSubject);
            ReorderLevelDAO reorderLevelDAO = new ReorderLevelDAO(connection);
            
            controller = new WebStockManagementController(null, webStockDAO, mainStockDAO, itemDAO, reorderLevelDAO);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize WebStockServlet", e);
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
                viewAllWebStocks(request, response);
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
                response.sendRedirect(request.getContextPath() + "/webstock/view");
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
                addWebStock(request, response);
                break;
            case "/reshelf":
                reshelfStock(request, response);
                break;
            case "/delete":
                deleteWebStock(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/webstock/view");
        }
    }

    private void viewAllWebStocks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Map<WebStock, String> webStocks = controller.getWebStockDAO().getAllWebStocksWithItemCodes();
        request.setAttribute("webStocks", webStocks);
        request.getRequestDispatcher("/jsp/webstock/view.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Item> items = controller.getItemDAO().getAllItems();
        request.setAttribute("items", items);
        request.getRequestDispatcher("/jsp/webstock/add.jsp").forward(request, response);
    }

    private void showReshelfForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Map<Integer, List<BatchSelection>> reshelvingInfo = controller.getWebStockDAO().getReshelvingInfo(
            controller.getWebStockDAO().getAllWebStocks(),
            controller.getMainStockDAO()
        );
        Map<Integer, Integer> reshelfQuantities = controller.getWebStockDAO().getReshelfQuantities(
            controller.getWebStockDAO().getAllWebStocks()
        );
        Map<Integer, String> itemNames = controller.getItemDAO().getItemNamesByItemIds(reshelfQuantities.keySet());

        request.setAttribute("reshelvingInfo", reshelvingInfo);
        request.setAttribute("reshelfQuantities", reshelfQuantities);
        request.setAttribute("itemNames", itemNames);
        request.getRequestDispatcher("/jsp/webstock/reshelf.jsp").forward(request, response);
    }

    private void showDeleteForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/webstock/delete.jsp").forward(request, response);
    }

    private void addWebStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String itemCode = request.getParameter("itemCode");
        int webCapacity = Integer.parseInt(request.getParameter("webCapacity"));
        
        WebStock webStock = new WebStock();
        webStock.setItemId(controller.getItemDAO().getItemIdByCode(itemCode));
        webStock.setWebCapacity(webCapacity);
        webStock.setCurrentQuantity(0);
        
        boolean success = controller.getWebStockDAO().addWebStock(webStock);
        
        if (success) {
            request.setAttribute("message", "Web stock added successfully!");
        } else {
            request.setAttribute("error", "Failed to add web stock. Item may already exist.");
        }
        
        response.sendRedirect(request.getContextPath() + "/webstock/view");
    }

    private void reshelfStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Map<Integer, List<BatchSelection>> reshelvingInfo = controller.getWebStockDAO().getReshelvingInfo(
            controller.getWebStockDAO().getAllWebStocks(),
            controller.getMainStockDAO()
        );
        Map<Integer, Integer> reshelfQuantities = controller.getWebStockDAO().getReshelfQuantities(
            controller.getWebStockDAO().getAllWebStocks()
        );

        controller.getWebStockDAO().confirmReshelving(
            reshelvingInfo,
            reshelfQuantities,
            controller.getMainStockDAO(),
            controller.getReorderLevelDAO()
        );

        request.setAttribute("message", "Web stock reshelved successfully!");
        response.sendRedirect(request.getContextPath() + "/webstock/view");
    }

    private void deleteWebStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        controller.getWebStockDAO().deleteWebStockByItemId(itemId);
        request.setAttribute("message", "Web stock deleted successfully!");
        response.sendRedirect(request.getContextPath() + "/webstock/view");
    }
}
