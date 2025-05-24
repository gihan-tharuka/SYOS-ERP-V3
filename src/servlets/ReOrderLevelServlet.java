package servlets;

import java.io.IOException;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.DatabaseConnection;
import dao.ReorderLevelDAO;
import model.ReorderLevel;
import com.google.gson.Gson;
import java.util.List;

@WebServlet("/reorderlevel/*")
public class ReOrderLevelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DatabaseConnection dbConnection;
    private ReorderLevelDAO reorderLevelDAO;
    private Gson gson;

    public void init() {
        dbConnection = DatabaseConnection.getInstance();
        Connection connection = dbConnection.getConnection();
        reorderLevelDAO = new ReorderLevelDAO(connection);
        gson = new Gson();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null || action.equals("/")) {
            // List all reorder levels
            List<ReorderLevel> reorderLevels = reorderLevelDAO.getAllReorderLevels();
            request.setAttribute("reorderLevels", reorderLevels);
            request.getRequestDispatcher("/jsp/reorderlevel/list.jsp").forward(request, response);
        } else if (action.equals("/edit")) {
            // Show edit form
            int reorderLevelId = Integer.parseInt(request.getParameter("id"));
            ReorderLevel reorderLevel = reorderLevelDAO.getReorderLevelById(reorderLevelId);
            request.setAttribute("reorderLevel", reorderLevel);
            request.getRequestDispatcher("/jsp/reorderlevel/edit.jsp").forward(request, response);
        } else if (action.equals("/add")) {
            // Show add form with available items
            request.setAttribute("availableItems", reorderLevelDAO.getAvailableItems());
            request.getRequestDispatcher("/jsp/reorderlevel/add.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null || action.equals("/")) {
            // Handle form submission
            String formAction = request.getParameter("action");
            
            if ("add".equals(formAction)) {
                int itemId = Integer.parseInt(request.getParameter("itemId"));
                int thresholdQuantity = Integer.parseInt(request.getParameter("thresholdQuantity"));
                reorderLevelDAO.addReorderLevel(itemId, thresholdQuantity);
                broadcastReorderLevelUpdate("add", reorderLevelDAO.getReorderLevelByItemId(itemId));
            } else if ("edit".equals(formAction)) {
                int reorderLevelId = Integer.parseInt(request.getParameter("reorderLevelId"));
                int thresholdQuantity = Integer.parseInt(request.getParameter("thresholdQuantity"));
                reorderLevelDAO.updateReorderLevel(reorderLevelId, thresholdQuantity);
                broadcastReorderLevelUpdate("update", reorderLevelDAO.getReorderLevelById(reorderLevelId));
            } else if ("delete".equals(formAction)) {
                int reorderLevelId = Integer.parseInt(request.getParameter("reorderLevelId"));
                ReorderLevel reorderLevel = reorderLevelDAO.getReorderLevelById(reorderLevelId);
                reorderLevelDAO.deleteReorderLevel(reorderLevelId);
                broadcastReorderLevelUpdate("delete", reorderLevel);
            }
            
            response.sendRedirect(request.getContextPath() + "/reorderlevel");
        }
    }

    private void broadcastReorderLevelUpdate(String eventType, ReorderLevel reorderLevel) {
        if (reorderLevel != null) {
            String jsonData = gson.toJson(reorderLevel);
            ReorderLevelSSEServlet.broadcastUpdate(eventType, jsonData);
        }
    }

    public void destroy() {
        dbConnection.closeAllConnections();
    }
}
