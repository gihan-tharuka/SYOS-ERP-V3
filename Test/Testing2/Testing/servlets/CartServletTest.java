package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.CartServlet;
import model.CartItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CartServletTest {
    private CartServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = new CartServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void testDoGet_EmptyCart() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(null);
        when(request.getContextPath()).thenReturn("");
        doNothing().when(response).sendRedirect(anyString());

        java.lang.reflect.Method doGet = CartServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        verify(response).sendRedirect(contains("/webstore"));
    }

    @Test
    public void testDoGet_NonEmptyCart() throws Exception {
        List<CartItem> cart = new ArrayList<>();
        CartItem item = mock(CartItem.class);
        when(item.getTotalPrice()).thenReturn(10.0);
        cart.add(item);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(request, response);

        java.lang.reflect.Method doGet = CartServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        verify(request).setAttribute(eq("cart"), eq(cart));
        verify(request).setAttribute(eq("totalAmount"), eq(10.0));
        verify(request).getRequestDispatcher(contains("/jsp/webstore/cart.jsp"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_RemoveAction() throws Exception {
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("index")).thenReturn("0");
        when(request.getSession()).thenReturn(session);
        List<CartItem> cart = new ArrayList<>();
        cart.add(mock(CartItem.class));
        when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getContextPath()).thenReturn("");
        doNothing().when(response).sendRedirect(anyString());

        java.lang.reflect.Method doPost = CartServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(response).sendRedirect(contains("/cart"));
    }
} 