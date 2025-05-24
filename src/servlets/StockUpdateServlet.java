package servlets;

import dao.WebStockDAO;
import dao.DatabaseConnection;
import model.WebStock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/api/stock-updates")
public class StockUpdateServlet extends HttpServlet {
    private WebStockDAO webStockDAO;

    @Override
    public void init() throws ServletException {
        webStockDAO = new WebStockDAO(DatabaseConnection.getInstance().getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<WebStock, String> webStocks = webStockDAO.getAllWebStocksWithItemCodes();
        
        // Create a simplified map for JSON response
        Map<Integer, Map<String, Object>> simplifiedStocks = new HashMap<>();
        for (Map.Entry<WebStock, String> entry : webStocks.entrySet()) {
            WebStock stock = entry.getKey();
            Map<String, Object> stockData = new HashMap<>();
            stockData.put("currentQuantity", stock.getCurrentQuantity());
            stockData.put("itemCode", entry.getValue());
            simplifiedStocks.put(stock.getItemId(), stockData);
        }
        
        // Convert to JSON string manually
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<Integer, Map<String, Object>> entry : simplifiedStocks.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            json.append("\"").append(entry.getKey()).append("\":{");
            boolean firstInner = true;
            for (Map.Entry<String, Object> innerEntry : entry.getValue().entrySet()) {
                if (!firstInner) {
                    json.append(",");
                }
                firstInner = false;
                json.append("\"").append(innerEntry.getKey()).append("\":");
                if (innerEntry.getValue() instanceof String) {
                    json.append("\"").append(innerEntry.getValue()).append("\"");
                } else {
                    json.append(innerEntry.getValue());
                }
            }
            json.append("}");
        }
        json.append("}");
        
        response.getWriter().write(json.toString());
    }
} 