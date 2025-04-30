package model;

public class Item {
    private int itemId;
    private String itemCode;
    private String itemName;
    private double price;
    private Double discount;

    public Item(int itemId, String itemCode, String itemName, double price, Double discount) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.price = price;
        this.discount = discount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount){
        this.discount = discount;
    }
}

