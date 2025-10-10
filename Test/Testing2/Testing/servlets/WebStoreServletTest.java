package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.WebStoreServlet;
import dao.WebStockDAO;
import dao.ItemDAO;
import model.WebStock;
import model.Item;
import model.CartItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import static org.mockito.Mockito.*;

public class WebStoreServletTest {
    private WebStoreServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private WebStockDAO webStockDAOMock;
    private ItemDAO itemDAOMock;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = new WebStoreServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        webStockDAOMock = mock(WebStockDAO.class);
        itemDAOMock = mock(ItemDAO.class);

        // Inject mocks
        Field webStockDAOField = WebStoreServlet.class.getDeclaredField("webStockDAO");
        webStockDAOField.setAccessible(true);
        webStockDAOField.set(servlet, webStockDAOMock);
        Field itemDAOField = WebStoreServlet.class.getDeclaredField("itemDAO");
        itemDAOField.setAccessible(true);
        itemDAOField.set(servlet, itemDAOMock);
    }

    @Test
    public void testDoGet_ForwardsToJsp() throws Exception {
        Map<WebStock, String> webStocks = new HashMap<>();
        when(webStockDAOMock.getAllWebStocksWithItemCodes()).thenReturn(webStocks);
        when(itemDAOMock.getItemByCode(anyString())).thenReturn(mock(Item.class));
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(request, response);

        Method doGet = WebStoreServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        verify(request).setAttribute(eq("webStocks"), any());
        verify(request).setAttribute(eq("webStockItems"), any());
        verify(request).getRequestDispatcher(contains("/jsp/webstore/webstore.jsp"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_AddToCart_CreatesCart() throws Exception {
        when(request.getParameter("action")).thenReturn("addToCart");
        when(request.getParameter("itemId")).thenReturn("1");
        when(request.getParameter("quantity")).thenReturn("2");
        WebStock webStock = mock(WebStock.class);
        when(webStock.getCurrentQuantity()).thenReturn(10);
        when(webStockDAOMock.getWebStockByItemId(1)).thenReturn(webStock);
        when(itemDAOMock.getItemCodeById(1)).thenReturn("ITEM001");
        Item item = mock(Item.class);
        when(itemDAOMock.getItemByCode("ITEM001")).thenReturn(item);
        when(item.getItemName()).thenReturn("Test Item");
        when(item.getPrice()).thenReturn(5.0);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(null); // cart does not exist
        when(request.getContextPath()).thenReturn("");
        doNothing().when(response).sendRedirect(anyString());

        Method doPost = WebStoreServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(session).setAttribute(eq("cart"), any());
        verify(response).sendRedirect(contains("/webstore"));
    }

    @Test
    public void testDoPost_AddToCart_ExistingCart() throws Exception {
        when(request.getParameter("action")).thenReturn("addToCart");
        when(request.getParameter("itemId")).thenReturn("1");
        when(request.getParameter("quantity")).thenReturn("2");
        WebStock webStock = mock(WebStock.class);
        when(webStock.getCurrentQuantity()).thenReturn(10);
        when(webStockDAOMock.getWebStockByItemId(1)).thenReturn(webStock);
        when(itemDAOMock.getItemCodeById(1)).thenReturn("ITEM001");
        Item item = mock(Item.class);
        when(itemDAOMock.getItemByCode("ITEM001")).thenReturn(item);
        when(item.getItemName()).thenReturn("Test Item");
        when(item.getPrice()).thenReturn(5.0);
        when(request.getSession()).thenReturn(session);
        List<CartItem> cart = new ArrayList<>();
        when(session.getAttribute("cart")).thenReturn(cart); // cart already exists
        when(request.getContextPath()).thenReturn("");
        doNothing().when(response).sendRedirect(anyString());

        Method doPost = WebStoreServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(session, never()).setAttribute(eq("cart"), any());
        verify(response).sendRedirect(contains("/webstore"));
    }
} 