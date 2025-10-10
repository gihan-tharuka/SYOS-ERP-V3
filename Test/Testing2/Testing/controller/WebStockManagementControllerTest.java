package Testing2.Testing.controller;

import controller.WebStockManagementController;
import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ReorderLevelDAO;
import dao.WebStockDAO;
import model.BatchSelection;
import model.WebStock;
import view.WebStockManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class WebStockManagementControllerTest {
    private WebStockManagementView view;
    private WebStockDAO webStockDAO;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private ReorderLevelDAO reorderLevelDAO;
    private WebStockManagementController controller;

    @BeforeEach
    void setUp() {
        view = mock(WebStockManagementView.class);
        webStockDAO = mock(WebStockDAO.class);
        mainStockDAO = mock(MainStockDAO.class);
        itemDAO = mock(ItemDAO.class);
        reorderLevelDAO = mock(ReorderLevelDAO.class);
        controller = new WebStockManagementController(view, webStockDAO, mainStockDAO, itemDAO, reorderLevelDAO);
    }

    @Test
    void testShowWebStockManagementMenu_AddNew() {
        when(view.getUserChoice()).thenReturn(1);
        WebStockManagementController spyController = spy(controller);
        doNothing().when(spyController).addNewWebStock();
        doNothing().when(spyController).viewAllWebStocks();
        doNothing().when(spyController).handleReshelfStock();
        doNothing().when(spyController).editWebStock();
        doNothing().when(spyController).deleteWebStock();
        spyController.showWebStockManagementMenu();
        verify(spyController).addNewWebStock();
    }

    @Test
    void testAddNewWebStock_Success() {
        when(view.getItemCodeForNewWebStock()).thenReturn("itemCode");
        when(itemDAO.getItemIdByCode("itemCode")).thenReturn(1);
        WebStock webStock = mock(WebStock.class);
        when(view.getNewWebStockDetails(1)).thenReturn(webStock);
        when(webStockDAO.addWebStock(webStock)).thenReturn(true);
        controller.addNewWebStock();
        verify(webStockDAO).addWebStock(webStock);
        verify(view).showAddWebStockSuccess();
    }

    @Test
    void testAddNewWebStock_Failure() {
        when(view.getItemCodeForNewWebStock()).thenReturn("itemCode");
        when(itemDAO.getItemIdByCode("itemCode")).thenReturn(1);
        WebStock webStock = mock(WebStock.class);
        when(view.getNewWebStockDetails(1)).thenReturn(webStock);
        when(webStockDAO.addWebStock(webStock)).thenReturn(false);
        controller.addNewWebStock();
        verify(webStockDAO).addWebStock(webStock);
        verify(view).showAddWebStockFailure();
    }

    @Test
    void testAddNewWebStock_InvalidItemCode() {
        when(view.getItemCodeForNewWebStock()).thenReturn("itemCode");
        when(itemDAO.getItemIdByCode("itemCode")).thenReturn(-1);
        controller.addNewWebStock();
        verify(itemDAO).getItemIdByCode("itemCode");
        verify(view, never()).showAddWebStockSuccess();
    }

    @Test
    void testViewAllWebStocks() {
        Map<WebStock, String> webStocksWithItemCodes = new HashMap<>();
        when(webStockDAO.getAllWebStocksWithItemCodes()).thenReturn(webStocksWithItemCodes);
        controller.viewAllWebStocks();
        verify(view).displayAllWebStocks(webStocksWithItemCodes);
    }

    @Test
    void testHandleReshelfStock_Confirm() {
        List<WebStock> allWebStocks = Arrays.asList(mock(WebStock.class));
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        Map<Integer, String> itemNames = new HashMap<>();
        when(webStockDAO.getAllWebStocks()).thenReturn(allWebStocks);
        when(webStockDAO.getReshelvingInfo(allWebStocks, mainStockDAO)).thenReturn(reshelvingInfo);
        when(webStockDAO.getReshelfQuantities(allWebStocks)).thenReturn(reshelfQuantities);
        when(itemDAO.getItemNamesByItemIds(reshelfQuantities.keySet())).thenReturn(itemNames);
        doNothing().when(view).displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        when(view.confirmReshelfAll()).thenReturn(true);
        doNothing().when(webStockDAO).confirmReshelving(reshelvingInfo, reshelfQuantities, mainStockDAO, reorderLevelDAO);
        doNothing().when(view).showReshelfSuccess();
        controller.handleReshelfStock();
        verify(view).displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        verify(webStockDAO).confirmReshelving(reshelvingInfo, reshelfQuantities, mainStockDAO, reorderLevelDAO);
        verify(view).showReshelfSuccess();
    }

    @Test
    void testHandleReshelfStock_Cancel() {
        List<WebStock> allWebStocks = Arrays.asList(mock(WebStock.class));
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        Map<Integer, String> itemNames = new HashMap<>();
        when(webStockDAO.getAllWebStocks()).thenReturn(allWebStocks);
        when(webStockDAO.getReshelvingInfo(allWebStocks, mainStockDAO)).thenReturn(reshelvingInfo);
        when(webStockDAO.getReshelfQuantities(allWebStocks)).thenReturn(reshelfQuantities);
        when(itemDAO.getItemNamesByItemIds(reshelfQuantities.keySet())).thenReturn(itemNames);
        doNothing().when(view).displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        when(view.confirmReshelfAll()).thenReturn(false);
        controller.handleReshelfStock();
        verify(view).displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        verify(webStockDAO, never()).confirmReshelving(any(), any(), any(), any());
        verify(view, never()).showReshelfSuccess();
    }

    @Test
    void testDeleteWebStock_Confirm() {
        when(view.getItemIdForDelete()).thenReturn(1);
        when(view.confirmDelete(1)).thenReturn(true);
        controller.deleteWebStock();
        verify(webStockDAO).deleteWebStockByItemId(1);
        verify(view).showDeleteSuccess();
    }

    @Test
    void testDeleteWebStock_Cancel() {
        when(view.getItemIdForDelete()).thenReturn(1);
        when(view.confirmDelete(1)).thenReturn(false);
        controller.deleteWebStock();
        verify(webStockDAO, never()).deleteWebStockByItemId(anyInt());
        verify(view, never()).showDeleteSuccess();
    }

    @Test
    void testEditWebStock_Success() {
        when(view.getItemIdForEdit()).thenReturn(1);
        WebStock updatedWebStock = mock(WebStock.class);
        when(view.getUpdatedWebStockDetails(1)).thenReturn(updatedWebStock);
        controller.editWebStock();
        verify(webStockDAO).updateWebStock(updatedWebStock);
        verify(view).showEditSuccess();
    }

    @Test
    void testEditWebStock_Cancel() {
        when(view.getItemIdForEdit()).thenReturn(1);
        when(view.getUpdatedWebStockDetails(1)).thenReturn(null);
        controller.editWebStock();
        verify(webStockDAO, never()).updateWebStock(any());
        verify(view, never()).showEditSuccess();
    }
} 