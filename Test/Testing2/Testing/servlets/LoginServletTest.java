package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.LoginServlet;
import dao.UserDAO;
import authentication.AdminAuthenticationHandler;
import authentication.CashierAuthenticationHandler;
import authentication.CustomerAuthenticationHandler;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class LoginServletTest {
    private LoginServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AsyncContext asyncContext;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = spy(new LoginServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        asyncContext = mock(AsyncContext.class);
        writer = mock(PrintWriter.class);

        // Inject mocks for userDAO and handlers if needed
        Field userDAOField = LoginServlet.class.getDeclaredField("userDAO");
        userDAOField.setAccessible(true);
        userDAOField.set(servlet, mock(UserDAO.class));
        Field adminHandlerField = LoginServlet.class.getDeclaredField("adminHandler");
        adminHandlerField.setAccessible(true);
        adminHandlerField.set(servlet, mock(AdminAuthenticationHandler.class));
        Field cashierHandlerField = LoginServlet.class.getDeclaredField("cashierHandler");
        cashierHandlerField.setAccessible(true);
        cashierHandlerField.set(servlet, mock(CashierAuthenticationHandler.class));
        Field customerHandlerField = LoginServlet.class.getDeclaredField("customerHandler");
        customerHandlerField.setAccessible(true);
        customerHandlerField.set(servlet, mock(CustomerAuthenticationHandler.class));
    }

    @Test
    public void testDoPost_ValidLoginRequest() throws Exception {
        when(request.getParameter("username")).thenReturn("user1");
        when(request.getParameter("password")).thenReturn("pass1");
        when(request.getParameter("role")).thenReturn("admin");
        when(request.startAsync()).thenReturn(asyncContext);
        when(response.getWriter()).thenReturn(writer);

        Method doPost = LoginServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(request).startAsync();
        verify(response).setContentType("text/plain");
        verify(writer).write(contains("Login request accepted"));
    }

    @Test
    public void testDoPost_MissingParameter() throws Exception {
        when(request.getParameter("username")).thenReturn(null); // missing username
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("login.jsp")).thenReturn(dispatcher);

        Method doPost = LoginServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(request).setAttribute(eq("error"), contains("Missing required login information"));
        verify(request).getRequestDispatcher("login.jsp");
        verify(dispatcher).forward(request, response);
    }
} 