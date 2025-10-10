package model;

public class WebStock extends Stock {
    private int webCapacity;
    private int currentQuantity;

    public int getWebCapacity() {
        return webCapacity;
    }

    public void setWebCapacity(int webCapacity) {
        this.webCapacity = webCapacity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }
}
