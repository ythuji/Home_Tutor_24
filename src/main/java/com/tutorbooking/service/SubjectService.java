package com.tutorbooking.service;

import com.tutorbooking.model.Subject;
import com.tutorbooking.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository = new SubjectRepository();

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public void addSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    public void removeSubject(String id) {
        subjectRepository.deleteById(id);
    }
}