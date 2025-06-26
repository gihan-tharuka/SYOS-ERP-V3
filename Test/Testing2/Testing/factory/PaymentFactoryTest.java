package Testing2.Testing.factory;

import factory.PaymentFactory;
import model.CashPayment;
import model.Payment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentFactoryTest {

    @Test
    public void testCreatePaymentCash() {
        Payment result = PaymentFactory.createPayment(PaymentFactory.PaymentType.CASH);

        assertNotNull(result);
        assertTrue(result instanceof CashPayment);
    }

    @Test
    public void testCreatePaymentNullType() {
        assertThrows(IllegalArgumentException.class, () -> {
            PaymentFactory.createPayment(null);
        });
    }

    @Test
    public void testPaymentTypeEnumValues() {
        PaymentFactory.PaymentType[] types = PaymentFactory.PaymentType.values();
        assertEquals(1, types.length);
        
        boolean hasCash = false;
        for (PaymentFactory.PaymentType type : types) {
            switch (type) {
                case CASH:
                    hasCash = true;
                    break;
            }
        }
        
        assertTrue(hasCash);
    }

    @Test
    public void testCreatePaymentAllTypes() {
        // Test all payment types in one test for full coverage
        Payment cashPayment = PaymentFactory.createPayment(PaymentFactory.PaymentType.CASH);

        assertNotNull(cashPayment);
        assertTrue(cashPayment instanceof CashPayment);
    }

    @Test
    public void testPaymentTypeEnumName() {
        PaymentFactory.PaymentType cashType = PaymentFactory.PaymentType.CASH;
        assertEquals("CASH", cashType.name());
    }

    @Test
    public void testPaymentTypeEnumOrdinal() {
        PaymentFactory.PaymentType cashType = PaymentFactory.PaymentType.CASH;
        assertEquals(0, cashType.ordinal());
    }
} 