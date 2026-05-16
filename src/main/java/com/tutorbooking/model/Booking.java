package com.tutorbooking.model;

public class Booking {
    private String id;
    private String studentId;
    private String tutorId;
    private String subject;
    private String dateTime;
    private String status; // PENDING, ACCEPTED, COMPLETED, CANCELLED

    public Booking(String id, String studentId, String tutorId, String subject, String dateTime, String status) {
        this.id = id;
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.subject = subject;
        this.dateTime = dateTime;
        this.status = status;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public String getSubject() {
        return subject;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return id + "|" + studentId + "|" + tutorId + "|" + subject + "|" + dateTime + "|" + status;
    }
}