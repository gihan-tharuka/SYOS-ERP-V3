package model;

public class ReorderLevel {
    private int reorderLevelId;
    private int itemId;
    private int thresholdQuantity;
    private int totalStock;

    public ReorderLevel(int reorderLevelId, int itemId, int thresholdQuantity, int totalStock) {
        this.reorderLevelId = reorderLevelId;
        this.itemId = itemId;
        this.thresholdQuantity = thresholdQuantity;
        this.totalStock = totalStock;
    }

    public int getReorderLevelId() {
        return reorderLevelId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getThresholdQuantity() {
        return thresholdQuantity;
    }

    public int getTotalStock() {
        return totalStock;
    }
}

