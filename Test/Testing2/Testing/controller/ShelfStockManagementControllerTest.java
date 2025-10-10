package Testing2.Testing.controller;

import controller.ShelfStockManagementController;
import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ReorderLevelDAO;
import dao.ShelfStockDAO;
import model.BatchSelection;
import model.ShelfStock;
import view.ShelfStockManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class ShelfStockManagementControllerTest {
    private ShelfStockManagementView view;
    private ShelfStockDAO shelfStockDAO;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private ReorderLevelDAO reorderLevelDAO;
    private ShelfStockManagementController controller;

    @BeforeEach
    void setUp() {
        view = mock(ShelfStockManagementView.class);
        shelfStockDAO = mock(ShelfStockDAO.class);
        mainStockDAO = mock(MainStockDAO.class);
        itemDAO = mock(ItemDAO.class);
        reorderLevelDAO = mock(ReorderLevelDAO.class);
        controller = new ShelfStockManagementController(view, shelfStockDAO, mainStockDAO, itemDAO, reorderLevelDAO);
    }

    @Test
    void testShowShelfStockManagementMenu_AddNew() {
        when(view.getUserChoice()).thenReturn(1);
        ShelfStockManagementController spyController = spy(controller);
        doNothing().when(spyController).addNewShelfStock();
        doNothing().when(spyController).viewAllShelfStocks();
        doNothing().when(spyController).handleReshelfStock();
        doNothing().when(spyController).editShelfStock();
        doNothing().when(spyController).deleteShelfStock();
        spyController.showShelfStockManagementMenu();
        verify(spyController).addNewShelfStock();
    }

    @Test
    void testAddNewShelfStock_Success() {
        when(view.getItemCodeForNewShelfStock()).thenReturn("itemCode");
        when(itemDAO.getItemIdByCode("itemCode")).thenReturn(1);
        ShelfStock shelfStock = mock(ShelfStock.class);
        when(view.getNewShelfStockDetails(1)).thenReturn(shelfStock);
        when(shelfStockDAO.addShelfStock(shelfStock)).thenReturn(true);
        controller.addNewShelfStock();
        verify(shelfStockDAO).addShelfStock(shelfStock);
        verify(view).showAddShelfStockSuccess();
    }

    @Test
    void testAddNewShelfStock_Failure() {
        when(view.getItemCodeForNewShelfStock()).thenReturn("itemCode");
        when(itemDAO.getItemIdByCode("itemCode")).thenReturn(1);
        ShelfStock shelfStock = mock(ShelfStock.class);
        when(view.getNewShelfStockDetails(1)).thenReturn(shelfStock);
        when(shelfStockDAO.addShelfStock(shelfStock)).thenReturn(false);
        controller.addNewShelfStock();
        verify(shelfStockDAO).addShelfStock(shelfStock);
        verify(view).showAddShelfStockFailure();
    }

    @Test
    void testAddNewShelfStock_InvalidItemCode() {
        when(view.getItemCodeForNewShelfStock()).thenReturn("itemCode");
        when(itemDAO.getItemIdByCode("itemCode")).thenReturn(-1);
        controller.addNewShelfStock();
        verify(itemDAO).getItemIdByCode("itemCode");
        verify(view, never()).showAddShelfStockSuccess();
    }

    @Test
    void testViewAllShelfStocks() {
        Map<ShelfStock, String> shelfStocksWithItemCodes = new HashMap<>();
        when(shelfStockDAO.getAllShelfStocksWithItemCodes()).thenReturn(shelfStocksWithItemCodes);
        controller.viewAllShelfStocks();
        verify(view).displayAllShelfStocks(shelfStocksWithItemCodes);
    }

    @Test
    void testHandleReshelfStock_Confirm() {
        List<ShelfStock> allShelfStocks = Arrays.asList(mock(ShelfStock.class));
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        Map<Integer, String> itemNames = new HashMap<>();
        when(shelfStockDAO.getAllShelfStocks()).thenReturn(allShelfStocks);
        when(shelfStockDAO.getReshelvingInfo(allShelfStocks, mainStockDAO)).thenReturn(reshelvingInfo);
        when(shelfStockDAO.getReshelfQuantities(allShelfStocks)).thenReturn(reshelfQuantities);
        when(itemDAO.getItemNamesByItemIds(reshelfQuantities.keySet())).thenReturn(itemNames);
        doNothing().when(view).displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        when(view.confirmReshelfAll()).thenReturn(true);
        doNothing().when(shelfStockDAO).confirmReshelving(reshelvingInfo, reshelfQuantities, mainStockDAO, reorderLevelDAO);
        doNothing().when(view).showReshelfSuccess();
        controller.handleReshelfStock();
        verify(view).displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        verify(shelfStockDAO).confirmReshelving(reshelvingInfo, reshelfQuantities, mainStockDAO, reorderLevelDAO);
        verify(view).showReshelfSuccess();
    }

    @Test
    void testHandleReshelfStock_Cancel() {
        List<ShelfStock> allShelfStocks = Arrays.asList(mock(ShelfStock.class));
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        Map<Integer, String> itemNames = new HashMap<>();
        when(shelfStockDAO.getAllShelfStocks()).thenReturn(allShelfStocks);
        when(shelfStockDAO.getReshelvingInfo(allShelfStocks, mainStockDAO)).thenReturn(reshelvingInfo);
        when(shelfStockDAO.getReshelfQuantities(allShelfStocks)).thenReturn(reshelfQuantities);
        when(itemDAO.getItemNamesByItemIds(reshelfQuantities.keySet())).thenReturn(itemNames);
        doNothing().when(view).displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        when(view.confirmReshelfAll()).thenReturn(false);
        controller.handleReshelfStock();
        verify(view).displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        verify(shelfStockDAO, never()).confirmReshelving(any(), any(), any(), any());
        verify(view, never()).showReshelfSuccess();
    }

    @Test
    void testDeleteShelfStock_Confirm() {
        when(view.getItemIdForDelete()).thenReturn(1);
        when(view.confirmDelete(1)).thenReturn(true);
        controller.deleteShelfStock();
        verify(shelfStockDAO).deleteShelfStockByItemId(1);
        verify(view).showDeleteSuccess();
    }

    @Test
    void testDeleteShelfStock_Cancel() {
        when(view.getItemIdForDelete()).thenReturn(1);
        when(view.confirmDelete(1)).thenReturn(false);
        controller.deleteShelfStock();
        verify(shelfStockDAO, never()).deleteShelfStockByItemId(anyInt());
        verify(view, never()).showDeleteSuccess();
    }

    @Test
    void testEditShelfStock_Success() {
        when(view.getItemIdForEdit()).thenReturn(1);
        ShelfStock updatedShelfStock = mock(ShelfStock.class);
        when(view.getUpdatedShelfStockDetails(1)).thenReturn(updatedShelfStock);
        controller.editShelfStock();
        verify(shelfStockDAO).updateShelfStock(updatedShelfStock);
        verify(view).showEditSuccess();
    }

    @Test
    void testEditShelfStock_Cancel() {
        when(view.getItemIdForEdit()).thenReturn(1);
        when(view.getUpdatedShelfStockDetails(1)).thenReturn(null);
        controller.editShelfStock();
        verify(shelfStockDAO, never()).updateShelfStock(any());
        verify(view, never()).showEditSuccess();
    }
} 