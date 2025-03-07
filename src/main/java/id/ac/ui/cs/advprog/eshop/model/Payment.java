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

    }

    private void validateVoucherPayment() {

    }

    private void validateBankTransferPayment() {

    }

    public void setStatus(String status) {

    }
}
