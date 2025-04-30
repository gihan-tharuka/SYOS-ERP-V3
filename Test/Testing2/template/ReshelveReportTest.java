package Testing2.template;

import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ShelfStockDAO;
import model.BatchSelection;
import model.MainStock;
import model.ShelfStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.BatchSelectionStrategy;
import template.ReshelveReport;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ReshelveReportTest {

    private ReshelveReport reshelveReport;
    private ShelfStockDAO shelfStockDAO;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private BatchSelectionStrategy strategy;

    // Subclass to expose the protected methods
    private class TestableReshelveReport extends ReshelveReport {
        public TestableReshelveReport(ShelfStockDAO shelfStockDAO, MainStockDAO mainStockDAO, ItemDAO itemDAO, BatchSelectionStrategy strategy) {
            super(shelfStockDAO, mainStockDAO, itemDAO, strategy);
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
        shelfStockDAO = mock(ShelfStockDAO.class);
        mainStockDAO = mock(MainStockDAO.class);
        itemDAO = mock(ItemDAO.class);
        strategy = mock(BatchSelectionStrategy.class);
        reshelveReport = new TestableReshelveReport(shelfStockDAO, mainStockDAO, itemDAO, strategy);
    }

    @Test
    public void testFetchData() {
        // Prepare mock shelf stocks
        List<ShelfStock> shelfStocks = new ArrayList<>();
        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(1);
        shelfStock.setShelfCapacity(100);
        shelfStock.setCurrentQuantity(50);
        shelfStocks.add(shelfStock);
        when(shelfStockDAO.getAllShelfStocks()).thenReturn(shelfStocks);

        // Prepare mock main stocks
        List<MainStock> mainStocks = new ArrayList<>();
        MainStock mainStock = new MainStock();
        mainStock.setBatchCode("batch1");
        mainStock.setExpiryDate(new java.sql.Date(System.currentTimeMillis()));
        mainStocks.add(mainStock);
        when(mainStockDAO.getMainStocksByItemId(1)).thenReturn(mainStocks);

        // Prepare mock batch selections
        List<BatchSelection> batchSelections = new ArrayList<>();
        BatchSelection batchSelection = new BatchSelection(mainStock, 50);
        batchSelections.add(batchSelection);
        when(strategy.selectBatches(mainStocks, 50)).thenReturn(batchSelections);

        // Prepare mock item name
        when(itemDAO.getItemNameById(1)).thenReturn("Item1");

        // Fetch data
        ((TestableReshelveReport) reshelveReport).publicFetchData();

        // Verify interactions
        verify(shelfStockDAO).getAllShelfStocks();
        verify(mainStockDAO).getMainStocksByItemId(1);
        verify(strategy).selectBatches(mainStocks, 50);
        verify(itemDAO).getItemNameById(1);
    }

    @Test
    public void testGenerateOutput() {
        // Prepare mock shelf stocks
        List<ShelfStock> shelfStocks = new ArrayList<>();
        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(1);
        shelfStock.setShelfCapacity(100);
        shelfStock.setCurrentQuantity(50);
        shelfStocks.add(shelfStock);
        when(shelfStockDAO.getAllShelfStocks()).thenReturn(shelfStocks);

        // Prepare mock main stocks
        List<MainStock> mainStocks = new ArrayList<>();
        MainStock mainStock = new MainStock();
        mainStock.setBatchCode("batch1");
        mainStock.setExpiryDate(new java.sql.Date(System.currentTimeMillis()));
        mainStocks.add(mainStock);
        when(mainStockDAO.getMainStocksByItemId(1)).thenReturn(mainStocks);

        // Prepare mock batch selections
        List<BatchSelection> batchSelections = new ArrayList<>();
        BatchSelection batchSelection = new BatchSelection(mainStock, 50);
        batchSelections.add(batchSelection);
        when(strategy.selectBatches(mainStocks, 50)).thenReturn(batchSelections);

        // Prepare mock item name
        when(itemDAO.getItemNameById(1)).thenReturn("Item1");

        // Fetch data and generate output
        ((TestableReshelveReport) reshelveReport).publicFetchData();
        ((TestableReshelveReport) reshelveReport).publicGenerateOutput();

        // Verify interactions
        verify(shelfStockDAO).getAllShelfStocks();
        verify(mainStockDAO).getMainStocksByItemId(1);
        verify(strategy).selectBatches(mainStocks, 50);
        verify(itemDAO).getItemNameById(1);
    }
}


