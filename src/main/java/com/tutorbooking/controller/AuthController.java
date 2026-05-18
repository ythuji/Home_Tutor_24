package com.tutorbooking.controller;

import com.tutorbooking.model.User;
import com.tutorbooking.model.Student;
import com.tutorbooking.model.Tutor;
import com.tutorbooking.service.UserService;
import com.tutorbooking.service.TutorService;
import com.tutorbooking.util.IDGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final UserService userService = new UserService();
    private final TutorService tutorService = new TutorService();

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password,
                               HttpSession session, Model model) {
        // First try to authenticate as user/student
        User user = userService.authenticate(email, password);
        if (user != null) {
            session.setAttribute("loggedUser", user);
            return "redirect:/dashboard";
        }

        // Then try to authenticate as tutor
        Tutor tutor = tutorService.authenticate(email, password);
        if (tutor != null) {
            session.setAttribute("loggedUser", tutor);
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Invalid Access Path or Secure Key");
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "user/register";
    }

    @GetMapping("/users/register")
    public String showUserRegisterForm() {
        return "user/register";
    }

    @PostMapping("/users/register")
    public String registerUser(@RequestParam String name, @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam(defaultValue = "STUDENT") String role,
                               @RequestParam(required = false) String grade,
                               @RequestParam(required = false) String subject,
                               @RequestParam(required = false) Double rate,
                               Model model) {
        User existingUser = userService.getUserByEmail(email);
        Tutor existingTutor = tutorService.getTutorByEmail(email);
        if (existingUser != null || existingTutor != null) {
            model.addAttribute("error", "Email already exists");
            return "user/register";
        }

        if ("TUTOR".equalsIgnoreCase(role)) {
            String tutorId = IDGenerator.generate("T");
            Tutor newTutor = new Tutor(tutorId, name, email, password, "",
                    subject != null ? subject : "", 0, rate != null ? rate : 0.0, 0.0);
            tutorService.addTutor(newTutor);
        } else {
            String userId = IDGenerator.generate("U");
            Student newStudent = new Student(userId, name, email, password,
                    grade != null ? grade : "");
            userService.registerUser(newStudent);
        }
        model.addAttribute("success", "Registration successful! Please login.");
        return "auth/login";
    }

    @GetMapping("/tutors/register")
    public String showTutorRegisterForm() {
        return "auth/tutor-register";
    }

    @PostMapping("/tutors/register")
    public String registerTutor(@RequestParam String name, @RequestParam String email,
                                @RequestParam String password, @RequestParam String phone,
                                @RequestParam String specialization, @RequestParam Double hourlyRate,
                                Model model) {
        Tutor existingTutor = tutorService.getTutorByEmail(email);
        if (existingTutor != null) {
            model.addAttribute("error", "Email already exists");
            return "auth/tutor-register";
        }

        String tutorId = IDGenerator.generate("T");
        Tutor newTutor = new Tutor(tutorId, name, email, password, phone, specialization, 0, hourlyRate, 0.0);
        tutorService.addTutor(newTutor);
        model.addAttribute("success", "Registration successful! Please login.");
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

