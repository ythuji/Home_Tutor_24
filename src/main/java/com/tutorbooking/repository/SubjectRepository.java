package com.tutorbooking.repository;

import com.tutorbooking.model.Subject;
import com.tutorbooking.util.FileHelper;
import java.util.ArrayList;
import java.util.List;

public class SubjectRepository {
    private static final String FILE_NAME = "subjects.txt";

    public List<Subject> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<Subject> subjects = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                subjects.add(new Subject(parts[0], parts[1], parts[2], parts[3]));
            }
        }
        return subjects;
    }

    public void save(Subject subject) {
        FileHelper.appendLine(FILE_NAME, subject.toString());
    }

    public boolean update(String id, Subject subject) {
        return FileHelper.updateRecordById(FILE_NAME, id, subject.toString());
    }

    public boolean deleteById(String id) {
        return FileHelper.deleteRecordById(FILE_NAME, id);
    }
}