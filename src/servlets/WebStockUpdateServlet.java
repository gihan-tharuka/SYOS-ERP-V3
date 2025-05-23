package servlets;

import dao.DatabaseConnection;
import dao.WebStockDAO;
import model.WebStock;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;

@WebServlet("/webstock/updates")
public class WebStockUpdateServlet extends HttpServlet {
    private WebStockDAO webStockDAO;

    @Override
    public void init() throws ServletException {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        webStockDAO = new WebStockDAO(dbConnection.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Set CORS headers
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type");
            
            Map<WebStock, String> webStocks = webStockDAO.getAllWebStocksWithItemCodes();
            
            // Create a simplified map of itemId -> quantity for the response
            Map<Integer, Integer> stockUpdates = new HashMap<>();
            for (WebStock stock : webStocks.keySet()) {
                stockUpdates.put(stock.getItemId(), stock.getCurrentQuantity());
            }

            // Convert to JSON
            JSONObject jsonResponse = new JSONObject(stockUpdates);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", e.getMessage());
            response.getWriter().write(errorJson.toString());
        }
    }
} 