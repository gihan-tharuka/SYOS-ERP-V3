package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.ReOrderLevelServlet;
import dao.ReorderLevelDAO;
import model.ReorderLevel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class ReOrderLevelServletTest {
    private ReOrderLevelServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private ReorderLevelDAO reorderLevelDAOMock;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = new ReOrderLevelServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        reorderLevelDAOMock = mock(ReorderLevelDAO.class);

        // Inject mock ReorderLevelDAO
        Field daoField = ReOrderLevelServlet.class.getDeclaredField("reorderLevelDAO");
        daoField.setAccessible(true);
        daoField.set(servlet, reorderLevelDAOMock);
    }

    @Test
    public void testDoGet_ListAll() throws Exception {
        when(request.getPathInfo()).thenReturn(null);
        when(reorderLevelDAOMock.getAllReorderLevels()).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(request, response);

        Method doGet = ReOrderLevelServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        verify(request).setAttribute(eq("reorderLevels"), any());
        verify(request).getRequestDispatcher(contains("/jsp/reorderlevel/list.jsp"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_AddAction() throws Exception {
        when(request.getPathInfo()).thenReturn(null);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("itemId")).thenReturn("1");
        when(request.getParameter("thresholdQuantity")).thenReturn("10");
        when(request.getContextPath()).thenReturn("");
        doNothing().when(response).sendRedirect(anyString());

        Method doPost = ReOrderLevelServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(reorderLevelDAOMock).addReorderLevel(1, 10);
        verify(response).sendRedirect(contains("/reorderlevel"));
    }
} 