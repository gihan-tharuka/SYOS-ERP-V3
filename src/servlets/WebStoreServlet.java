package servlets;

import dao.DatabaseConnection;
import dao.ItemDAO;
import dao.WebStockDAO;
import model.CartItem;
import model.Item;
import model.WebStock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
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
        request.setAttribute("webStocks", webStocks);
        request.getRequestDispatcher("/jsp/webstore/webstore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("addToCart".equals(action)) {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            WebStock webStock = webStockDAO.getWebStockByItemId(itemId);
            if (webStock != null && quantity <= webStock.getCurrentQuantity()) {
                Item item = itemDAO.getItemByCode(webStockDAO.getAllWebStocksWithItemCodes().get(webStock));
                
                HttpSession session = request.getSession();
                List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
                if (cart == null) {
                    cart = new ArrayList<>();
                    session.setAttribute("cart", cart);
                }
                
                CartItem cartItem = new CartItem(itemId, item.getItemName(), quantity, item.getPrice());
                cart.add(cartItem);
                
                response.sendRedirect(request.getContextPath() + "/webstore");
            } else {
                request.setAttribute("error", "Invalid quantity or item not available");
                doGet(request, response);
            }
        }
    }
}
