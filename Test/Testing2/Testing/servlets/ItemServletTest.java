package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.ItemServlet;
import dao.ItemDAO;
import view.ItemManagementView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.*;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class ItemServletTest {
    private ItemServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private ItemDAO itemDAOMock;
    private ItemManagementView viewMock;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = spy(new ItemServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        // Inject mocks for itemDAO and view
        itemDAOMock = mock(ItemDAO.class);
        viewMock = mock(ItemManagementView.class);
        Field itemDAOField = ItemServlet.class.getDeclaredField("itemDAO");
        itemDAOField.setAccessible(true);
        itemDAOField.set(servlet, itemDAOMock);
        Field viewField = ItemServlet.class.getDeclaredField("view");
        viewField.setAccessible(true);
        viewField.set(servlet, viewMock);

        doNothing().when(servlet).init(any(ServletConfig.class));
        servlet.init(mock(ServletConfig.class));
    }

    @Test
    public void testDoGet_DeleteAction() throws Exception {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("code")).thenReturn("ITEM001");
        doNothing().when(response).sendRedirect(anyString());
        Method doGet = ItemServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);
        verify(response).sendRedirect("item-management");
    }

    @Test
    public void testDoPost_AddAction() throws Exception {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("code")).thenReturn("ITEM001");
        when(request.getParameter("name")).thenReturn("Test Item");
        when(request.getParameter("price")).thenReturn("10.0");
        when(request.getParameter("discount")).thenReturn("0.0");
        doNothing().when(response).sendRedirect(anyString());
        Method doPost = ItemServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);
        verify(response).sendRedirect("item-management");
    }
} 