package com.tutorbooking.model;

public class Student extends User {
    private String grade;

    public Student() {
        super();
        this.setRole("STUDENT");
    }

    public Student(String id, String name, String email, String password, String grade) {
        super(id, name, email, password, "STUDENT");
        this.grade = grade;
    }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override
    public String toString() {
        return super.toString() + "|" + grade;
    }
}