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

@WebServlet("/reorderlevel/*")
public class ReOrderLevelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DatabaseConnection dbConnection;
    private ReorderLevelDAO reorderLevelDAO;

    public void init() {
        dbConnection = DatabaseConnection.getInstance();
        Connection connection = dbConnection.getConnection();
        reorderLevelDAO = new ReorderLevelDAO(connection);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null || action.equals("/")) {
            // List all reorder levels
            request.setAttribute("reorderLevels", reorderLevelDAO.getAllReorderLevels());
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
            } else if ("edit".equals(formAction)) {
                int reorderLevelId = Integer.parseInt(request.getParameter("reorderLevelId"));
                int thresholdQuantity = Integer.parseInt(request.getParameter("thresholdQuantity"));
                reorderLevelDAO.updateReorderLevel(reorderLevelId, thresholdQuantity);
            } else if ("delete".equals(formAction)) {
                int reorderLevelId = Integer.parseInt(request.getParameter("reorderLevelId"));
                reorderLevelDAO.deleteReorderLevel(reorderLevelId);
            }
            
            response.sendRedirect(request.getContextPath() + "/reorderlevel");
        }
    }

    public void destroy() {
        dbConnection.closeAllConnections();
    }
}
