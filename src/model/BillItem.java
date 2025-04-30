package model;

public class BillItem {
    private int billItemId;
    private int billId;
    private int itemId;
    private String itemName;
    private int quantity;
    private double itemTotalPrice;

    public BillItem() {}

    public BillItem(int billItemId, int billId, int itemId, String itemName, int quantity, double itemTotalPrice) {
        this.billItemId = billItemId;
        this.billId = billId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemTotalPrice = itemTotalPrice;
    }

    public int getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(int billItemId) {
        this.billItemId = billItemId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(double itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }
}

