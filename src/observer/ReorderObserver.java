package observer;

import dao.ReorderLevelDAO;
import model.Item;

public class ReorderObserver implements Observer {
    private ReorderLevelDAO reorderLevelDAO;
    private static final int DEFAULT_THRESHOLD_QUANTITY = 50;

    public ReorderObserver(ReorderLevelDAO reorderLevelDAO) {
        this.reorderLevelDAO = reorderLevelDAO;
    }

    @Override
    public void update(Item item) {
        reorderLevelDAO.addReorderLevel(item.getItemId(), DEFAULT_THRESHOLD_QUANTITY);
        System.out.println("ReorderObserver: Reorder level updated for item: " + item.getItemId());
    }
}

