package servlets;

import controller.ItemManagementController;
import dao.DatabaseConnection;
import dao.ItemDAO;
import model.Item;
import observer.ReorderSubject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/items/*")
public class ItemServlet extends HttpServlet {
    private ItemDAO itemDAO;
    private ItemManagementController controller;
    private ReorderSubject reorderSubject;

    @Override
    public void init() throws ServletException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        reorderSubject = new ReorderSubject();
        itemDAO = new ItemDAO(connection, reorderSubject);
        controller = new ItemManagementController(itemDAO, null); // View is handled by JSPs
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null || action.equals("/")) {
            List<Item> items = itemDAO.getAllItems();
            request.setAttribute("items", items);
            request.getRequestDispatcher("/jsp/item/list.jsp").forward(request, response);
        } else if (action.equals("/add")) {
            request.getRequestDispatcher("/jsp/item/add.jsp").forward(request, response);
        } else if (action.equals("/edit")) {
            String itemCode = request.getParameter("code");
            Item item = itemDAO.getItemByCode(itemCode);
            if (item != null) {
                request.setAttribute("item", item);
                request.getRequestDispatcher("/jsp/item/edit.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/items");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action.equals("/add")) {
            String itemCode = request.getParameter("itemCode");
            String itemName = request.getParameter("itemName");
            double price = Double.parseDouble(request.getParameter("price"));
            double discount = Double.parseDouble(request.getParameter("discount"));
            
            Item item = new Item(0, itemCode, itemName, price, discount);
            itemDAO.addItem(item);
            response.sendRedirect(request.getContextPath() + "/items");
            
        } else if (action.equals("/edit")) {
            String itemCode = request.getParameter("itemCode");
            String itemName = request.getParameter("itemName");
            double price = Double.parseDouble(request.getParameter("price"));
            double discount = Double.parseDouble(request.getParameter("discount"));
            
            Item item = new Item(0, itemCode, itemName, price, discount);
            itemDAO.updateItem(item);
            response.sendRedirect(request.getContextPath() + "/items");
            
        } else if (action.equals("/delete")) {
            String itemCode = request.getParameter("code");
            itemDAO.deleteItemByCode(itemCode);
            response.sendRedirect(request.getContextPath() + "/items");
        }
    }
}    