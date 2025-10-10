package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.ShelfStockServlet;
import controller.ShelfStockManagementController;
import dao.ShelfStockDAO;
import dao.MainStockDAO;
import dao.ItemDAO;
import dao.ReorderLevelDAO;
import model.ShelfStock;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ShelfStockServletTest {
    private ShelfStockServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ShelfStockManagementController controllerMock;
    private ShelfStockDAO shelfStockDAOMock;
    private MainStockDAO mainStockDAOMock;
    private ItemDAO itemDAOMock;
    private ReorderLevelDAO reorderLevelDAOMock;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = spy(new ShelfStockServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        controllerMock = mock(ShelfStockManagementController.class);
        shelfStockDAOMock = mock(ShelfStockDAO.class);
        mainStockDAOMock = mock(MainStockDAO.class);
        itemDAOMock = mock(ItemDAO.class);
        reorderLevelDAOMock = mock(ReorderLevelDAO.class);

        // Inject controller mock
        Field controllerField = ShelfStockServlet.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(servlet, controllerMock);

        // Setup controller DAO getters
        when(controllerMock.getShelfStockDAO()).thenReturn(shelfStockDAOMock);
        when(controllerMock.getMainStockDAO()).thenReturn(mainStockDAOMock);
        when(controllerMock.getItemDAO()).thenReturn(itemDAOMock);
        when(controllerMock.getReorderLevelDAO()).thenReturn(reorderLevelDAOMock);
    }

    @Test
    public void testDoGet_ViewAction() throws Exception {
        when(request.getPathInfo()).thenReturn("/view");
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/jsp/shelfstock/view.jsp")).thenReturn(dispatcher);
        when(shelfStockDAOMock.getAllShelfStocksWithItemCodes()).thenReturn(Collections.emptyMap());

        Method doGet = ShelfStockServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        verify(request).setAttribute(eq("shelfStocks"), any());
        verify(request).getRequestDispatcher("/jsp/shelfstock/view.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_AddAction() throws Exception {
        when(request.getPathInfo()).thenReturn("/add");
        when(request.getParameter("itemCode")).thenReturn("ITEM001");
        when(request.getParameter("shelfCapacity")).thenReturn("10");
        when(itemDAOMock.getItemIdByCode("ITEM001")).thenReturn(1);
        when(shelfStockDAOMock.addShelfStock(any(ShelfStock.class))).thenReturn(true);
        doNothing().when(response).sendRedirect(anyString());

        Method doPost = ShelfStockServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(response).sendRedirect(contains("/shelfstock/view"));
    }
} 