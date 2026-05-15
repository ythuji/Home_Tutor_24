package com.tutorbooking.controller;

import com.tutorbooking.model.Tutor;
import com.tutorbooking.model.User;
import com.tutorbooking.service.TutorService;
import com.tutorbooking.service.ReviewService;
import com.tutorbooking.util.IDGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/tutors")
public class TutorController {
    private final TutorService tutorService = new TutorService();
    private final ReviewService reviewService = new ReviewService();

    @GetMapping("/list")
    public String listTutors(Model model) {
        var tutors = tutorService.getAllTutors();

        // Calculate ratings and review counts for each tutor
        Map<String, Double> tutorRatings = new HashMap<>();
        Map<String, Integer> tutorReviewCounts = new HashMap<>();

        for (Tutor tutor : tutors) {
            double avgRating = reviewService.getAverageRating(tutor.getId());
            int reviewCount = reviewService.getReviewCount(tutor.getId());

            tutorRatings.put(tutor.getId(), Math.round(avgRating * 10.0) / 10.0);
            tutorReviewCounts.put(tutor.getId(), reviewCount);
        }

        model.addAttribute("tutors", tutors);
        model.addAttribute("tutorRatings", tutorRatings);
        model.addAttribute("tutorReviewCounts", tutorReviewCounts);
        return "tutor/tutor-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"TUTOR".equals(user.getRole())) {
            return "redirect:/login";
        }
        return "tutor/tutor-form";
    }

    @PostMapping("/save")
    public String saveTutor(@RequestParam String name, @RequestParam String email,
                            @RequestParam String specialization, @RequestParam Double rate,
                            @RequestParam(required = false) String id) {
        if (id == null || id.isEmpty()) {
            String newId = IDGenerator.generate("T");
            Tutor tutor = new Tutor(newId, name, email, "PASS_PLACEHOLDER", specialization, rate);
            tutorService.addTutor(tutor);
        } else {
            // Fetch existing tutor to preserve all fields
            Tutor existingTutor = tutorService.getTutorById(id);
            if (existingTutor != null) {
                // Update only the fields from the form
                existingTutor.setName(name);
                existingTutor.setEmail(email);
                existingTutor.setSpecialization(specialization);
                existingTutor.setHourlyRate(rate);
                // Preserve phone, experience, and rating from existing record
                tutorService.updateTutor(id, existingTutor);
            }
        }
        return "redirect:/tutors/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        // Only tutors can edit, and only their own profile
        if (loggedUser == null || !"TUTOR".equals(loggedUser.getRole())) {
            return "redirect:/login";
        }

        // Check if the tutor is trying to edit their own profile
        if (!loggedUser.getId().equals(id)) {
            return "redirect:/tutors/list"; // Cannot edit other tutors
        }

        Tutor tutor = tutorService.getTutorById(id);
        model.addAttribute("tutor", tutor);
        return "tutor/tutor-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTutor(@PathVariable String id, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        // Only tutors can delete, and only their own profile
        if (loggedUser == null || !"TUTOR".equals(loggedUser.getRole())) {
            return "redirect:/login";
        }

        // Check if the tutor is trying to delete their own profile
        if (!loggedUser.getId().equals(id)) {
            return "redirect:/tutors/list"; // Cannot delete other tutors
        }

        tutorService.removeTutor(id);
        return "redirect:/logout"; // Logout after deleting own account
    }
}

