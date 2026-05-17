package com.tutorbooking.service;

import com.tutorbooking.model.Tutor;
import com.tutorbooking.repository.TutorRepository;
import java.util.List;

public class TutorService {

    private TutorRepository tutorRepository = new TutorRepository();

    // CREATE
    public void addTutor(Tutor tutor) {
        tutorRepository.save(tutor);
    }

    // READ ALL
    public List<Tutor> getAllTutors() {
        return tutorRepository.findAll();
    }

    // READ ONE
    public Tutor getTutorById(String id) {
        return tutorRepository.findById(id);
    }

    // UPDATE
    public boolean updateTutor(String id, Tutor tutor) {
        return tutorRepository.update(id, tutor);
    }

    // DELETE
    public boolean deleteTutor(String id) {
        return tutorRepository.deleteById(id);
    }

    public Tutor authenticate(String email, String password) {
        List<Tutor> tutors = getAllTutors();
        for (Tutor tutor : tutors) {
            if (tutor.getEmail().equals(email) && tutor.getPassword().equals(password)) {
                return tutor;
            }
        }
        return null;
    }

    public Tutor getTutorByEmail(String email) {
        List<Tutor> tutors = getAllTutors();
        for (Tutor tutor : tutors) {
            if (tutor.getEmail().equalsIgnoreCase(email)) {
                return tutor;
            }
        }
        return null;
    }
}
