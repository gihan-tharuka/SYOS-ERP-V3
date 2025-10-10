package Testing2.Testing.controller;

import controller.MainStockManagementController;
import dao.ItemDAO;
import dao.MainStockDAO;
import dao.UserDAO;
import factory.StockFactory;
import model.Item;
import model.MainStock;
import view.MainStockManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

public class MainStockManagementControllerTest {
    private MainStockManagementView view;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private UserDAO userDAO;
    private MainStockManagementController controller;

    @BeforeEach
    void setUp() {
        view = mock(MainStockManagementView.class);
        mainStockDAO = mock(MainStockDAO.class);
        itemDAO = mock(ItemDAO.class);
        userDAO = mock(UserDAO.class);
        controller = new MainStockManagementController(view, mainStockDAO, itemDAO, userDAO);
    }

    @Test
    void testShowMainStockManagementMenu_AddNew() {
        when(view.getUserChoice()).thenReturn(1);
        doNothing().when(view).displayMainStockManagementMenu();
        MainStockManagementController spyController = spy(controller);
        doNothing().when(spyController).addNewMainStock();
        doNothing().when(spyController).viewAllMainStocks();
        doNothing().when(spyController).editMainStock();
        doNothing().when(spyController).deleteMainStock();
        spyController.showMainStockManagementMenu();
        verify(spyController).addNewMainStock();
    }

    @Test
    void testShowMainStockManagementMenu_ViewAll() {
        when(view.getUserChoice()).thenReturn(2);
        doNothing().when(view).displayMainStockManagementMenu();
        MainStockManagementController spyController = spy(controller);
        doNothing().when(spyController).addNewMainStock();
        doNothing().when(spyController).viewAllMainStocks();
        doNothing().when(spyController).editMainStock();
        doNothing().when(spyController).deleteMainStock();
        spyController.showMainStockManagementMenu();
        verify(spyController).viewAllMainStocks();
    }

    @Test
    void testShowMainStockManagementMenu_Edit() {
        when(view.getUserChoice()).thenReturn(3);
        doNothing().when(view).displayMainStockManagementMenu();
        MainStockManagementController spyController = spy(controller);
        doNothing().when(spyController).addNewMainStock();
        doNothing().when(spyController).viewAllMainStocks();
        doNothing().when(spyController).editMainStock();
        doNothing().when(spyController).deleteMainStock();
        spyController.showMainStockManagementMenu();
        verify(spyController).editMainStock();
    }

    @Test
    void testShowMainStockManagementMenu_Delete() {
        when(view.getUserChoice()).thenReturn(4);
        doNothing().when(view).displayMainStockManagementMenu();
        MainStockManagementController spyController = spy(controller);
        doNothing().when(spyController).addNewMainStock();
        doNothing().when(spyController).viewAllMainStocks();
        doNothing().when(spyController).editMainStock();
        doNothing().when(spyController).deleteMainStock();
        spyController.showMainStockManagementMenu();
        verify(spyController).deleteMainStock();
    }

    @Test
    void testAddNewMainStock_Success() {
        MainStock mainStock = mock(MainStock.class);
        Item item = mock(Item.class);
        try (MockedStatic<StockFactory> stockFactoryMock = mockStatic(StockFactory.class)) {
            stockFactoryMock.when(() -> StockFactory.createStock(StockFactory.StockType.MAIN)).thenReturn(mainStock);
            when(view.getItemCodeFromUser()).thenReturn("itemCode");
            when(itemDAO.getItemByCode("itemCode")).thenReturn(item);
            when(item.getItemId()).thenReturn(1);
            when(view.getSupplierUsernameFromUser()).thenReturn("supplier");
            when(userDAO.getSupplierIdByUsername("supplier")).thenReturn(2);
            when(view.getNewMainStockDetails(mainStock)).thenReturn(mainStock);
            when(mainStock.getItemId()).thenReturn(1);
            when(mainStock.getBatchCode()).thenReturn("batch");
            when(mainStockDAO.doesMainStockExist(1, "batch")).thenReturn(false);

            controller.addNewMainStock();

            verify(mainStock).setItemId(1);
            verify(mainStock).setSupplierId(2);
            verify(mainStockDAO).addMainStock(mainStock);
            verify(view).showAddMainStockSuccess();
        }
    }

    @Test
    void testAddNewMainStock_ItemNotFound() {
        MainStock mainStock = mock(MainStock.class);
        try (MockedStatic<StockFactory> stockFactoryMock = mockStatic(StockFactory.class)) {
            stockFactoryMock.when(() -> StockFactory.createStock(StockFactory.StockType.MAIN)).thenReturn(mainStock);
            when(view.getItemCodeFromUser()).thenReturn("itemCode");
            when(itemDAO.getItemByCode("itemCode")).thenReturn(null);

            controller.addNewMainStock();

            verify(itemDAO).getItemByCode("itemCode");
            verify(mainStock, never()).setItemId(anyInt());
        }
    }

    @Test
    void testAddNewMainStock_SupplierNotFound() {
        MainStock mainStock = mock(MainStock.class);
        Item item = mock(Item.class);
        try (MockedStatic<StockFactory> stockFactoryMock = mockStatic(StockFactory.class)) {
            stockFactoryMock.when(() -> StockFactory.createStock(StockFactory.StockType.MAIN)).thenReturn(mainStock);
            when(view.getItemCodeFromUser()).thenReturn("itemCode");
            when(itemDAO.getItemByCode("itemCode")).thenReturn(item);
            when(item.getItemId()).thenReturn(1);
            when(view.getSupplierUsernameFromUser()).thenReturn("supplier");
            when(userDAO.getSupplierIdByUsername("supplier")).thenReturn(null);

            controller.addNewMainStock();

            verify(mainStock).setItemId(1);
            verify(userDAO).getSupplierIdByUsername("supplier");
            verify(mainStock, never()).setSupplierId(anyInt());
        }
    }

    @Test
    void testAddNewMainStock_AlreadyExists() {
        MainStock mainStock = mock(MainStock.class);
        Item item = mock(Item.class);
        try (MockedStatic<StockFactory> stockFactoryMock = mockStatic(StockFactory.class)) {
            stockFactoryMock.when(() -> StockFactory.createStock(StockFactory.StockType.MAIN)).thenReturn(mainStock);
            when(view.getItemCodeFromUser()).thenReturn("itemCode");
            when(itemDAO.getItemByCode("itemCode")).thenReturn(item);
            when(item.getItemId()).thenReturn(1);
            when(view.getSupplierUsernameFromUser()).thenReturn("supplier");
            when(userDAO.getSupplierIdByUsername("supplier")).thenReturn(2);
            when(view.getNewMainStockDetails(mainStock)).thenReturn(mainStock);
            when(mainStock.getItemId()).thenReturn(1);
            when(mainStock.getBatchCode()).thenReturn("batch");
            when(mainStockDAO.doesMainStockExist(1, "batch")).thenReturn(true);

            controller.addNewMainStock();

            verify(mainStockDAO, never()).addMainStock(mainStock);
            verify(view, never()).showAddMainStockSuccess();
        }
    }

    @Test
    void testViewAllMainStocks() {
        List<MainStock> mainStocks = Collections.singletonList(mock(MainStock.class));
        when(mainStockDAO.getAllMainStocks()).thenReturn(mainStocks);

        controller.viewAllMainStocks();

        verify(mainStockDAO).getAllMainStocks();
        verify(view).displayAllMainStocks(mainStocks, itemDAO, userDAO);
    }

    @Test
    void testEditMainStock_FoundAndEdited() {
        when(view.getUserChoice()).thenReturn(1);
        MainStock mainStock = mock(MainStock.class);
        when(mainStockDAO.getMainStockById(1)).thenReturn(mainStock);
        when(mainStock.getQuantity()).thenReturn(10);
        when(view.getNewMainStockDetails(mainStock)).thenReturn(mainStock);
        when(mainStock.getItemId()).thenReturn(1);
        when(mainStock.getQuantity()).thenReturn(15);

        controller.editMainStock();

        verify(mainStockDAO).editMainStock(mainStock);
        verify(mainStockDAO).adjustTotalStock(eq(1), anyInt());
        verify(view).showEditMainStockSuccess();
    }

    @Test
    void testEditMainStock_NotFound() {
        when(view.getUserChoice()).thenReturn(1);
        when(mainStockDAO.getMainStockById(1)).thenReturn(null);

        controller.editMainStock();

        verify(mainStockDAO).getMainStockById(1);
        verify(view, never()).showEditMainStockSuccess();
    }

    @Test
    void testDeleteMainStock() {
        when(view.getUserChoice()).thenReturn(1);

        controller.deleteMainStock();

        verify(mainStockDAO).deleteMainStock(1);
        verify(view).showDeleteMainStockSuccess();
    }
} 