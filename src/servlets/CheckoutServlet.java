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
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import concurrency.ThreadPoolManager;
import concurrency.CheckoutTask;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int TIMEOUT_SECONDS = 30;
    
    private DatabaseConnection dbConnection;
    private SaleDAO saleDAO;
    private BillDAO billDAO;
    private BillItemDAO billItemDAO;
    private WebStockDAO webStockDAO;
    private ThreadPoolManager threadPoolManager;

    @Override
    public void init() throws ServletException {
        dbConnection = new DatabaseConnection();
        saleDAO = new SaleDAO();
        billDAO = new BillDAO();
        billItemDAO = new BillItemDAO();
        webStockDAO = new WebStockDAO();
        threadPoolManager = ThreadPoolManager.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            request.setAttribute("error", "Your cart is empty");
            request.getRequestDispatcher("/jsp/webstore/cart.jsp").forward(request, response);
            return;
        }

        try {
            CheckoutTask checkoutTask = new CheckoutTask(
                request,
                response,
                cart,
                session,
                dbConnection,
                saleDAO,
                billDAO,
                billItemDAO,
                webStockDAO
            );

            Future<Boolean> future = threadPoolManager.submitTask(checkoutTask);
            boolean success = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (!success) {
                request.setAttribute("error", "Checkout processing failed");
                request.getRequestDispatcher("/jsp/webstore/cart.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error processing checkout: " + e.getMessage());
            request.getRequestDispatcher("/jsp/webstore/cart.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        threadPoolManager.shutdown();
    }
} 