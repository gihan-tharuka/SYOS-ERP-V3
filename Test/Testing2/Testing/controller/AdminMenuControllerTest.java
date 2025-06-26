package Testing2.Testing.controller;

import controller.AdminMenuController;
import controller.UserManagementController;
import controller.ItemManagementController;
import controller.MainStockManagementController;
import controller.ShelfStockManagementController;
import controller.SalesManagementController;
import controller.ReportManagementController;
import view.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdminMenuControllerTest {
    private AdminMenuView view;
    private UserManagementView userManagementView;
    private ItemManagementView itemManagementView;
    private MainStockManagementView mainStockManagementView;
    private ShelfStockManagementView shelfStockManagementView;
    private SalesManagementView salesManagementView;
    private UserManagementController userManagementController;
    private ItemManagementController itemManagementController;
    private MainStockManagementController mainStockManagementController;
    private ShelfStockManagementController shelfStockManagementController;
    private SalesManagementController salesManagementController;
    private ReportManagementController reportManagementController;
    private AdminMenuController controller;

    @BeforeEach
    void setUp() {
        view = mock(AdminMenuView.class);
        userManagementView = mock(UserManagementView.class);
        itemManagementView = mock(ItemManagementView.class);
        mainStockManagementView = mock(MainStockManagementView.class);
        shelfStockManagementView = mock(ShelfStockManagementView.class);
        salesManagementView = mock(SalesManagementView.class);
        userManagementController = mock(UserManagementController.class);
        itemManagementController = mock(ItemManagementController.class);
        mainStockManagementController = mock(MainStockManagementController.class);
        shelfStockManagementController = mock(ShelfStockManagementController.class);
        salesManagementController = mock(SalesManagementController.class);
        reportManagementController = mock(ReportManagementController.class);
        controller = new AdminMenuController(
            view, userManagementView, itemManagementView, mainStockManagementView, shelfStockManagementView,
            salesManagementView, userManagementController, itemManagementController, mainStockManagementController,
            shelfStockManagementController, salesManagementController, reportManagementController
        );
    }

    @Test
    void testHandleMenu_AdminManagement() {
        when(view.getUserChoice()).thenReturn(1, 9); // 1: admin management, 9: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        doNothing().when(userManagementView).displayAdminManagementMenu();
        when(userManagementView.getUserChoice()).thenReturn(5); // exit admin management
        doNothing().when(userManagementView).clearInput();
        doNothing().when(userManagementController).handleUserManagement(anyString(), anyInt());
        controller.handleMenu();
        verify(userManagementView).displayAdminManagementMenu();
        verify(userManagementController).handleUserManagement(eq("admin"), anyInt());
    }

    @Test
    void testHandleMenu_CashierManagement() {
        when(view.getUserChoice()).thenReturn(2, 9); // 2: cashier management, 9: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        doNothing().when(userManagementView).displayCashierManagementMenu();
        when(userManagementView.getUserChoice()).thenReturn(5); // exit cashier management
        doNothing().when(userManagementView).clearInput();
        doNothing().when(userManagementController).handleUserManagement(anyString(), anyInt());
        controller.handleMenu();
        verify(userManagementView).displayCashierManagementMenu();
        verify(userManagementController).handleUserManagement(eq("cashier"), anyInt());
    }

    @Test
    void testHandleMenu_SupplierManagement() {
        when(view.getUserChoice()).thenReturn(3, 9); // 3: supplier management, 9: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        doNothing().when(userManagementView).displaySupplierManagementMenu();
        when(userManagementView.getUserChoice()).thenReturn(5); // exit supplier management
        doNothing().when(userManagementView).clearInput();
        doNothing().when(userManagementController).handleUserManagement(anyString(), anyInt());
        controller.handleMenu();
        verify(userManagementView).displaySupplierManagementMenu();
        verify(userManagementController).handleUserManagement(eq("supplier"), anyInt());
    }

    @Test
    void testHandleMenu_ItemManagement() {
        when(view.getUserChoice()).thenReturn(4, 9); // 4: item management, 9: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        doNothing().when(itemManagementView).displayItemManagementMenu();
        when(itemManagementView.getUserChoice()).thenReturn(5); // exit item management
        doNothing().when(itemManagementView).clearInput();
        doNothing().when(itemManagementController).handleItemManagement(anyInt());
        controller.handleMenu();
        verify(itemManagementView).displayItemManagementMenu();
        verify(itemManagementController).handleItemManagement(anyInt());
    }

    @Test
    void testHandleMenu_MainStockManagement() {
        when(view.getUserChoice()).thenReturn(5, 9); // 5: main stock management, 9: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        doNothing().when(mainStockManagementController).showMainStockManagementMenu();
        controller.handleMenu();
        verify(mainStockManagementController).showMainStockManagementMenu();
    }

    @Test
    void testHandleMenu_ShelfStockManagement() {
        when(view.getUserChoice()).thenReturn(6, 9); // 6: shelf stock management, 9: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        doNothing().when(shelfStockManagementView).displayShelfStockManagementMenu();
        when(shelfStockManagementView.getUserChoice()).thenReturn(3); // exit shelf stock management
        doNothing().when(shelfStockManagementView).clearInput();
        doNothing().when(shelfStockManagementController).showShelfStockManagementMenu();
        controller.handleMenu();
        verify(shelfStockManagementView).displayShelfStockManagementMenu();
        verify(shelfStockManagementController).showShelfStockManagementMenu();
    }

    @Test
    void testHandleMenu_SalesManagement() {
        when(view.getUserChoice()).thenReturn(7, 9); // 7: sales management, 9: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        doNothing().when(salesManagementView).displaySalesManagementMenu();
        when(salesManagementView.getUserChoice()).thenReturn(3); // exit sales management
        doNothing().when(salesManagementView).clearInput();
        doNothing().when(salesManagementController).handleSalesManagement(anyInt());
        controller.handleMenu();
        verify(salesManagementView).displaySalesManagementMenu();
        verify(salesManagementController).handleSalesManagement(anyInt());
    }

    @Test
    void testHandleMenu_ReportManagement() {
        when(view.getUserChoice()).thenReturn(8, 9); // 8: report management, 9: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        doNothing().when(reportManagementController).showReportManagementMenu();
        controller.handleMenu();
        verify(reportManagementController).showReportManagementMenu();
    }

    @Test
    void testHandleMenu_InvalidChoice() {
        when(view.getUserChoice()).thenReturn(99, 9); // 99: invalid, 9: exit
        doNothing().when(view).displayMenu();
        doNothing().when(view).clearInput();
        controller.handleMenu();
        verify(userManagementController, never()).handleUserManagement(anyString(), anyInt());
        verify(itemManagementController, never()).handleItemManagement(anyInt());
        verify(mainStockManagementController, never()).showMainStockManagementMenu();
        verify(shelfStockManagementController, never()).showShelfStockManagementMenu();
        verify(salesManagementController, never()).handleSalesManagement(anyInt());
        verify(reportManagementController, never()).showReportManagementMenu();
    }
} 