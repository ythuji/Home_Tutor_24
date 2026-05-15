package com.tutorbooking.repository;

import com.tutorbooking.model.User;
import com.tutorbooking.model.Student;
import com.tutorbooking.model.Tutor;
import com.tutorbooking.util.FileHelper;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String FILE_NAME = "users.txt";

    public List<User> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 5) continue; // Skip invalid lines

            if (parts[4].equals("STUDENT")) {
                String grade = parts.length > 5 ? parts[5] : "";
                users.add(new Student(parts[0], parts[1], parts[2], parts[3], grade));
            } else if (parts[4].equals("TUTOR")) {
                String phone = parts.length > 5 ? parts[5] : "";
                String specialization = parts.length > 6 ? parts[6] : "";
                double rate = parts.length > 7 ? Double.parseDouble(parts[7]) : 0.0;
                users.add(new Tutor(parts[0], parts[1], parts[2], parts[3], phone, specialization, 0, rate, 0.0));
            }
        }
        return users;
    }

    public void save(User user) {
        FileHelper.appendLine(FILE_NAME, user.toString());
    }

    public boolean update(String id, User user) {
        return FileHelper.updateRecordById(FILE_NAME, id, user.toString());
    }

    public boolean deleteById(String id) {
        return FileHelper.deleteRecordById(FILE_NAME, id);
    }

    public User findByEmail(String email) {
        return findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}