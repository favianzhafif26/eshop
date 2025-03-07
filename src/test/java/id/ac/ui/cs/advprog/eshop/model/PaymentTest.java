package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private List<Product> products;
    private Order order;
    private Map<String, String> voucherPayment;
    private Map<String, String> bankTransferPayment;

    @BeforeEach
    void setUp() {
        this.products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(1);
        this.products.add(product1);
        this.products.add(product2);

        this.order = new Order("13652556-012a-4c07-b546-54eb1396d79b", this.products, 1708560000L, "Safira Sudrajat");

        this.voucherPayment = new HashMap<>();
        this.voucherPayment.put("voucherCode", "ESHOP123456789AB");
        this.bankTransferPayment = new HashMap<>();
        this.bankTransferPayment.put("bankName", "Mandiri");
        this.bankTransferPayment.put("referenceCode", "123456789ABCDEFGHI");
    }

    @Test
    void testCreatePaymentEmptyOrder() {
        this.order = null;
        assertThrows(IllegalArgumentException.class,
                    () -> new Payment("13652556-012a-4c07-b546-54eb1396d79b", "VOUCHER", this.voucherPayment, this.order));
    }

    @Test
    void testCreatePaymentDefaultStatus() {
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b", "VOUCHER", this.voucherPayment, this.order);

        assertEquals("13652556-012a-4c07-b546-54eb1396d79b", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals(this.voucherPayment, payment.getPaymentData());
        assertEquals(this.order, payment.getOrder());
        assertEquals("PENDING", payment.getStatus());
        assertEquals("WAITING_PAYMENT", payment.getOrder().getStatus());
    }

    @Test
    void testCreatePaymentSuccessStatus() {
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b", "VOUCHER", this.voucherPayment, this.order);
        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", payment.getOrder().getStatus());
    }

    @Test
    void testSetPaymentInvalidStatus() {
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b", "VOUCHER", this.voucherPayment, this.order);
        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("Meow"));
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b", "VOUCHER", this.voucherPayment, this.order);
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
        assertEquals("REJECTED", payment.getOrder().getStatus());
    }
}
