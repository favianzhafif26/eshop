package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    String id;
    Order order;
    String method;
    Map<String, String> paymentData;
    String status;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        if (order == null || paymentData == null) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.order = order;
        this.paymentData = paymentData;

        if (!PaymentMethods.contains(method)) {
            throw new IllegalArgumentException();
        }
        this.method = method;

        if (method.equals(PaymentMethods.VOUCHER.getValue())) {
            validateVoucherPayment();
        }
        else if (method.equals(PaymentMethods.BANK_TRANSFER.getValue())) {
            validateBankTransferPayment();
        }
    }

    private void validateVoucherPayment() {
        String voucher = paymentData.get("voucherCode");
        if ((voucher != null && voucher.length() == 16) && voucher.startsWith("ESHOP") && countDigit(voucher) == 8) {
            this.status = PaymentStatus.SUCCESS.getValue();
        }
        else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    private void validateBankTransferPayment() {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        if (bankName != null && !bankName.isEmpty() && referenceCode != null && !referenceCode.isEmpty()) {
            this.status = PaymentStatus.SUCCESS.getValue();
        }
        else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    public void setStatus(String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }
}
