package com.tutorbooking.model;

public class Tutor extends User {

    private String tutorId;
    private String subject;
    private int experienceYears;
    private String availability;
    private double hourlyRate;
    private String tutorType;

    public Tutor(String name, String email, String phone,
                 String tutorId, String subject, int experienceYears,
                 String availability, double hourlyRate, String tutorType) {
        super(name, email, phone);
        this.tutorId = tutorId;
        this.subject = subject;
        this.experienceYears = experienceYears;
        this.availability = availability;
        this.hourlyRate = hourlyRate;
        this.tutorType = tutorType;
    }

    public String getTutorId() { return tutorId; }
    public void setTutorId(String tutorId) { this.tutorId = tutorId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getTutorType() { return tutorType; }
    public void setTutorType(String tutorType) { this.tutorType = tutorType; }

    @Override
    public String getDetails() {
        return "ID: " + tutorId +
                " | Name: " + getName() +
                " | Subject: " + subject +
                " | Experience: " + experienceYears + " yrs" +
                " | Rate: Rs." + hourlyRate + "/hr" +
                " | Availability: " + availability +
                " | Type: " + tutorType;
    }

    @Override
    public String toString() {
        return tutorId + "|" + getName() + "|" + getEmail() + "|" +
                getPhone() + "|" + subject + "|" + experienceYears + "|" +
                availability + "|" + hourlyRate + "|" + tutorType;
    }
}