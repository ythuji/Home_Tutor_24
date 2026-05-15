package com.tutorbooking.controller;

import com.tutorbooking.model.Subject;
import com.tutorbooking.service.SubjectService;
import com.tutorbooking.util.IDGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService = new SubjectService();

    @GetMapping("/list")
    public String listSubjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "subject/subject-list";
    }

    @PostMapping("/add")
    public String addSubject(@RequestParam String name, @RequestParam String category, @RequestParam String level) {
        String id = IDGenerator.generate("S");
        subjectService.addSubject(new Subject(id, name, category, level));
        return "redirect:/subjects/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable String id) {
        subjectService.removeSubject(id);
        return "redirect:/subjects/list";
    }
}

