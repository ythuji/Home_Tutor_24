package com.tutorbooking.controller;

import com.tutorbooking.model.Booking;
import com.tutorbooking.model.User;
import com.tutorbooking.service.BookingService;
import com.tutorbooking.service.TutorService;
import com.tutorbooking.util.IDGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService = new BookingService();
    private final TutorService tutorService = new TutorService();

    @GetMapping("/list")
    public String listBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        if ("STUDENT".equals(user.getRole())) {
            model.addAttribute("bookings", bookingService.getBookingsByStudent(user.getId()));
        } else {
            model.addAttribute("bookings", bookingService.getBookingsByTutor(user.getId()));
        }
        return "booking/booking-list";
    }

    @GetMapping("/create")
    public String showBookingForm(@RequestParam(required = false) String tutorId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"STUDENT".equals(user.getRole()))
            return "redirect:/login";

        model.addAttribute("tutors", tutorService.getAllTutors());
        if (tutorId != null && !tutorId.isEmpty()) {
            model.addAttribute("selectedTutorId", tutorId);
        }
        return "booking/booking-form";
    }

    @PostMapping("/save")
    public String saveBooking(@RequestParam String tutorId, @RequestParam String subject,
                              @RequestParam String date, @RequestParam String time,
                              HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        String id = IDGenerator.generate("B");
        Booking booking = new Booking(id, user.getId(), tutorId, subject, date + " " + time, "PENDING");
        bookingService.createBooking(booking);
        return "redirect:/bookings/list";
    }

    @GetMapping("/status/{id}/{status}")
    public String updateStatus(@PathVariable String id, @PathVariable String status) {
        bookingService.updateBookingStatus(id, status);
        return "redirect:/bookings/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        Booking booking = bookingService.getBookingById(id);
        if (booking != null && (booking.getStudentId().equals(user.getId()) || booking.getTutorId().equals(user.getId()))) {
            bookingService.deleteBooking(id);
        }
        return "redirect:/bookings/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"STUDENT".equals(user.getRole())) {
            return "redirect:/login";
        }

        Booking booking = bookingService.getBookingById(id);
        if (booking == null || !booking.getStudentId().equals(user.getId())) {
            return "redirect:/bookings/list";
        }

        if (!bookingService.canEditBooking(id)) {
            return "redirect:/bookings/list";
        }

        model.addAttribute("booking", booking);
        model.addAttribute("tutors", tutorService.getAllTutors());
        return "booking/booking-edit";
    }

    @PostMapping("/update/{id}")
    public String updateBooking(@PathVariable String id, @RequestParam String subject,
                                @RequestParam String date, @RequestParam String time,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        Booking booking = bookingService.getBookingById(id);
        if (booking == null || !booking.getStudentId().equals(user.getId())) {
            return "redirect:/bookings/list";
        }

        if (!bookingService.canEditBooking(id)) {
            return "redirect:/bookings/list";
        }

        bookingService.updateBooking(id, subject, date + " " + time);
        return "redirect:/bookings/list";
    }
}

