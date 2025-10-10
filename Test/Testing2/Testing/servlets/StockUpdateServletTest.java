package Testing2.Testing.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlets.StockUpdateServlet;
import dao.WebStockDAO;
import model.WebStock;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class StockUpdateServletTest {
    private StockUpdateServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private WebStockDAO webStockDAOMock;
    private PrintWriter writer;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = spy(new StockUpdateServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        webStockDAOMock = mock(WebStockDAO.class);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        // Inject mock WebStockDAO
        Field daoField = StockUpdateServlet.class.getDeclaredField("webStockDAO");
        daoField.setAccessible(true);
        daoField.set(servlet, webStockDAOMock);
    }

    @Test
    public void testDoGet_ReturnsJson() throws Exception {
        // Prepare mock data
        WebStock stock = mock(WebStock.class);
        when(stock.getCurrentQuantity()).thenReturn(5);
        when(stock.getItemId()).thenReturn(1);
        Map<WebStock, String> webStocks = new HashMap<>();
        webStocks.put(stock, "ITEM001");
        when(webStockDAOMock.getAllWebStocksWithItemCodes()).thenReturn(webStocks);
        when(response.getWriter()).thenReturn(writer);

        // Call doGet via reflection
        Method doGet = StockUpdateServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        writer.flush();
        String output = stringWriter.toString();
        // Should contain the mocked itemId and itemCode
        assert output.contains("\"1\":");
        assert output.contains("\"itemCode\":\"ITEM001\"");
        assert output.contains("\"currentQuantity\":5");
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(webStockDAOMock).getAllWebStocksWithItemCodes();
    }
} 