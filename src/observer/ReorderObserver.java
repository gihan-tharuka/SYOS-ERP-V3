package observer;

import dao.ReorderLevelDAO;
import model.Item;

public class ReorderObserver implements Observer {
    private ReorderLevelDAO reorderLevelDAO;

    public ReorderObserver(ReorderLevelDAO reorderLevelDAO) {
        this.reorderLevelDAO = reorderLevelDAO;
    }

    @Override
    public void update(Item item) {
        reorderLevelDAO.addReorderLevel(item.getItemId());
        System.out.println("ReorderObserver: Reorder level updated for item: " + item.getItemId());
    }
}

