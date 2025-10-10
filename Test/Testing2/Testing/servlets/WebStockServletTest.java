package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.WebStockServlet;
import controller.WebStockManagementController;
import dao.WebStockDAO;
import dao.MainStockDAO;
import dao.ItemDAO;
import dao.ReorderLevelDAO;
import model.WebStock;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class WebStockServletTest {
    private WebStockServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private WebStockManagementController controllerMock;
    private WebStockDAO webStockDAOMock;
    private MainStockDAO mainStockDAOMock;
    private ItemDAO itemDAOMock;
    private ReorderLevelDAO reorderLevelDAOMock;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = spy(new WebStockServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        controllerMock = mock(WebStockManagementController.class);
        webStockDAOMock = mock(WebStockDAO.class);
        mainStockDAOMock = mock(MainStockDAO.class);
        itemDAOMock = mock(ItemDAO.class);
        reorderLevelDAOMock = mock(ReorderLevelDAO.class);

        // Inject controller mock
        Field controllerField = WebStockServlet.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(servlet, controllerMock);

        // Setup controller DAO getters
        when(controllerMock.getWebStockDAO()).thenReturn(webStockDAOMock);
        when(controllerMock.getMainStockDAO()).thenReturn(mainStockDAOMock);
        when(controllerMock.getItemDAO()).thenReturn(itemDAOMock);
        when(controllerMock.getReorderLevelDAO()).thenReturn(reorderLevelDAOMock);
    }

    @Test
    public void testDoGet_ViewAction() throws Exception {
        when(request.getPathInfo()).thenReturn("/view");
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/jsp/webstock/view.jsp")).thenReturn(dispatcher);
        when(webStockDAOMock.getAllWebStocksWithItemCodes()).thenReturn(Collections.emptyMap());

        Method doGet = WebStockServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        verify(request).setAttribute(eq("webStocks"), any());
        verify(request).getRequestDispatcher("/jsp/webstock/view.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_AddAction() throws Exception {
        when(request.getPathInfo()).thenReturn("/add");
        when(request.getParameter("itemCode")).thenReturn("ITEM001");
        when(request.getParameter("webCapacity")).thenReturn("10");
        when(itemDAOMock.getItemIdByCode("ITEM001")).thenReturn(1);
        when(webStockDAOMock.addWebStock(any(WebStock.class))).thenReturn(true);
        doNothing().when(response).sendRedirect(anyString());

        Method doPost = WebStockServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(response).sendRedirect(contains("/webstock/view"));
    }
} 