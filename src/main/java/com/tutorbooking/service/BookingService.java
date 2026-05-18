package com.tutorbooking.service;

import com.tutorbooking.model.Booking;
import com.tutorbooking.model.Payment;
import com.tutorbooking.repository.BookingRepository;
import com.tutorbooking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository = new BookingRepository();
    private final PaymentRepository paymentRepository = new PaymentRepository();

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByStudent(String studentId) {
        return getAllBookings().stream()
                .filter(b -> b.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookingsByTutor(String tutorId) {
        return getAllBookings().stream()
                .filter(b -> b.getTutorId().equals(tutorId))
                .collect(Collectors.toList());
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id);
    }

    public void createBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public void updateBookingStatus(String id, String status) {
        Booking booking = bookingRepository.findById(id);
        if (booking != null) {
            booking.setStatus(status);
            bookingRepository.update(id, booking);
        }
    }

    public void updateBooking(String id, String subject, String dateTime) {
        Booking booking = bookingRepository.findById(id);
        if (booking != null) {
            booking.setSubject(subject);
            bookingRepository.update(id, new Booking(id, booking.getStudentId(), booking.getTutorId(), subject,
                    dateTime, booking.getStatus()));
        }
    }

    public boolean canEditBooking(String id) {
        Booking booking = getBookingById(id);
        if (booking == null || !"PENDING".equals(booking.getStatus())) {
            return false;
        }
        // Check if booking is more than 24 hours away
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime bookingDateTime = LocalDateTime.parse(booking.getDateTime(), formatter);
            LocalDateTime now = LocalDateTime.now();
            return bookingDateTime.isAfter(now.plusHours(24));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasPayment(String bookingId) {
        try {
            List<Payment> payments = paymentRepository.findAll();
            return payments.stream()
                    .anyMatch(p -> p.getBookingId().equals(bookingId) && "PAID".equals(p.getStatus()));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Return a set of booking IDs from a tutor's bookings that have a PAID payment.
     */
    public Set<String> getPaidBookingIdsForTutor(String tutorId) {
        Set<String> paid = new HashSet<>();
        List<Booking> tutorBookings = getBookingsByTutor(tutorId);
        for (Booking b : tutorBookings) {
            if (hasPayment(b.getId())) {
                paid.add(b.getId());
            }
        }
        return paid;
    }

    public void deleteBooking(String id) {
        // Also delete any associated payments
        try {
            List<Payment> payments = paymentRepository.findAll();
            payments.stream()
                    .filter(p -> p.getBookingId().equals(id))
                    .forEach(p -> paymentRepository.deleteById(p.getId()));
        } catch (Exception e) {
            // Continue with booking deletion even if payment deletion fails
        }
        bookingRepository.deleteById(id);
    }
}
