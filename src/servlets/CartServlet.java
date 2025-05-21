package servlets;

import model.CartItem;
import dao.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private DatabaseConnection dbConnection;
    
    @Override
    public void init() throws ServletException {
        dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/webstore");
            return;
        }
        
        double totalAmount = cart.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        
        request.setAttribute("cart", cart);
        request.setAttribute("totalAmount", totalAmount);
        request.getRequestDispatcher("/jsp/webstore/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("remove".equals(action)) {
            int index = Integer.parseInt(request.getParameter("index"));
            HttpSession session = request.getSession();
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            
            if (cart != null && index >= 0 && index < cart.size()) {
                cart.remove(index);
            }
            
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    @Override
    public void destroy() {
        try {
            dbConnection.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 