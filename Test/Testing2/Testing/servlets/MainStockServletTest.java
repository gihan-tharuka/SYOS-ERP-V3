package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.MainStockServlet;
import dao.MainStockDAO;
import dao.ItemDAO;
import dao.UserDAO;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class MainStockServletTest {
    private MainStockServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AsyncContext asyncContext;
    private PrintWriter writer;
    private MainStockDAO mainStockDAOMock;
    private ItemDAO itemDAOMock;
    private UserDAO userDAOMock;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = spy(new MainStockServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        asyncContext = mock(AsyncContext.class);
        writer = mock(PrintWriter.class);

        // Inject mocks for DAOs
        mainStockDAOMock = mock(MainStockDAO.class);
        itemDAOMock = mock(ItemDAO.class);
        userDAOMock = mock(UserDAO.class);

        Field mainStockDAOField = MainStockServlet.class.getDeclaredField("mainStockDAO");
        mainStockDAOField.setAccessible(true);
        mainStockDAOField.set(servlet, mainStockDAOMock);

        Field itemDAOField = MainStockServlet.class.getDeclaredField("itemDAO");
        itemDAOField.setAccessible(true);
        itemDAOField.set(servlet, itemDAOMock);

        Field userDAOField = MainStockServlet.class.getDeclaredField("userDAO");
        userDAOField.setAccessible(true);
        userDAOField.set(servlet, userDAOMock);
    }

    @Test
    public void testDoPost_ValidAddOperation() throws Exception {
        when(request.getPathInfo()).thenReturn("/add");
        when(request.startAsync()).thenReturn(asyncContext);
        when(request.getParameterMap()).thenReturn(Collections.emptyMap());
        when(response.getWriter()).thenReturn(writer);

        Method doPost = MainStockServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(request).startAsync();
        verify(response).setContentType("text/plain");
        verify(writer).write(contains("Stock operation request accepted"));
    }

    @Test
    public void testDoGet_ViewAction() throws Exception {
        when(request.getPathInfo()).thenReturn("/view");
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/jsp/mainstock/list.jsp")).thenReturn(dispatcher);
        when(mainStockDAOMock.getAllMainStocks()).thenReturn(Collections.emptyList());

        Method doGet = MainStockServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        verify(request).setAttribute(eq("mainStocks"), any());
        verify(request).getRequestDispatcher("/jsp/mainstock/list.jsp");
        verify(dispatcher).forward(request, response);
    }
} 