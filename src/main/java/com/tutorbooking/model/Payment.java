package com.tutorbooking.model;

public class Payment {
    private String id;
    private String bookingId;
    private String studentId;
    private double amount;
    private String paymentMethod;
    private String paymentDate;
    private String status; // PENDING, PAID, REFUNDED

    public Payment(String id, String bookingId, String studentId, double amount, String paymentMethod, String paymentDate, String status) {
        this.id = id;
        this.bookingId = bookingId;
        this.studentId = studentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public String getId() { return id; }
    public String getBookingId() { return bookingId; }
    public String getStudentId() { return studentId; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return id + "|" + bookingId + "|" + studentId + "|" + amount + "|" + paymentMethod + "|" + paymentDate + "|" + status;
    }
}
