package servlets;

import dao.*;
import model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.AsyncContext;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.sql.Connection;

@WebServlet(urlPatterns = "/checkout", asyncSupported = true)
public class CheckoutServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CheckoutServlet.class.getName());
    private SaleDAO saleDAO;
    private BillDAO billDAO;
    private BillItemDAO billItemDAO;
    private WebStockDAO webStockDAO;
    private DatabaseConnection dbConnection;
    
    // Thread-safe queue to hold checkout requests
    private final BlockingQueue<CheckoutTask> checkoutQueue = new LinkedBlockingQueue<>();
    private final ExecutorService workerThreadPool = Executors.newFixedThreadPool(5);

    @Override
    public void init() throws ServletException {
        try {
            dbConnection = DatabaseConnection.getInstance();
            if (!dbConnection.isConnectionValid()) {
                throw new ServletException("Database connection is not valid");
            }
            
            Connection conn = dbConnection.getConnection();
            try {
                saleDAO = new SaleDAO(conn);
                billDAO = new BillDAO(conn);
                billItemDAO = new BillItemDAO(conn);
                webStockDAO = new WebStockDAO(conn);
            } finally {
                dbConnection.releaseConnection(conn);
            }

            // Start worker threads to process the queue
            for (int i = 0; i < 5; i++) {
                workerThreadPool.submit(this::processCheckoutQueue);
            }
            logger.info("CheckoutServlet initialized successfully");
        } catch (Exception e) {
            logger.severe("Error initializing CheckoutServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize CheckoutServlet", e);
        }
    }

    // Worker thread: Processes queued checkout requests
    private void processCheckoutQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                CheckoutTask task = checkoutQueue.take(); // Blocks if queue is empty
                handleCheckout(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.severe("Error processing checkout queue: " + e.getMessage());
            }
        }
    }

    // Encapsulates a checkout request/response pair
    private static class CheckoutTask {
        final AsyncContext asyncContext;
        final List<CartItem> cart;
        final String paymentMethod;
        final Integer userId;

        CheckoutTask(AsyncContext asyncContext, List<CartItem> cart, String paymentMethod, Integer userId) {
            this.asyncContext = asyncContext;
            this.cart = cart;
            this.paymentMethod = paymentMethod;
            this.userId = userId;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            
            if (cart == null || cart.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/webstore");
                return;
            }

            // Get user ID from session
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                logger.warning("No user ID found in session");
                request.setAttribute("error", "User session expired. Please login again.");
                request.getRequestDispatcher("/jsp/webstore/cart.jsp").forward(request, response);
                return;
            }
            logger.info("Processing checkout for user ID: " + userId);

            // Start async context
            AsyncContext asyncContext = request.startAsync();
            asyncContext.setTimeout(30000); // 30 second timeout

            // Add the request to the queue
            checkoutQueue.add(new CheckoutTask(
                asyncContext,
                cart,
                request.getParameter("paymentMethod"),
                userId
            ));
            
            // Send immediate response
            response.setContentType("text/plain");
            response.getWriter().write("Checkout request accepted. Processing...");
            
        } catch (Exception e) {
            logger.severe("Error in doPost: " + e.getMessage());
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/webstore/cart.jsp").forward(request, response);
        }
    }

    // Actual checkout logic (called by worker threads)
    private void handleCheckout(CheckoutTask task) {
        try {
            // Create Sale record
            Sale sale = new Sale();
            sale.setSaleDate(new Date());
            sale.setTransactionType("WEB");
            sale.setUserId(task.userId);
            saleDAO.addSale(sale);

            // Create Bill record
            Bill bill = new Bill();
            bill.setSaleId(sale.getSaleId());
            bill.setBillDate(new Date());
            bill.setPaymentMethod(task.paymentMethod);
            
            double totalPrice = task.cart.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();
            bill.setTotalPrice(totalPrice);
            bill.setDiscount(0.0); // No discount for web store
            bill.setCashTendered(totalPrice);
            bill.setChangeAmount(0.0);
            
            billDAO.addBill(bill);

            // Create BillItems and update WebStock
            for (CartItem cartItem : task.cart) {
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

            HttpServletRequest request = (HttpServletRequest) task.asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) task.asyncContext.getResponse();
            HttpSession session = request.getSession();

            // Clear cart and set bill for display
            session.removeAttribute("cart");
            request.setAttribute("bill", bill);
            request.setAttribute("billItems", task.cart);
            
            // Forward to bill display page
            request.getRequestDispatcher("/jsp/webstore/bill.jsp").forward(request, response);

        } catch (Exception e) {
            logger.severe("Error handling checkout: " + e.getMessage());
            try {
                HttpServletRequest request = (HttpServletRequest) task.asyncContext.getRequest();
                HttpServletResponse response = (HttpServletResponse) task.asyncContext.getResponse();
                request.setAttribute("error", "Error processing checkout: " + e.getMessage());
                request.getRequestDispatcher("/jsp/webstore/cart.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.severe("Error forwarding to cart page: " + ex.getMessage());
            }
        } finally {
            task.asyncContext.complete();
        }
    }

    @Override
    public void destroy() {
        logger.info("Shutting down CheckoutServlet");
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