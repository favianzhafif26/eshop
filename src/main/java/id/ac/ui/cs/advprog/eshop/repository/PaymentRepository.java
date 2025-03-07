package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class PaymentRepository {
    private List<Payment> payments = new ArrayList<>();

    public Payment save(Payment payment) {
        int i = 0;
        for (Payment p : payments) {
            if (p.getId().equals(payment.getId())) {
                payments.remove(i);
                payment.setId(p.getId());
                payments.add(i, payment);
                return payment;
            }
            i += 1;
        }
        payments.add(i, payment);
        return payment;
    }

    public Payment findById(String id) {
        for (Payment p : payments) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Iterator<Payment> findAll() {
        return payments.iterator();
    }
}