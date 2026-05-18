
package com.tutorbooking.repository;

import com.tutorbooking.model.Review;
import com.tutorbooking.util.FileHelper;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {
    private static final String FILE_NAME = "reviews.txt";

    public List<Review> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<Review> reviews = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 5) {
                reviews.add(new Review(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]));
            }
        }
        return reviews;
    }

    public void save(Review review) {
        FileHelper.appendLine(FILE_NAME, review.toString());
    }

    public boolean update(String id, Review review) {
        return FileHelper.updateRecordById(FILE_NAME, id, review.toString());
    }

    public boolean deleteById(String id) {
        return FileHelper.deleteRecordById(FILE_NAME, id);
    }
}
