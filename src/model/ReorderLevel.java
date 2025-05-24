package model;

public class ReorderLevel {
    private int reorderLevelId;
    private int itemId;
    private int thresholdQuantity;
    private String itemCode;
    private String itemName;
    private int totalStock;

    public ReorderLevel() {
    }

    public ReorderLevel(int reorderLevelId, int itemId, int thresholdQuantity, String itemCode, String itemName, int totalStock) {
        this.reorderLevelId = reorderLevelId;
        this.itemId = itemId;
        this.thresholdQuantity = thresholdQuantity;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.totalStock = totalStock;
    }

    public int getReorderLevelId() {
        return reorderLevelId;
    }

    public void setReorderLevelId(int reorderLevelId) {
        this.reorderLevelId = reorderLevelId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getThresholdQuantity() {
        return thresholdQuantity;
    }

    public void setThresholdQuantity(int thresholdQuantity) {
        this.thresholdQuantity = thresholdQuantity;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }
}

