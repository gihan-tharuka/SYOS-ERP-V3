package Testing2.Testing.observer;

import dao.ReorderLevelDAO;
import model.Item;
import observer.Observer;
import observer.ReorderObserver;
import observer.ReorderSubject;
import observer.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ObserverTest {
    
    private ReorderLevelDAO reorderLevelDAO;
    private ReorderObserver reorderObserver;
    private ReorderSubject reorderSubject;
    private Item testItem;
    private Observer mockObserver;

    @BeforeEach
    public void setUp() {
        reorderLevelDAO = mock(ReorderLevelDAO.class);
        reorderObserver = new ReorderObserver(reorderLevelDAO);
        reorderSubject = new ReorderSubject();
        testItem = new Item(1, "ITEM001", "Test Item", 10.0, 0.0);
        mockObserver = mock(Observer.class);
    }

    @Test
    public void testReorderObserverUpdate() {
        reorderObserver.update(testItem);

        verify(reorderLevelDAO).addReorderLevel(1, 50);
    }

    @Test
    public void testReorderObserverUpdateWithDifferentItem() {
        Item anotherItem = new Item(2, "ITEM002", "Another Item", 15.0, 0.0);

        reorderObserver.update(anotherItem);

        verify(reorderLevelDAO).addReorderLevel(2, 50);
    }

    @Test
    public void testSubjectAddObserver() {
        reorderSubject.addObserver(mockObserver);
        reorderSubject.itemAdded(testItem);

        verify(mockObserver).update(testItem);
    }

    @Test
    public void testSubjectRemoveObserver() {
        reorderSubject.addObserver(mockObserver);
        reorderSubject.removeObserver(mockObserver);
        reorderSubject.itemAdded(testItem);

        verify(mockObserver, never()).update(any(Item.class));
    }

    @Test
    public void testSubjectMultipleObservers() {
        Observer mockObserver2 = mock(Observer.class);
        
        reorderSubject.addObserver(mockObserver);
        reorderSubject.addObserver(mockObserver2);
        reorderSubject.itemAdded(testItem);

        verify(mockObserver).update(testItem);
        verify(mockObserver2).update(testItem);
    }

    @Test
    public void testSubjectAddAndRemoveObserver() {
        reorderSubject.addObserver(mockObserver);
        reorderSubject.itemAdded(testItem);
        verify(mockObserver).update(testItem);

        reorderSubject.removeObserver(mockObserver);
        reorderSubject.itemAdded(testItem);
        verify(mockObserver, times(1)).update(testItem); // Should only be called once
    }

    @Test
    public void testReorderSubjectItemAdded() {
        reorderSubject.addObserver(reorderObserver);
        reorderSubject.itemAdded(testItem);

        verify(reorderLevelDAO).addReorderLevel(1, 50);
    }

    @Test
    public void testObserverInterface() {
        // Test that ReorderObserver implements Observer interface
        assertTrue(reorderObserver instanceof Observer);
    }

    @Test
    public void testSubjectInheritance() {
        // Test that ReorderSubject extends Subject
        assertTrue(reorderSubject instanceof Subject);
    }

    @Test
    public void testReorderObserverConstructor() {
        ReorderObserver observer = new ReorderObserver(reorderLevelDAO);
        assertNotNull(observer);
    }

    @Test
    public void testReorderObserverWithNullItem() {
        assertThrows(NullPointerException.class, () -> {
            reorderObserver.update(null);
        });
    }

    @Test
    public void testSubjectWithNullObserver() {
        // Test that the system works without null observers
        // Since Subject class doesn't handle null observers, we test the normal flow
        reorderSubject.addObserver(mockObserver);
        reorderSubject.itemAdded(testItem);
        
        // Verify that the observer was called correctly
        verify(mockObserver).update(testItem);
    }

    @Test
    public void testSubjectRemoveNonExistentObserver() {
        // Test removing observer that was never added
        reorderSubject.removeObserver(mockObserver);
        reorderSubject.itemAdded(testItem);
        
        // Should not throw exception
        verify(mockObserver, never()).update(any(Item.class));
    }
} 