package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethods;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    PaymentRepository paymentRepository;

    List<Payment> payments;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        List<Product> products = new ArrayList<>();
        payments = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        Map<String, String> bankTransferDetails = new HashMap<>();
        bankTransferDetails.put("bankName", "Mandiri");
        bankTransferDetails.put("referenceCode", "123456789ABCDEFGHI");

        Order order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
        Order order2 = new Order("13652996-013a-4c07-b546-54eb1396d79b",
                products, 1708570000L, "Bambang Sudrajat");
        Payment payment1 = new Payment("13652556-012a-4c07-b546-54eb1396d79b", order1, "VOUCHER", Map.of("voucherCode", "ESHOP1234ABC5678"));
        Payment payment2 = new Payment("13652556-012a-4c07-b546-54eb1396d79b", order2, "BANK_TRANSFER", bankTransferDetails);
        payments.add(payment1);
        payments.add(payment2);
    }

    @Test
    void testSavePayment() {
        Payment payment = payments.get(1);
        Payment result = paymentRepository.save(payment);
        Payment findResult = paymentRepository.findById(result.getId());

        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
        assertEquals(payment.getOrder(), findResult.getOrder());
        assertEquals(payment.getStatus(), findResult.getStatus());
        assertEquals(payment.getMethod(), findResult.getMethod());
    }

    @Test
    void testUpdatePayment() {
        Payment payment = payments.get(1);
        paymentRepository.save(payment);
        Map<String, String> newPaymentDetail = new HashMap<>();
        newPaymentDetail.put("bankName", "Mandiri");
        newPaymentDetail.put("referenceCode", "123456789ABCDEFGHI");
        Payment newPayment = new Payment("13652556-012a-4c07-b546-54eb1396d79b", payment.getOrder(), "BANK_TRANSFER", newPaymentDetail);
        Payment result = paymentRepository.save(newPayment);
        Payment findResult = paymentRepository.findById(result.getId());

        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
        assertEquals(payment.getOrder(), findResult.getOrder());
        assertEquals(PaymentMethods.BANK_TRANSFER.getValue(), findResult.getStatus());
        assertEquals(payment.getOrder(), findResult.getOrder());
    }

    @Test
    void testFindPaymentByIdAndFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findPayment = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payments.get(1).getId(), findPayment.getId());
        assertEquals(payments.get(1).getPaymentData(), findPayment.getPaymentData());
        assertEquals(payments.get(1).getOrder(), findPayment.getOrder());
        assertEquals(payments.get(1).getStatus(), findPayment.getStatus());
        assertEquals(payments.get(1).getMethod(), findPayment.getMethod());
    }

    @Test
    void testFindPaymentByIdAndNotFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findPayment = paymentRepository.findById("abcde");
        assertNull(findPayment);
    }

    @Test
    void testFindAllPaymentsIfEmpty() {
        Iterator<Product> paymentIterator = paymentRepository.findAll();
        assertFalse(paymentIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOnePayment() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Iterator<Payment> paymentIterator = paymentRepository.findAll();
        assertTrue(paymentIterator.hasNext());
        Payment firstPayment = paymentIterator.next();
        assertEquals(payments.get(1).getId(), savedPayment.getId());
        assertEquals(payments.get(1).getPaymentData(), savedPayment.getPaymentData());
        assertEquals(payments.get(1).getOrder(), savedPayment.getOrder());
        assertEquals(payments.get(1).getStatus(), savedPayment.getStatus());
        assertEquals(payments.get(1).getMethod(), savedPayment.getMethod());
        Payment secondPayment = paymentIterator.next();
        assertEquals(payments.get(2).getId(), secondPayment.getId());
        assertEquals(payments.get(2).getPaymentData(), secondPayment.getPaymentData());
        assertEquals(payments.get(2).getOrder(), secondPayment.getOrder());
        assertEquals(payments.get(2).getStatus(), secondPayment.getStatus());
        assertEquals(payments.get(2).getMethod(), secondPayment.getMethod());
        assertFalse(paymentIterator.hasNext());
    }
}