package com.tutorbooking.repository;

import com.tutorbooking.model.Payment;
import com.tutorbooking.util.FileHelper;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private static final String FILE_NAME = "payments.txt";

    public List<Payment> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<Payment> payments = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 7) {
                payments.add(new Payment(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]),
                        parts[4], parts[5], parts[6]));
            }
        }
        return payments;
    }

    public void save(Payment payment) {
        FileHelper.appendLine(FILE_NAME, payment.toString());
    }

    public boolean update(String id, Payment payment) {
        return FileHelper.updateRecordById(FILE_NAME, id, payment.toString());
    }

    public boolean deleteById(String id) {
        return FileHelper.deleteRecordById(FILE_NAME, id);
    }

    public Payment findById(String id) {
        return findAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

