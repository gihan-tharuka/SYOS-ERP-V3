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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.nio.file.Paths;
import java.nio.file.Files;

@WebServlet("/item-management")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
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
        String imagePath = null;
        String uploadDir = getServletContext().getRealPath("/resources/item-images");
        Files.createDirectories(Paths.get(uploadDir));
        Part imagePart = request.getPart("image");
        if (imagePart != null && imagePart.getSize() > 0) {
            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
            String savedFileName = System.currentTimeMillis() + "_" + fileName;
            String fullPath = Paths.get(uploadDir, savedFileName).toString();
            imagePart.write(fullPath);
            imagePath = "resources/item-images/" + savedFileName;
        }
        switch (action) {
            case "add": {
                String itemCode = request.getParameter("code");
                String itemName = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                double discount = Double.parseDouble(request.getParameter("discount"));
                Item newItem = new Item(0, itemCode, itemName, price, discount, imagePath);
                AddItemCommand addCommand = new AddItemCommand(itemDAO, view);
                addCommand.setItem(newItem);
                addCommand.execute();
                response.sendRedirect("item-management");
                break;
            }
            case "edit": {
                String itemCode = request.getParameter("code");
                String itemName = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                double discount = Double.parseDouble(request.getParameter("discount"));
                Item existingItem = itemDAO.getItemByCode(itemCode);
                String finalImagePath = imagePath != null ? imagePath : (existingItem != null ? existingItem.getImagePath() : null);
                Item updatedItem = new Item(0, itemCode, itemName, price, discount, finalImagePath);
                EditItemCommand editCommand = new EditItemCommand(itemDAO, view);
                editCommand.setItem(updatedItem);
                editCommand.execute();
                response.sendRedirect("item-management");
                break;
            }
        }
    }
}
