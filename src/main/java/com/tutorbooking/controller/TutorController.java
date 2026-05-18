package com.tutorbooking.controller;

import com.tutorbooking.model.Tutor;
import com.tutorbooking.service.TutorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tutors")
public class TutorController {

    private final TutorService tutorService = new TutorService();

    // Show all tutors
    @GetMapping({"", "/list"})
    public String listTutors(Model model) {
        model.addAttribute("tutors", tutorService.getAllTutors());
        return "tutor/tutor-list";
    }

    // Show add tutor form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("tutor", new Tutor(
                "", "", "", "", "", 0, "", 0.0, ""
        ));
        model.addAttribute("action", "add");
        return "tutor/tutor-form";
    }

    // Submit add tutor form
    @PostMapping("/add")
    public String addTutor(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String phone,
                           @RequestParam String tutorId,
                           @RequestParam String subject,
                           @RequestParam int experienceYears,
                           @RequestParam String availability,
                           @RequestParam double hourlyRate,
                           @RequestParam String tutorType) {
        Tutor tutor = new Tutor(name, email, phone, tutorId,
                subject, experienceYears,
                availability, hourlyRate, tutorType);
        tutorService.addTutor(tutor);
        return "redirect:/tutors";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Tutor tutor = tutorService.getTutorById(id);
        model.addAttribute("tutor", tutor);
        model.addAttribute("action", "edit");
        return "tutor/tutor-form";
    }

    // Submit edit form
    @PostMapping("/edit/{id}")
    public String updateTutor(@PathVariable String id,
                              @RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String phone,
                              @RequestParam String subject,
                              @RequestParam int experienceYears,
                              @RequestParam String availability,
                              @RequestParam double hourlyRate,
                              @RequestParam String tutorType) {
        Tutor tutor = new Tutor(name, email, phone, id,
                subject, experienceYears,
                availability, hourlyRate, tutorType);
        tutorService.updateTutor(id, tutor);
        return "redirect:/tutors";
    }

    // Delete tutor
    @GetMapping("/delete/{id}")
    public String deleteTutor(@PathVariable String id) {
        tutorService.deleteTutor(id);
        return "redirect:/tutors";
    }

    // Search tutor
    @GetMapping("/search")
    public String searchTutor(@RequestParam String id, Model model) {
        Tutor tutor = tutorService.getTutorById(id);
        model.addAttribute("tutor", tutor);
        model.addAttribute("tutors", tutorService.getAllTutors());
        return "tutor/tutor-list";
    }
}
