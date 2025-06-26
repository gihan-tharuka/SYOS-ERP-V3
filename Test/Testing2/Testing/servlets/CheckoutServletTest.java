package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.CheckoutServlet;
import model.CartItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CheckoutServletTest {
    private CheckoutServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = new CheckoutServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void testDoPost_EmptyCart() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(null);
        when(request.getContextPath()).thenReturn("");
        doNothing().when(response).sendRedirect(anyString());

        Method doPost = CheckoutServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(response).sendRedirect(contains("/webstore"));
    }

    @Test
    public void testDoPost_MissingUserId() throws Exception {
        List<CartItem> cart = new ArrayList<>();
        cart.add(mock(CartItem.class));
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(session.getAttribute("userId")).thenReturn(null);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(request, response);

        Method doPost = CheckoutServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(request).setAttribute(eq("error"), contains("User session expired"));
        verify(request).getRequestDispatcher(contains("/jsp/webstore/cart.jsp"));
        verify(dispatcher).forward(request, response);
    }
} 