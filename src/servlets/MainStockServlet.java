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

import jakarta.servlet.AsyncContext;
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
import java.util.concurrent.*;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/mainstock/*", asyncSupported = true)
public class MainStockServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MainStockServlet.class.getName());
    private static final int THREAD_POOL_SIZE = 5;
    private static final int QUEUE_CAPACITY = 100;
    
    private MainStockManagementController controller;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private UserDAO userDAO;
    private DatabaseConnection dbConnection;
    
    // Thread-safe queue to hold stock operations
    private final BlockingQueue<StockOperationTask> operationQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    private final ExecutorService workerThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    @Override
    public void init() throws ServletException {
        try {
            dbConnection = DatabaseConnection.getInstance();
            Connection connection = dbConnection.getConnection();
            if (connection == null || connection.isClosed()) {
                throw new ServletException("Database connection is not available");
            }

            ReorderSubject reorderSubject = new ReorderSubject();
            mainStockDAO = new MainStockDAO(connection);
            itemDAO = new ItemDAO(connection, reorderSubject);
            userDAO = new UserDAO(connection);
            controller = new MainStockManagementController(null, mainStockDAO, itemDAO, userDAO);

            // Start worker threads to process the queue
            for (int i = 0; i < THREAD_POOL_SIZE; i++) {
                workerThreadPool.submit(this::processOperationQueue);
            }
            logger.info("MainStockServlet initialized successfully");
        } catch (Exception e) {
            logger.severe("Error initializing MainStockServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize MainStockServlet", e);
        }
    }

    // Worker thread: Processes queued stock operations
    private void processOperationQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                StockOperationTask task = operationQueue.take(); // Blocks if queue is empty
                handleStockOperation(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.severe("Error processing operation queue: " + e.getMessage());
            }
        }
    }

    // Encapsulates a stock operation request/response pair
    private static class StockOperationTask {
        final AsyncContext asyncContext;
        final String operation;
        final Map<String, String> parameters;

        StockOperationTask(AsyncContext asyncContext, String operation, Map<String, String> parameters) {
            this.asyncContext = asyncContext;
            this.operation = operation;
            this.parameters = parameters;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Start async context
            AsyncContext asyncContext = request.startAsync();
            asyncContext.setTimeout(30000); // 30 second timeout

            // Extract parameters
            Map<String, String> parameters = new HashMap<>();
            request.getParameterMap().forEach((key, values) -> {
                if (values != null && values.length > 0) {
                    parameters.put(key, values[0]);
                }
            });

            // Add the operation to the queue
            String operation = request.getPathInfo();
            if (operation == null) {
                operation = "/";
            }
            operationQueue.add(new StockOperationTask(asyncContext, operation, parameters));
            
            // Send immediate response
            response.setContentType("text/plain");
            response.getWriter().write("Stock operation request accepted. Processing...");
            
        } catch (Exception e) {
            logger.severe("Error in doPost: " + e.getMessage());
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/mainstock/list.jsp").forward(request, response);
        }
    }

    // Actual stock operation logic (called by worker threads)
    private void handleStockOperation(StockOperationTask task) {
        try {
            HttpServletRequest request = (HttpServletRequest) task.asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) task.asyncContext.getResponse();
            
            switch (task.operation) {
                case "/add":
                    addMainStock(task.parameters);
                    break;
                case "/edit":
                    editMainStock(task.parameters);
                    break;
                case "/delete":
                    deleteMainStock(task.parameters);
                    break;
                default:
                    logger.warning("Unknown operation: " + task.operation);
            }
            
            response.sendRedirect(request.getContextPath() + "/mainstock/view");
        } catch (Exception e) {
            logger.severe("Error handling stock operation: " + e.getMessage());
            try {
                HttpServletRequest request = (HttpServletRequest) task.asyncContext.getRequest();
                HttpServletResponse response = (HttpServletResponse) task.asyncContext.getResponse();
                request.setAttribute("error", "Internal server error: " + e.getMessage());
                request.getRequestDispatcher("/jsp/mainstock/list.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.severe("Error forwarding to list page: " + ex.getMessage());
            }
        } finally {
            task.asyncContext.complete();
        }
    }

    private void viewAllMainStocks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
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

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            request.setAttribute("items", itemDAO.getAllItems());
            request.getRequestDispatcher("/jsp/mainstock/add.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading items: " + e.getMessage());
            request.getRequestDispatcher("/jsp/mainstock/add.jsp").forward(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
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

    private void addMainStock(Map<String, String> parameters) throws Exception {
        MainStock mainStock = (MainStock) StockFactory.createStock(StockFactory.StockType.MAIN);
        
        String itemCode = parameters.get("itemCode");
        Item item = itemDAO.getItemByCode(itemCode);
        if (item != null) {
            mainStock.setItemId(item.getItemId());
        }

        String supplierUsername = parameters.get("supplierUsername");
        Integer supplierId = userDAO.getSupplierIdByUsername(supplierUsername);
        if (supplierId != null) {
            mainStock.setSupplierId(supplierId);
        }

        mainStock.setBatchCode(parameters.get("batchCode"));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mainStock.setPurchaseDate(dateFormat.parse(parameters.get("purchaseDate")));
        mainStock.setPurchasePrice(Double.parseDouble(parameters.get("purchasePrice")));
        mainStock.setQuantity(Integer.parseInt(parameters.get("quantity")));
        
        String expiryDateStr = parameters.get("expiryDate");
        if (expiryDateStr != null && !expiryDateStr.isEmpty()) {
            mainStock.setExpiryDate(dateFormat.parse(expiryDateStr));
        }

        if (!mainStockDAO.doesMainStockExist(mainStock.getItemId(), mainStock.getBatchCode())) {
            mainStockDAO.addMainStock(mainStock);
            // Broadcast stock update
            ReorderLevel reorderLevel = reorderLevelDAO.getReorderLevelByItemId(mainStock.getItemId());
            if (reorderLevel != null) {
                ReorderLevelSSEServlet.broadcastUpdate("update", gson.toJson(reorderLevel));
            }
        } else {
            throw new Exception("An entry with the same item ID and batch code already exists.");
        }
    }

    private void editMainStock(Map<String, String> parameters) throws Exception {
        int stockId = Integer.parseInt(parameters.get("stockId"));
        MainStock mainStock = mainStockDAO.getMainStockById(stockId);
        
        if (mainStock != null) {
            int previousQuantity = mainStock.getQuantity();
            
            String itemCode = parameters.get("itemCode");
            Item item = itemDAO.getItemByCode(itemCode);
            if (item != null) {
                mainStock.setItemId(item.getItemId());
            }

            String supplierUsername = parameters.get("supplierUsername");
            Integer supplierId = userDAO.getSupplierIdByUsername(supplierUsername);
            if (supplierId != null) {
                mainStock.setSupplierId(supplierId);
            }

            mainStock.setBatchCode(parameters.get("batchCode"));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mainStock.setPurchaseDate(dateFormat.parse(parameters.get("purchaseDate")));
            mainStock.setPurchasePrice(Double.parseDouble(parameters.get("purchasePrice")));
            mainStock.setQuantity(Integer.parseInt(parameters.get("quantity")));
            
            String expiryDateStr = parameters.get("expiryDate");
            if (expiryDateStr != null && !expiryDateStr.isEmpty()) {
                mainStock.setExpiryDate(dateFormat.parse(expiryDateStr));
            }

            mainStockDAO.editMainStock(mainStock);
            mainStockDAO.adjustTotalStock(mainStock.getItemId(), mainStock.getQuantity() - previousQuantity);
            
            // Broadcast stock update
            ReorderLevel reorderLevel = reorderLevelDAO.getReorderLevelByItemId(mainStock.getItemId());
            if (reorderLevel != null) {
                ReorderLevelSSEServlet.broadcastUpdate("update", gson.toJson(reorderLevel));
            }
        }
    }

    private void deleteMainStock(Map<String, String> parameters) throws Exception {
        int stockId = Integer.parseInt(parameters.get("id"));
        MainStock mainStock = mainStockDAO.getMainStockById(stockId);
        if (mainStock != null) {
            int itemId = mainStock.getItemId();
            mainStockDAO.deleteMainStock(stockId);
            
            // Broadcast stock update
            ReorderLevel reorderLevel = reorderLevelDAO.getReorderLevelByItemId(itemId);
            if (reorderLevel != null) {
                ReorderLevelSSEServlet.broadcastUpdate("update", gson.toJson(reorderLevel));
            }
        }
    }

    @Override
    public void destroy() {
        logger.info("Shutting down MainStockServlet");
        workerThreadPool.shutdown();
        try {
            if (!workerThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                workerThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            workerThreadPool.shutdownNow();
        }
        dbConnection.closeAllConnections();
    }
} 