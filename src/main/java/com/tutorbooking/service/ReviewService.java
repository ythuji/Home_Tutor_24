package com.tutorbooking.service;

package com.tutorbooking.service;

import com.tutorbooking.model.Review;
import com.tutorbooking.model.Payment;
import com.tutorbooking.model.Booking;
import com.tutorbooking.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository = new ReviewRepository();
    private final PaymentService paymentService = new PaymentService();
    private final BookingService bookingService = new BookingService();

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByTutor(String tutorId) {
        return getAllReviews().stream()
                .filter(r -> r.getTutorId().equals(tutorId))
                .collect(Collectors.toList());
    }

    public List<Review> getReviewsByStudent(String studentId) {
        return getAllReviews().stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public Review getReviewById(String id) {
        return getAllReviews().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public double getAverageRating(String tutorId) {
        List<Review> reviews = getReviewsByTutor(tutorId);
        if (reviews.isEmpty())
            return 0.0;
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public int getReviewCount(String tutorId) {
        return getReviewsByTutor(tutorId).size();
    }

    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    public void updateReview(String id, int rating, String comment) {
        Review review = getReviewById(id);
        if (review != null) {
            reviewRepository.deleteById(id);
            Review updatedReview = new Review(id, review.getStudentId(), review.getTutorId(), rating, comment);
            reviewRepository.save(updatedReview);
        }
    }

    public void removeReview(String id) {
        reviewRepository.deleteById(id);
    }

    public boolean deleteReview(String id) {
        return reviewRepository.deleteById(id);
    }

    // Validation: Check if student has a PAID booking with this tutor
    public boolean hasEligibleBooking(String studentId, String tutorId) {
        // Get all paid payments for this student
        List<Payment> paidPayments = paymentService.getAllPayments().stream()
                .filter(p -> p.getStudentId().equals(studentId) && "PAID".equals(p.getStatus()))
                .collect(Collectors.toList());

        // Check if any paid payment corresponds to a booking with this tutor
        for (Payment payment : paidPayments) {
            Booking booking = bookingService.getBookingById(payment.getBookingId());
            if (booking != null && booking.getTutorId().equals(tutorId)) {
                return true;
            }
        }
        return false;
    }

    // Check if student has already reviewed this tutor
    public boolean hasAlreadyReviewed(String studentId, String tutorId) {
        return getAllReviews().stream()
                .anyMatch(r -> r.getStudentId().equals(studentId) && r.getTutorId().equals(tutorId));
    }

    // Check if student is eligible to review (has paid booking AND hasn't already
    // reviewed)
    public boolean isEligibleToReview(String studentId, String tutorId) {
        return hasEligibleBooking(studentId, tutorId) && !hasAlreadyReviewed(studentId, tutorId);
    }
}
