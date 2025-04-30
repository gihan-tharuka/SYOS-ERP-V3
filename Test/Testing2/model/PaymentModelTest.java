package Testing2.model;

import model.CashPayment;
import model.Payment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentModelTest {

    @Test
    public void testSetAndGetAmount() {
        Payment payment = new CashPayment();
        double amount = 100.0;
        payment.setAmount(amount);
        assertEquals(amount, payment.getAmount(), "The amount should be set and retrieved correctly");
    }

    @Test
    public void testCalculateChange() {
        Payment payment = new CashPayment();
        payment.setAmount(50.0);
        double cashTendered = 100.0;
        double expectedChange = 50.0;
        assertEquals(expectedChange, payment.calculateChange(cashTendered),
                "The calculated change should be correct");
    }
}
