package com.tutorbooking.repository;

import com.tutorbooking.model.Booking;
import com.tutorbooking.util.FileHelper;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    private static final String FILE_NAME = "bookings.txt";

    public List<Booking> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<Booking> bookings = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 6) {
                bookings.add(new Booking(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
            }
        }
        return bookings;
    }

    public void save(Booking booking) {
        FileHelper.appendLine(FILE_NAME, booking.toString());
    }

    public boolean update(String id, Booking booking) {
        return FileHelper.updateRecordById(FILE_NAME, id, booking.toString());
    }

    public boolean deleteById(String id) {
        return FileHelper.deleteRecordById(FILE_NAME, id);
    }

    public Booking findById(String id) {
        return findAll().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
