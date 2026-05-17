package com.tutorbooking.service;

import com.tutorbooking.model.Admin;
import com.tutorbooking.model.Booking;
import com.tutorbooking.model.Payment;
import com.tutorbooking.model.Review;
import com.tutorbooking.model.Tutor;
import com.tutorbooking.model.User;
import com.tutorbooking.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository = new AdminRepository();
    private final UserService userService = new UserService();
    private final TutorService tutorService = new TutorService();
    private final BookingService bookingService = new BookingService();
    private final PaymentService paymentService = new PaymentService();
    private final ReviewService reviewService = new ReviewService();

    public Admin authenticateAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Admin getAdminById(String id) {
        return adminRepository.findById(id);
    }

    public void createAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public boolean updateAdmin(String id, Admin admin) {
        return adminRepository.update(id, admin);
    }

    public boolean deleteAdmin(String id) {
        return adminRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(userService.getAllUsers());
        users.addAll(tutorService.getAllTutors());
        return users;
    }

    public boolean deleteUser(String id) {
        return userService.deleteUser(id);
    }

    public List<Tutor> getAllTutors() {
        return tutorService.getAllTutors();
    }

    public boolean deleteTutor(String id) {
        return tutorService.deleteTutor(id);
    }

    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    public boolean deleteBooking(String id) {
        bookingService.deleteBooking(id);
        return true;
    }

    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    public boolean deletePayment(String id) {
        return paymentService.deletePayment(id);
    }

    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    public boolean deleteReview(String id) {
        return reviewService.deleteReview(id);
    }

    public int getTotalUsers() {
        return getAllUsers().size();
    }

    public int getTotalTutors() {
        return getAllTutors().size();
    }

    public int getTotalBookings() {
        return getAllBookings().size();
    }

    public int getTotalReviews() {
        return getAllReviews().size();
    }

    public double getTotalRevenue() {
        return getAllPayments().stream()
                .filter(p -> "PAID".equals(p.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public double getAverageRating() {
        return getAllReviews().stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public long getCompletedBookings() {
        return getAllBookings().stream()
                .filter(b -> "COMPLETED".equalsIgnoreCase(b.getStatus()))
                .count();
    }

    public long getPendingBookings() {
        return getAllBookings().stream()
                .filter(b -> "PENDING".equalsIgnoreCase(b.getStatus()))
                .count();
    }

    public List<Booking> getBookingsByStudent(String studentId) {
        return bookingService.getBookingsByStudent(studentId);
    }

    public List<Booking> getBookingsByTutor(String tutorId) {
        return bookingService.getBookingsByTutor(tutorId);
    }

    public Booking getBookingById(String id) {
        return bookingService.getBookingById(id);
    }

    public void createBooking(Booking booking) {
        bookingService.createBooking(booking);
    }

    public void updateBookingStatus(String id, String status) {
        bookingService.updateBookingStatus(id, status);
    }

    public void updateBooking(String id, String subject, String dateTime) {
        bookingService.updateBooking(id, subject, dateTime);
    }

    public boolean canEditBooking(String id) {
        return bookingService.canEditBooking(id);
    }

    public boolean hasPayment(String bookingId) {
        return bookingService.hasPayment(bookingId);
    }
}