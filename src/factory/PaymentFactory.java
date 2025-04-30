package factory;

import model.CashPayment;
import model.Payment;

public class PaymentFactory {
    public enum PaymentType {
        CASH

    }

    public static Payment createPayment(PaymentType paymentType) {
        if (paymentType == null) {
            throw new IllegalArgumentException("Invalid payment type");
        }
        switch (paymentType) {
            case CASH:
                return new CashPayment();
            default:
                throw new IllegalArgumentException("Invalid payment type");
        }
    }
}
