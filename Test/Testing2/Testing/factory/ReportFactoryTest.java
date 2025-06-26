package Testing2.Testing.factory;

import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ReportDAO;
import dao.ShelfStockDAO;
import factory.ReportFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.BatchSelectionStrategy;
import template.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportFactoryTest {
    
    private ShelfStockDAO shelfStockDAO;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private ReportDAO reportDAO;
    private BatchSelectionStrategy strategy;

    @BeforeEach
    public void setUp() {
        shelfStockDAO = mock(ShelfStockDAO.class);
        mainStockDAO = mock(MainStockDAO.class);
        itemDAO = mock(ItemDAO.class);
        reportDAO = mock(ReportDAO.class);
        strategy = mock(BatchSelectionStrategy.class);
    }

    @Test
    public void testCreateReportDailySales() {
        ReportTemplate result = ReportFactory.createReport(
            ReportFactory.ReportType.DAILY_SALES, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );

        assertNotNull(result);
        assertTrue(result instanceof DailySalesReport);
    }

    @Test
    public void testCreateReportReshelve() {
        ReportTemplate result = ReportFactory.createReport(
            ReportFactory.ReportType.RESHELVE, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );

        assertNotNull(result);
        assertTrue(result instanceof ReshelveReport);
    }

    @Test
    public void testCreateReportReorder() {
        ReportTemplate result = ReportFactory.createReport(
            ReportFactory.ReportType.REORDER, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );

        assertNotNull(result);
        assertTrue(result instanceof ReorderReport);
    }

    @Test
    public void testCreateReportStock() {
        ReportTemplate result = ReportFactory.createReport(
            ReportFactory.ReportType.STOCK, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );

        assertNotNull(result);
        assertTrue(result instanceof StockReport);
    }

    @Test
    public void testCreateReportBill() {
        ReportTemplate result = ReportFactory.createReport(
            ReportFactory.ReportType.BILL, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );

        assertNotNull(result);
        assertTrue(result instanceof BillReport);
    }

    @Test
    public void testCreateReportAllTypes() {
        // Test all report types in one test for full coverage
        ReportTemplate dailySales = ReportFactory.createReport(
            ReportFactory.ReportType.DAILY_SALES, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );
        
        ReportTemplate reshelve = ReportFactory.createReport(
            ReportFactory.ReportType.RESHELVE, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );
        
        ReportTemplate reorder = ReportFactory.createReport(
            ReportFactory.ReportType.REORDER, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );
        
        ReportTemplate stock = ReportFactory.createReport(
            ReportFactory.ReportType.STOCK, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );
        
        ReportTemplate bill = ReportFactory.createReport(
            ReportFactory.ReportType.BILL, 
            shelfStockDAO, 
            mainStockDAO, 
            itemDAO, 
            reportDAO, 
            strategy
        );

        assertNotNull(dailySales);
        assertNotNull(reshelve);
        assertNotNull(reorder);
        assertNotNull(stock);
        assertNotNull(bill);
        
        assertTrue(dailySales instanceof DailySalesReport);
        assertTrue(reshelve instanceof ReshelveReport);
        assertTrue(reorder instanceof ReorderReport);
        assertTrue(stock instanceof StockReport);
        assertTrue(bill instanceof BillReport);
    }

    @Test
    public void testReportTypeEnumValues() {
        ReportFactory.ReportType[] types = ReportFactory.ReportType.values();
        assertEquals(5, types.length);
        
        boolean hasDailySales = false, hasReshelve = false, hasReorder = false, hasStock = false, hasBill = false;
        for (ReportFactory.ReportType type : types) {
            switch (type) {
                case DAILY_SALES:
                    hasDailySales = true;
                    break;
                case RESHELVE:
                    hasReshelve = true;
                    break;
                case REORDER:
                    hasReorder = true;
                    break;
                case STOCK:
                    hasStock = true;
                    break;
                case BILL:
                    hasBill = true;
                    break;
            }
        }
        
        assertTrue(hasDailySales);
        assertTrue(hasReshelve);
        assertTrue(hasReorder);
        assertTrue(hasStock);
        assertTrue(hasBill);
    }

    @Test
    public void testCreateReportWithNullParameters() {
        // Test that the factory handles null parameters gracefully
        ReportTemplate result = ReportFactory.createReport(
            ReportFactory.ReportType.DAILY_SALES, 
            null, 
            null, 
            null, 
            null, 
            null
        );

        assertNotNull(result);
        assertTrue(result instanceof DailySalesReport);
    }
} 