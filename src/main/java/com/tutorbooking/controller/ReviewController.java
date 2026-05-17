package com.tutorbooking.controller;

import com.tutorbooking.model.Review;
import com.tutorbooking.model.User;
import com.tutorbooking.model.Tutor;
import com.tutorbooking.service.ReviewService;
import com.tutorbooking.service.TutorService;
import com.tutorbooking.util.IDGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService = new ReviewService();
    private final TutorService tutorService = new TutorService();

    // View all reviews (for admin/tutor rating purposes)
    @GetMapping("/list")
    public String viewReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "review/review-list";
    }

    // View student's own reviews
    @GetMapping("/my-reviews")
    public String viewMyReviews(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        model.addAttribute("reviews", reviewService.getReviewsByStudent(user.getId()));
        return "review/review-list";
    }

    // View reviews for a specific tutor
    @GetMapping("/tutor/{tutorId}")
    public String viewTutorReviews(@PathVariable String tutorId, Model model) {
        Tutor tutor = tutorService.getTutorById(tutorId);
        if (tutor == null)
            return "redirect:/tutors/list";

        model.addAttribute("tutor", tutor);
        model.addAttribute("reviews", reviewService.getReviewsByTutor(tutorId));
        return "review/tutor-reviews";
    }

    // Show form to add new review
    @GetMapping("/add/{tutorId}")
    public String showReviewForm(@PathVariable String tutorId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"STUDENT".equals(user.getRole()))
            return "redirect:/login";

        Tutor tutor = tutorService.getTutorById(tutorId);
        if (tutor == null)
            return "redirect:/tutors/list";

        // Check if student is eligible to review
        boolean isEligible = reviewService.isEligibleToReview(user.getId(), tutorId);
        String errorMessage = null;

        if (!reviewService.hasEligibleBooking(user.getId(), tutorId)) {
            errorMessage = "You can only review tutors you have booked and paid for.";
        } else if (reviewService.hasAlreadyReviewed(user.getId(), tutorId)) {
            errorMessage = "You have already reviewed this tutor.";
        }

        model.addAttribute("tutor", tutor);
        model.addAttribute("isEligible", isEligible);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "review/review-form";
    }

    // Save new review
    @PostMapping("/save")
    public String saveReview(@RequestParam String tutorId, @RequestParam int rating,
                             @RequestParam String comment, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        // Validate that student is eligible to review
        if (!reviewService.isEligibleToReview(user.getId(), tutorId)) {
            return "redirect:/reviews/add/" + tutorId;
        }

        String id = IDGenerator.generate("R");
        Review review = new Review(id, user.getId(), tutorId, rating, comment);
        reviewService.addReview(review);
        return "redirect:/reviews/tutor/" + tutorId;
    }

    // Show edit form
    @GetMapping("/edit/{reviewId}")
    public String showEditForm(@PathVariable String reviewId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        Review review = reviewService.getReviewById(reviewId);
        if (review == null || !review.getStudentId().equals(user.getId())) {
            return "redirect:/reviews/my-reviews";
        }

        Tutor tutor = tutorService.getTutorById(review.getTutorId());
        model.addAttribute("review", review);
        model.addAttribute("tutor", tutor);
        return "review/edit-review";
    }

    // Update review
    @PostMapping("/update/{reviewId}")
    public String updateReview(@PathVariable String reviewId, @RequestParam int rating,
                               @RequestParam String comment, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        Review review = reviewService.getReviewById(reviewId);
        if (review == null || !review.getStudentId().equals(user.getId())) {
            return "redirect:/reviews/my-reviews";
        }

        reviewService.updateReview(reviewId, rating, comment);
        return "redirect:/reviews/my-reviews";
    }

    // Delete review
    @GetMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable String reviewId, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        Review review = reviewService.getReviewById(reviewId);
        if (review == null || !review.getStudentId().equals(user.getId())) {
            return "redirect:/reviews/my-reviews";
        }

        reviewService.deleteReview(reviewId);
        return "redirect:/reviews/my-reviews";
    }
}

