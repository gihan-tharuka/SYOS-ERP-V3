package servlets;

import dao.DatabaseConnection;
import dao.ItemDAO;
import dao.WebStockDAO;
import model.CartItem;
import model.Item;
import model.WebStock;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/webstore")
public class WebStoreServlet extends HttpServlet {
    private WebStockDAO webStockDAO;
    private ItemDAO itemDAO;

    @Override
    public void init() throws ServletException {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        webStockDAO = new WebStockDAO(dbConnection.getConnection());
        itemDAO = new ItemDAO(dbConnection.getConnection(), null);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Map<WebStock, String> webStocks = webStockDAO.getAllWebStocksWithItemCodes();
        Map<WebStock, Item> webStockItems = new HashMap<>();
        
        // Get item details for each web stock
        for (Map.Entry<WebStock, String> entry : webStocks.entrySet()) {
            Item item = itemDAO.getItemByCode(entry.getValue());
            if (item != null) {
                webStockItems.put(entry.getKey(), item);
            }
        }
        
        request.setAttribute("webStocks", webStocks);
        request.setAttribute("webStockItems", webStockItems);
        
        // Pass cart item IDs to JSP
        HttpSession session = request.getSession(false);
        List<CartItem> cart = null;
        if (session != null) {
            cart = (List<CartItem>) session.getAttribute("cart");
        }
        java.util.Set<Integer> cartItemIds = new java.util.HashSet<>();
        if (cart != null) {
            for (CartItem ci : cart) {
                cartItemIds.add(ci.getItemId());
            }
        }
        request.setAttribute("cartItemIds", cartItemIds);
        
        // Also pass a comma-separated string for JSP fn:contains
        StringBuilder cartItemIdsStr = new StringBuilder();
        for (Integer id : cartItemIds) {
            cartItemIdsStr.append(",").append(id).append(",");
        }
        request.setAttribute("cartItemIdsStr", cartItemIdsStr.toString());
        
        request.getRequestDispatcher("/jsp/webstore/webstore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("addToCart".equals(action)) {
            try {
                int itemId = Integer.parseInt(request.getParameter("itemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                
                WebStock webStock = webStockDAO.getWebStockByItemId(itemId);
                if (webStock == null) {
                    request.setAttribute("error", "Item not found");
                    doGet(request, response);
                    return;
                }

                // Get item code directly from the database
                String itemCode = itemDAO.getItemCodeById(itemId);
                if (itemCode == null) {
                    request.setAttribute("error", "Item code not found");
                    doGet(request, response);
                    return;
                }

                Item item = itemDAO.getItemByCode(itemCode);
                if (item == null) {
                    request.setAttribute("error", "Item details not found");
                    doGet(request, response);
                    return;
                }

                if (quantity <= 0 || quantity > webStock.getCurrentQuantity()) {
                    request.setAttribute("error", "Invalid quantity");
                    doGet(request, response);
                    return;
                }
                
                HttpSession session = request.getSession();
                List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
                if (cart == null) {
                    cart = new ArrayList<>();
                    session.setAttribute("cart", cart);
                }
                
                CartItem cartItem = new CartItem(itemId, item.getItemName(), quantity, item.getPrice());
                cart.add(cartItem);
                
                response.sendRedirect(request.getContextPath() + "/webstore");
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid input");
                doGet(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "An error occurred: " + e.getMessage());
                doGet(request, response);
            }
        }
    }
}
