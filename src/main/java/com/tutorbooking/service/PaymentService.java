package com.tutorbooking.service;

import com.tutorbooking.model.Payment;
import com.tutorbooking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository = new PaymentRepository();

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByUserId(String userId) {
        return getAllPayments().stream()
                .filter(p -> p.getStudentId().equals(userId))
                .collect(Collectors.toList());
    }

    public void processPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public void updatePaymentStatus(String id, String status) {
        Payment payment = paymentRepository.findById(id);
        if (payment != null) {
            payment.setStatus(status);
            paymentRepository.update(id, payment);
        }
    }

    public boolean deletePayment(String id) {
        return paymentRepository.deleteById(id);
    }
}

