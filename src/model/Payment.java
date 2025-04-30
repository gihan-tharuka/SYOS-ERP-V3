package model;

public abstract class Payment {
    protected double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public abstract double calculateChange(double cashTendered);
}

