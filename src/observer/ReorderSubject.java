package observer;

import model.Item;

public class ReorderSubject extends Subject {
    public void itemAdded(Item item) {
        notifyObservers(item);
    }
}
