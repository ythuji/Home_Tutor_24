package com.tutorbooking.model;

package com.tutorbooking.model;

public class Review {
    private String id;
    private String studentId;
    private String tutorId;
    private int rating;
    private String comment;

    public Review(String id, String studentId, String tutorId, int rating, String comment) {
        this.id = id;
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getTutorId() { return tutorId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }

    @Override
    public String toString() {
        return id + "|" + studentId + "|" + tutorId + "|" + rating + "|" + comment;
    }
}
