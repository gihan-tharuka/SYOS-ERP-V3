package servlets;

import command.*;
import dao.ItemDAO;
import dao.DatabaseConnection;
import model.Item;
import view.ItemManagementView;
import observer.ReorderSubject;
import observer.Subject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/item-management")
public class ItemServlet extends HttpServlet {
    private ItemDAO itemDAO;
    private ItemManagementView view;
    private ReorderSubject reorderSubject;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Get database connection from singleton
            Connection connection = DatabaseConnection.getInstance().getConnection();
            
            // Initialize reorder subject
            reorderSubject = new ReorderSubject();
            
            // Initialize DAO and view
            itemDAO = new ItemDAO(connection, reorderSubject);
            view = new ItemManagementView();
        } catch (Exception e) {
            throw new ServletException("Error initializing servlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            // Default action - view all items
            Command viewAllCommand = new ViewAllItemsCommand(itemDAO, view);
            viewAllCommand.execute();
            request.setAttribute("items", itemDAO.getAllItems());
            request.getRequestDispatcher("/jsp/item/list.jsp").forward(request, response);
        } else {
            switch (action) {
                case "add":
                    request.getRequestDispatcher("/jsp/item/add.jsp").forward(request, response);
                    break;
                case "edit":
                    String itemCode = request.getParameter("code");
                    Item item = itemDAO.getItemByCode(itemCode);
                    request.setAttribute("item", item);
                    request.getRequestDispatcher("/jsp/item/edit.jsp").forward(request, response);
                    break;
                case "delete":
                    itemCode = request.getParameter("code");
                    DeleteItemCommand deleteCommand = new DeleteItemCommand(itemDAO, view);
                    deleteCommand.setItemCode(itemCode);
                    deleteCommand.execute();
                    response.sendRedirect("item-management");
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        switch (action) {
            case "add":
                String itemCode = request.getParameter("code");
                String itemName = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                double discount = Double.parseDouble(request.getParameter("discount"));
                
                Item newItem = new Item(0, itemCode, itemName, price, discount);
                AddItemCommand addCommand = new AddItemCommand(itemDAO, view);
                addCommand.setItem(newItem);
                addCommand.execute();
                response.sendRedirect("item-management");
                break;
                
            case "edit":
                itemCode = request.getParameter("code");
                itemName = request.getParameter("name");
                price = Double.parseDouble(request.getParameter("price"));
                discount = Double.parseDouble(request.getParameter("discount"));
                
                Item updatedItem = new Item(0, itemCode, itemName, price, discount);
                EditItemCommand editCommand = new EditItemCommand(itemDAO, view);
                editCommand.setItem(updatedItem);
                editCommand.execute();
                response.sendRedirect("item-management");
                break;
        }
    }
}
