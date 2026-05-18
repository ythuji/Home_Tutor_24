package com.tutorbooking.controller;

import com.tutorbooking.model.User;
import com.tutorbooking.service.BookingService;
import com.tutorbooking.service.TutorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final BookingService bookingService = new BookingService();
    private final TutorService tutorService = new TutorService();

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);

        if ("STUDENT".equals(user.getRole())) {
            model.addAttribute("bookings", bookingService.getBookingsByStudent(user.getId()));
        } else {
            model.addAttribute("bookings", bookingService.getBookingsByTutor(user.getId()));
            // Add paid booking ids so the template can display payment status badges
            model.addAttribute("paidBookings", bookingService.getPaidBookingIdsForTutor(user.getId()));
        }

        model.addAttribute("tutorCount", tutorService.getAllTutors().size());

        return "dashboard";
    }
}

