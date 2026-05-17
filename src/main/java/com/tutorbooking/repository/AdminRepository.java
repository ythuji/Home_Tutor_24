package com.tutorbooking.repository;

import com.tutorbooking.model.Admin;
import com.tutorbooking.util.FileHelper;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {
    private static final String FILE_NAME = "admin.txt";

    public List<Admin> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<Admin> admins = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                admins.add(new Admin(parts[0], parts[1], parts[2], parts[3], parts[4],
                        parts.length > 5 ? parts[5] : ""));
            }
        }
        return admins;
    }

    public Admin findByEmail(String email) {
        return findAll().stream()
                .filter(a -> a.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public Admin findById(String id) {
        return findAll().stream()
                .filter(a -> a.toString().split("\\|")[0].equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Admin admin) {
        FileHelper.appendLine(FILE_NAME, admin.toString());
    }

    public boolean update(String id, Admin admin) {
        return FileHelper.updateRecordById(FILE_NAME, id, admin.toString());
    }

    public boolean deleteById(String id) {
        return FileHelper.deleteRecordById(FILE_NAME, id);
    }
}