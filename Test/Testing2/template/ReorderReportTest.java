package Testing2.template;

import dao.ReportDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.ReorderReport;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ReorderReportTest {

    private ReorderReport reorderReport;
    private ReportDAO reportDAO;

    // Subclass to expose the protected methods
    private class TestableReorderReport extends ReorderReport {
        public TestableReorderReport(ReportDAO reportDAO) {
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
        reorderReport = new TestableReorderReport(reportDAO);
    }

    @Test
    public void testFetchData() {
        // Prepare mock data
        List<Object[]> mockData = new ArrayList<>();
        mockData.add(new Object[]{"code1", "Item1", 10, 5});
        when(reportDAO.getReorderItems()).thenReturn(mockData);

        // Fetch data
        ((TestableReorderReport) reorderReport).publicFetchData();

        // Verify fetchData calls getReorderItems
        verify(reportDAO).getReorderItems();
    }

    @Test
    public void testGenerateOutput() {
        // Prepare mock data
        List<Object[]> mockData = new ArrayList<>();
        mockData.add(new Object[]{"code1", "Item1", 10, 5});
        when(reportDAO.getReorderItems()).thenReturn(mockData);

        // Fetch data and generate output
        ((TestableReorderReport) reorderReport).publicFetchData();
        ((TestableReorderReport) reorderReport).publicGenerateOutput();

        // Since this test outputs to the console, we will just verify interactions
        verify(reportDAO).getReorderItems();
    }
}
