package servlets;

import controller.MainStockManagementController;
import dao.DatabaseConnection;
import dao.ItemDAO;
import dao.MainStockDAO;
import dao.UserDAO;
import factory.StockFactory;
import model.Item;
import model.MainStock;
import observer.ReorderSubject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/mainstock/*")
public class MainStockServlet extends HttpServlet {
    private MainStockManagementController controller;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        try {
            // Get database connection from singleton
            Connection connection = DatabaseConnection.getInstance().getConnection();
            if (connection == null || connection.isClosed()) {
                throw new ServletException("Database connection is not available");
            }

            // Initialize ReorderSubject (you'll need to implement this)
            ReorderSubject reorderSubject = new ReorderSubject(); // Create appropriate instance

            // Initialize DAOs
            mainStockDAO = new MainStockDAO(connection);
            itemDAO = new ItemDAO(connection, reorderSubject);
            userDAO = new UserDAO(connection);
            controller = new MainStockManagementController(null, mainStockDAO, itemDAO, userDAO);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize MainStockServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null) {
            action = "/view";
        }
        
        switch (action) {
            case "/view":
                viewAllMainStocks(request, response);
                break;
            case "/add":
                showAddForm(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/mainstock/view");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        
        switch (action) {
            case "/add":
                addMainStock(request, response);
                break;
            case "/edit":
                editMainStock(request, response);
                break;
            case "/delete":
                deleteMainStock(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/mainstock/view");
        }
    }

    private void viewAllMainStocks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<MainStock> mainStocks = mainStockDAO.getAllMainStocks();
            List<Map<String, Object>> stockData = new ArrayList<>();
            
            for (MainStock stock : mainStocks) {
                Map<String, Object> stockMap = new HashMap<>();
                stockMap.put("stockId", stock.getStockId());
                stockMap.put("itemCode", itemDAO.getItemCodeById(stock.getItemId()));
                stockMap.put("supplierUsername", userDAO.getUsernameById(stock.getSupplierId()));
                stockMap.put("batchCode", stock.getBatchCode());
                stockMap.put("purchaseDate", stock.getPurchaseDate());
                stockMap.put("purchasePrice", stock.getPurchasePrice());
                stockMap.put("quantity", stock.getQuantity());
                stockMap.put("expiryDate", stock.getExpiryDate());
                stockData.add(stockMap);
            }
            
            request.setAttribute("mainStocks", stockData);
            request.getRequestDispatcher("/jsp/mainstock/list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading main stocks: " + e.getMessage());
            request.getRequestDispatcher("/jsp/mainstock/list.jsp").forward(request, response);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("items", itemDAO.getAllItems());
            request.getRequestDispatcher("/jsp/mainstock/add.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading items: " + e.getMessage());
            request.getRequestDispatcher("/jsp/mainstock/add.jsp").forward(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int stockId = Integer.parseInt(request.getParameter("id"));
            MainStock mainStock = mainStockDAO.getMainStockById(stockId);
            if (mainStock != null) {
                Map<String, Object> stockMap = new HashMap<>();
                stockMap.put("stockId", mainStock.getStockId());
                stockMap.put("itemCode", itemDAO.getItemCodeById(mainStock.getItemId()));
                stockMap.put("supplierUsername", userDAO.getUsernameById(mainStock.getSupplierId()));
                stockMap.put("batchCode", mainStock.getBatchCode());
                stockMap.put("purchaseDate", mainStock.getPurchaseDate());
                stockMap.put("purchasePrice", mainStock.getPurchasePrice());
                stockMap.put("quantity", mainStock.getQuantity());
                stockMap.put("expiryDate", mainStock.getExpiryDate());
                
                request.setAttribute("mainStock", stockMap);
                request.setAttribute("items", itemDAO.getAllItems());
                request.getRequestDispatcher("/jsp/mainstock/edit.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Main stock not found");
                response.sendRedirect(request.getContextPath() + "/mainstock/view");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error loading main stock: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/mainstock/view");
        }
    }

    private void addMainStock(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            MainStock mainStock = (MainStock) StockFactory.createStock(StockFactory.StockType.MAIN);
            
            String itemCode = request.getParameter("itemCode");
            Item item = itemDAO.getItemByCode(itemCode);
            if (item != null) {
                mainStock.setItemId(item.getItemId());
            }

            String supplierUsername = request.getParameter("supplierUsername");
            Integer supplierId = userDAO.getSupplierIdByUsername(supplierUsername);
            if (supplierId != null) {
                mainStock.setSupplierId(supplierId);
            }

            mainStock.setBatchCode(request.getParameter("batchCode"));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mainStock.setPurchaseDate(dateFormat.parse(request.getParameter("purchaseDate")));
            mainStock.setPurchasePrice(Double.parseDouble(request.getParameter("purchasePrice")));
            mainStock.setQuantity(Integer.parseInt(request.getParameter("quantity")));
            
            String expiryDateStr = request.getParameter("expiryDate");
            if (expiryDateStr != null && !expiryDateStr.isEmpty()) {
                mainStock.setExpiryDate(dateFormat.parse(expiryDateStr));
            }

            if (!mainStockDAO.doesMainStockExist(mainStock.getItemId(), mainStock.getBatchCode())) {
                mainStockDAO.addMainStock(mainStock);
                request.getSession().setAttribute("success", "Main stock added successfully!");
            } else {
                request.getSession().setAttribute("error", "An entry with the same item ID and batch code already exists.");
            }
        } catch (ParseException e) {
            request.getSession().setAttribute("error", "Invalid date format.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid number format.");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error adding main stock: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/mainstock/view");
    }

    private void editMainStock(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int stockId = Integer.parseInt(request.getParameter("stockId"));
            MainStock mainStock = mainStockDAO.getMainStockById(stockId);
            
            if (mainStock != null) {
                int previousQuantity = mainStock.getQuantity();
                
                String itemCode = request.getParameter("itemCode");
                Item item = itemDAO.getItemByCode(itemCode);
                if (item != null) {
                    mainStock.setItemId(item.getItemId());
                }

                String supplierUsername = request.getParameter("supplierUsername");
                Integer supplierId = userDAO.getSupplierIdByUsername(supplierUsername);
                if (supplierId != null) {
                    mainStock.setSupplierId(supplierId);
                }

                mainStock.setBatchCode(request.getParameter("batchCode"));
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mainStock.setPurchaseDate(dateFormat.parse(request.getParameter("purchaseDate")));
                mainStock.setPurchasePrice(Double.parseDouble(request.getParameter("purchasePrice")));
                mainStock.setQuantity(Integer.parseInt(request.getParameter("quantity")));
                
                String expiryDateStr = request.getParameter("expiryDate");
                if (expiryDateStr != null && !expiryDateStr.isEmpty()) {
                    mainStock.setExpiryDate(dateFormat.parse(expiryDateStr));
                }

                mainStockDAO.editMainStock(mainStock);
                mainStockDAO.adjustTotalStock(mainStock.getItemId(), mainStock.getQuantity() - previousQuantity);
                request.getSession().setAttribute("success", "Main stock updated successfully!");
            }
        } catch (ParseException e) {
            request.getSession().setAttribute("error", "Invalid date format.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid number format.");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error updating main stock: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/mainstock/view");
    }

    private void deleteMainStock(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int stockId = Integer.parseInt(request.getParameter("id"));
            mainStockDAO.deleteMainStock(stockId);
            request.getSession().setAttribute("success", "Main stock deleted successfully!");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error deleting main stock: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/mainstock/view");
    }
} 