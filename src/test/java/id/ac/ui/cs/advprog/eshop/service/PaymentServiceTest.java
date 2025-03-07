package id.ac.ui.cs.advprog.eshop.service;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethods;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @InjectMocks
    PaymentServiceImpl paymentService;
    @Mock
    PaymentRepository paymentRepository;
    Order order1;
    Order order2;
    List<Product> products;
    List<Payment> payments;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        products = List.of(new Product(), new Product());
        order1 = new Order("23912828-ajaj", products,  1708570000L, "Safira Sudrajat");
        order2 = new Order("23222808-ajaj", products,  1708590000L, "Bambang Sudrajat");
        Map<String, String> bankTransferDetails = new HashMap<>();
        bankTransferDetails.put("bankName", "Mandiri");
        bankTransferDetails.put("referenceCode", "123456789ABCDEFGHI");
        payments = new ArrayList<>();

        Payment payment1 = new Payment("13652556-012a-4c07-b546-54eb1396d79b", order1, "VOUCHER", Map.of("voucherCode", "ESHOP1234ABC5678"));
        payments.add(payment1);
        Payment payment2 = new Payment("13652556-012a-4c07-b546-54eb1396d79b", order2, "BANK_TRANSFER", bankTransferDetails);
        payments.add(payment2);
        Payment payment3 = new Payment("13652556-012a-4c07-b546-54eb1396d79b", order1, "VOUCHER", Map.of("voucherCode", "ESHOP1234"));
        payments.add(payment3);
    }

    @Test
    void testAddPaymentVoucherSuccess() {
        Payment payment = payments.get(0);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(payment.getOrder(), "VOUCHER", payment.getPaymentData());
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), result.getOrder().getStatus());
    }

    @Test
    void testAddPaymentVoucherFailure() {
        Payment payment = payments.get(2);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(payment.getOrder(), "VOUCHER",payment.getPaymentData());
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), result.getOrder().getStatus());
    }

    @Test
    void testAddPaymentBankTransferSuccess() {
        Payment payment = payments.get(1);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(payment.getOrder(), "BANK_TRANSFER",payment.getPaymentData());
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), result.getOrder().getStatus());
    }

    @Test
    void testAddPaymentBankTransferWithNoBankName() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "123456789ABCDEFGHI");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order1, "BANK_TRANSFER",paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), result.getOrder().getStatus());
    }

    @Test
    void testAddPaymentBankTransferWithNoReferenceCode() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Mandiri");
        paymentData.put("referenceCode", "");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order1, "BANK_TRANSFER",paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), result.getOrder().getStatus());
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = payments.get(0);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        payment = paymentService.setStatus(payment, "SUCCESS");
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), payment.getOrder().getStatus());
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("a5e93216-127c-43df-b7f1-89b720e496bb", order1, "VOUCHER", Map.of("voucherCode", "ESHOP1234ABC5678"));

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        payment = paymentService.setStatus(payment, "REJECTED");
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
    }

    @Test
    void testSetInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("a5e93216-127c-43df-b7f1-89b720e496bb", order1, "VOUCHER", Map.of("voucherCode", "ESHOP1234ABC5678"));
            payment = paymentService.setStatus(payment, "KELAR");
        });
    }

    @Test
    void testGetPayment() {
        Payment payment = payments.get(1);
        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetAllPayments() {
        when(paymentRepository.findAll()).thenReturn(payments.iterator());

        Iterator<Payment> result = paymentService.getAllPayments();

        List<Payment> resultList = new ArrayList<>();
        result.forEachRemaining(resultList::add);

        assertEquals(payments.size(), resultList.size());
        assertEquals(payments.get(0).getId(), resultList.get(0).getId());
        assertEquals(payments.get(1).getId(), resultList.get(1).getId());

        verify(paymentRepository, times(1)).findAll();
    }
}