package Testing2.Testing.controller;

import controller.CashierMenuController;
import controller.MainStockManagementController;
import controller.ShelfStockManagementController;
import controller.SalesManagementController;
import controller.ReportManagementController;
import view.CashierMenuView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CashierMenuControllerTest {
    private CashierMenuView view;
    private MainStockManagementController mainStockManagementController;
    private ShelfStockManagementController shelfStockManagementController;
    private SalesManagementController salesManagementController;
    private ReportManagementController reportManagementController;
    private CashierMenuController controller;

    @BeforeEach
    void setUp() {
        view = mock(CashierMenuView.class);
        mainStockManagementController = mock(MainStockManagementController.class);
        shelfStockManagementController = mock(ShelfStockManagementController.class);
        salesManagementController = mock(SalesManagementController.class);
        reportManagementController = mock(ReportManagementController.class);
        controller = new CashierMenuController(view, mainStockManagementController, shelfStockManagementController, salesManagementController, reportManagementController);
    }

    @Test
    void testHandleMenu_MainStock() {
        when(view.getUserChoice()).thenReturn(1, 5); // 1: main stock, 5: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        controller.handleMenu();
        verify(mainStockManagementController).showMainStockManagementMenu();
    }

    @Test
    void testHandleMenu_ShelfStock() {
        when(view.getUserChoice()).thenReturn(2, 5); // 2: shelf stock, 5: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        controller.handleMenu();
        verify(shelfStockManagementController).showShelfStockManagementMenu();
    }

    @Test
    void testHandleMenu_Sales() {
        when(view.getUserChoice()).thenReturn(3, 5); // 3: sales, 5: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        controller.handleMenu();
        verify(salesManagementController).handleSalesManagement(1);
    }

    @Test
    void testHandleMenu_Report() {
        when(view.getUserChoice()).thenReturn(4, 5); // 4: report, 5: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        controller.handleMenu();
        verify(reportManagementController).showReportManagementMenu();
    }

    @Test
    void testHandleMenu_InvalidChoice() {
        when(view.getUserChoice()).thenReturn(99, 5); // 99: invalid, 5: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        controller.handleMenu();
        verify(mainStockManagementController, never()).showMainStockManagementMenu();
        verify(shelfStockManagementController, never()).showShelfStockManagementMenu();
        verify(salesManagementController, never()).handleSalesManagement(anyInt());
        verify(reportManagementController, never()).showReportManagementMenu();
    }
} 