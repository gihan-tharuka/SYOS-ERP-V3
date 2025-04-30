package Testing2.factory;

import factory.PaymentFactory;
import factory.PaymentFactory.PaymentType;
import model.CashPayment;
import model.Payment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentFactoryTest {

    @Test
    public void testCreateCashPayment() {
        Payment payment = PaymentFactory.createPayment(PaymentType.CASH);
        assertNotNull(payment, "The payment should not be null");
        assertTrue(payment instanceof CashPayment, "The payment should be an instance of CashPayment");
    }

    @Test
    public void testInvalidPaymentType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            PaymentFactory.createPayment(null); // Example of invalid payment type
        });

        String expectedMessage = "Invalid payment type";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Exception message should match expected message");
    }
}
