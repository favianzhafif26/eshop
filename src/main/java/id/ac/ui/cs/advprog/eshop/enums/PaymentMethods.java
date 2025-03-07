package id.ac.ui.cs.advprog.eshop.enums;

import lombok.Getter;

@Getter
public enum PaymentMethods {
    BANK_TRANSFER("BANK_TRANSFER"),
    VOUCHER("VOUCHER");

    private final String value;
    PaymentMethods(String value){
        this.value = value;
    }

    public static boolean contains(String param) {
        for (PaymentMethods status : PaymentMethods.values()) {
            if (status.name().equals(param)) {
                return true;
            }
        }
        return false;
    }
}
