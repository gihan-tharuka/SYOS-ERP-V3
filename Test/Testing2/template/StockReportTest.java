package Testing2.template;

import dao.ReportDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.StockReport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class StockReportTest {

    private StockReport stockReport;
    private ReportDAO reportDAO;

    // Subclass to expose the protected methods
    private class TestableStockReport extends StockReport {
        public TestableStockReport(ReportDAO reportDAO) {
            super(reportDAO);
        }

        public void publicFetchData() {
            fetchData();
        }

        public void publicGenerateOutput() {
            generateOutput();
        }
    }

    @BeforeEach
    public void setUp() {
        reportDAO = mock(ReportDAO.class);
        stockReport = new TestableStockReport(reportDAO);
    }

    @Test
    public void testFetchData() {
        // Prepare mock data
        List<Object[]> mockData = new ArrayList<>();
        mockData.add(new Object[]{"code1", "Item1", "batch1", 10, new Date(System.currentTimeMillis())});
        when(reportDAO.getStockReport()).thenReturn(mockData);

        // Fetch data
        ((TestableStockReport) stockReport).publicFetchData();

        // Verify fetchData calls getStockReport
        verify(reportDAO).getStockReport();
    }

    @Test
    public void testGenerateOutput() {
        // Prepare mock data
        List<Object[]> mockData = new ArrayList<>();
        mockData.add(new Object[]{"code1", "Item1", "batch1", 10, new Date(System.currentTimeMillis())});
        when(reportDAO.getStockReport()).thenReturn(mockData);

        // Fetch data and generate output
        ((TestableStockReport) stockReport).publicFetchData();
        ((TestableStockReport) stockReport).publicGenerateOutput();

        // Since this test outputs to the console, we will just verify interactions
        verify(reportDAO).getStockReport();
    }
}
