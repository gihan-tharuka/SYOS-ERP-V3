package Testing2.Testing.view;

import view.WebStockManagementView;
import model.WebStock;
import model.MainStock;
import model.BatchSelection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class WebStockManagementViewTest {
    private WebStockManagementView view;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        System.setIn(new ByteArrayInputStream("".getBytes()));
        view = new WebStockManagementView();
    }

    @Test
    void testDisplayWebStockManagementMenu() {
        view.displayWebStockManagementMenu();
        String output = outputStream.toString();
        assertTrue(output.contains("Web Stock Management Menu:"));
    }

    @Test
    void testGetUserChoice() {
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new WebStockManagementView();
        int result = view.getUserChoice();
        assertEquals(2, result);
    }

    @Test
    void testClearInput() {
        String input = "test\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new WebStockManagementView();
        view.clearInput();
        assertTrue(true);
    }

    @Test
    void testGetItemCodeForNewWebStock() {
        String input = "ITEM001";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new WebStockManagementView();
        String result = view.getItemCodeForNewWebStock();
        assertEquals("ITEM001", result);
        assertTrue(outputStream.toString().contains("Enter item code:"));
    }

    @Test
    void testGetNewWebStockDetails() {
        String input = "100\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new WebStockManagementView();
        int itemId = 1;
        WebStock result = view.getNewWebStockDetails(itemId);
        assertNotNull(result);
        assertEquals(itemId, result.getItemId());
        assertEquals(100, result.getWebCapacity());
        assertEquals(0, result.getCurrentQuantity());
    }

    @Test
    void testDisplayReshelvingInfo() {
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        Map<Integer, String> itemNames = new HashMap<>();
        BatchSelection mockSelection = mock(BatchSelection.class);
        MainStock mockBatch = mock(MainStock.class);
        when(mockBatch.getBatchCode()).thenReturn("BATCH001");
        when(mockBatch.getExpiryDate()).thenReturn(new Date());
        when(mockSelection.getBatch()).thenReturn(mockBatch);
        when(mockSelection.getReshelfQuantity()).thenReturn(50);
        reshelvingInfo.put(1, Arrays.asList(mockSelection));
        reshelfQuantities.put(1, 100);
        itemNames.put(1, "Test Item");
        view.displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        String output = outputStream.toString();
        assertTrue(output.contains("Reshelving Info:"));
        assertTrue(output.contains("Item ID: 1"));
        assertTrue(output.contains("Test Item"));
        assertTrue(output.contains("BATCH001"));
        verify(mockSelection, times(2)).getBatch();
        verify(mockSelection).getReshelfQuantity();
    }

    @Test
    void testConfirmReshelfAll() {
        String input = "yes\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new WebStockManagementView();
        boolean result = view.confirmReshelfAll();
        assertTrue(result);
    }

    @Test
    void testShowAddWebStockSuccess() {
        view.showAddWebStockSuccess();
        assertTrue(outputStream.toString().contains("Web stock added successfully!"));
    }

    @Test
    void testShowAddWebStockFailure() {
        view.showAddWebStockFailure();
        assertTrue(outputStream.toString().contains("Failed to add web stock."));
    }

    @Test
    void testShowReshelfSuccess() {
        view.showReshelfSuccess();
        assertTrue(outputStream.toString().contains("Web stock reshelved successfully!"));
    }

    @Test
    void testDisplayAllWebStocks() {
        Map<WebStock, String> webStocksWithItemCodes = new HashMap<>();
        WebStock mockWebStock = mock(WebStock.class);
        when(mockWebStock.getStockId()).thenReturn(1);
        when(mockWebStock.getWebCapacity()).thenReturn(100);
        when(mockWebStock.getCurrentQuantity()).thenReturn(50);
        webStocksWithItemCodes.put(mockWebStock, "ITEM001");
        view.displayAllWebStocks(webStocksWithItemCodes);
        String output = outputStream.toString();
        assertTrue(output.contains("All Web Stocks:"));
        assertTrue(output.contains("Web ID"));
        assertTrue(output.contains("Item Code"));
        assertTrue(output.contains("Web Capacity"));
        assertTrue(output.contains("Current Quantity"));
        assertTrue(output.contains("ITEM001"));
        verify(mockWebStock).getStockId();
        verify(mockWebStock).getWebCapacity();
        verify(mockWebStock).getCurrentQuantity();
    }

    @Test
    void testGetItemIdForDelete() {
        String input = "5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new WebStockManagementView();
        int result = view.getItemIdForDelete();
        assertEquals(5, result);
    }

    @Test
    void testConfirmDelete() {
        String input = "yes\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new WebStockManagementView();
        boolean result = view.confirmDelete(5);
        assertTrue(result);
    }

    @Test
    void testShowDeleteSuccess() {
        view.showDeleteSuccess();
        assertTrue(outputStream.toString().contains("Web stock deleted successfully!"));
    }

    @Test
    void testGetItemIdForEdit() {
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new WebStockManagementView();
        int result = view.getItemIdForEdit();
        assertEquals(3, result);
    }

    @Test
    void testGetUpdatedWebStockDetails() {
        String input = "150\n75\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new WebStockManagementView();
        int itemId = 2;
        WebStock result = view.getUpdatedWebStockDetails(itemId);
        assertNotNull(result);
        assertEquals(itemId, result.getItemId());
        assertEquals(150, result.getWebCapacity());
        assertEquals(75, result.getCurrentQuantity());
    }

    @Test
    void testShowEditSuccess() {
        view.showEditSuccess();
        assertTrue(outputStream.toString().contains("Web stock updated successfully!"));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
} 