package Testing2.model;

import model.CashPayment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CashPaymentTest {

    @Test
    public void testCalculateChange() {
        CashPayment cashPayment = new CashPayment();
        cashPayment.setAmount(50.0);
        double cashTendered = 100.0;
        double expectedChange = 50.0;
        double actualChange = cashPayment.calculateChange(cashTendered);
        assertEquals(expectedChange, actualChange, "The calculated change should be correct");
    }

    @Test
    public void testCalculateChangeWithExactAmount() {
        CashPayment cashPayment = new CashPayment();
        cashPayment.setAmount(100.0);
        double cashTendered = 100.0;
        double expectedChange = 0.0;
        double actualChange = cashPayment.calculateChange(cashTendered);
        assertEquals(expectedChange, actualChange, "The calculated change should be zero when tendered amount equals the payment amount");
    }

    @Test
    public void testCalculateChangeWithInsufficientTender() {
        CashPayment cashPayment = new CashPayment();
        cashPayment.setAmount(100.0);
        double cashTendered = 50.0;
        double expectedChange = -50.0; // Negative change indicating insufficient tender
        double actualChange = cashPayment.calculateChange(cashTendered);
        assertEquals(expectedChange, actualChange, "The calculated change should be negative when tendered amount is less than the payment amount");
    }
}
