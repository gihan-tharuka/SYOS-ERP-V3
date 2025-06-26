package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.CustomerRegisterServlet;
import dao.UserDAO;
import model.Customer;
import model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class CustomerRegisterServletTest {
    private CustomerRegisterServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private UserDAO userDAOMock;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = spy(new CustomerRegisterServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        userDAOMock = mock(UserDAO.class);
        dispatcher = mock(RequestDispatcher.class);

        // Inject mock UserDAO
        Field daoField = CustomerRegisterServlet.class.getDeclaredField("userDAO");
        daoField.setAccessible(true);
        daoField.set(servlet, userDAOMock);
    }

    @Test
    public void testDoPost_Success() throws Exception {
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("testpass");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("mobile")).thenReturn("1234567890");
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        doNothing().when(response).sendRedirect(anyString());

        Method doPost = CustomerRegisterServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(userDAOMock).addUser(any(User.class), eq("customer"));
        verify(session).setAttribute("username", "testuser");
        verify(session).setAttribute("role", "customer");
        verify(response).sendRedirect(contains("/jsp/customer/dashboard.jsp"));
    }

    @Test
    public void testDoPost_Error() throws Exception {
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("testpass");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("mobile")).thenReturn("1234567890");
        doThrow(new RuntimeException("DB error")).when(userDAOMock).addUser(any(User.class), eq("customer"));
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(request, response);

        Method doPost = CustomerRegisterServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(request).setAttribute(eq("error"), contains("Registration failed"));
        verify(request).getRequestDispatcher(contains("/jsp/customer/register.jsp"));
        verify(dispatcher).forward(request, response);
    }
} 