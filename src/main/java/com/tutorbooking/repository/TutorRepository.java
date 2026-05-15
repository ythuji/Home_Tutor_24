package com.tutorbooking.repository;

import com.tutorbooking.model.Tutor;
import com.tutorbooking.util.FileHelper;
import java.util.ArrayList;
import java.util.List;

public class TutorRepository {

    private static final String FILE_NAME = "data/tutors.txt";

    public List<Tutor> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<Tutor> tutors = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 9) {
                tutors.add(new Tutor(
                        parts[1], parts[2], parts[3],
                        parts[0], parts[4],
                        Integer.parseInt(parts[5]),
                        parts[6],
                        Double.parseDouble(parts[7]),
                        parts[8]
                ));
            }
        }
        return tutors;
    }

    public Tutor findById(String id) {
        for (Tutor t : findAll()) {
            if (t.getTutorId().equals(id)) return t;
        }
        return null;
    }

    public void save(Tutor tutor) {
        FileHelper.appendLine(FILE_NAME, tutor.toString());
    }

    public boolean update(String id, Tutor tutor) {
        return FileHelper.updateRecordById(FILE_NAME, id, tutor.toString());
    }

    public boolean deleteById(String id) {
        return FileHelper.deleteRecordById(FILE_NAME, id);
    }
}