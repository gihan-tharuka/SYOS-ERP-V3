package model;

public class CashPayment extends Payment {
    @Override
    public double calculateChange(double cashTendered) {
        return cashTendered - amount;
    }
}

