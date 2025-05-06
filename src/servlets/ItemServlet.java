package servlets;

import command.*;
import dao.ItemDAO;
import model.Item;
import view.ItemManagementView;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/item-management")
public class ItemServlet extends HttpServlet {
    private ItemDAO itemDAO;
    private ItemManagementView view;

    @Override
    public void init() throws ServletException {
        super.init();
        itemDAO = new ItemDAO();
        view = new ItemManagementView();
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
                    Command deleteCommand = new DeleteItemCommand(itemDAO, view);
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
                Item newItem = new Item();
                newItem.setCode(request.getParameter("code"));
                newItem.setName(request.getParameter("name"));
                newItem.setDescription(request.getParameter("description"));
                newItem.setPrice(Double.parseDouble(request.getParameter("price")));
                newItem.setQuantity(Integer.parseInt(request.getParameter("quantity")));
                
                Command addCommand = new AddItemCommand(itemDAO, view);
                addCommand.execute();
                response.sendRedirect("item-management");
                break;
                
            case "edit":
                Item updatedItem = new Item();
                updatedItem.setCode(request.getParameter("code"));
                updatedItem.setName(request.getParameter("name"));
                updatedItem.setDescription(request.getParameter("description"));
                updatedItem.setPrice(Double.parseDouble(request.getParameter("price")));
                updatedItem.setQuantity(Integer.parseInt(request.getParameter("quantity")));
                
                Command editCommand = new EditItemCommand(itemDAO, view);
                editCommand.execute();
                response.sendRedirect("item-management");
                break;
        }
    }
}
