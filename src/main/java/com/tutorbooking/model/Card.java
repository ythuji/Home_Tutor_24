package com.tutorbooking.model;

import java.io.Serializable;

public class Card implements Serializable {
    private String id;
    private String userId;
    private String cardholderName;
    private String cardNumber;
    private String expiryDate; // MM/YY format
    private String cvv;

    public Card() {
    }

    public Card(String id, String userId, String cardholderName, String cardNumber, String expiryDate, String cvv) {
        this.id = id;
        this.userId = userId;
        this.cardholderName = cardholderName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return id + "|" + userId + "|" + cardholderName + "|" + cardNumber + "|" + expiryDate + "|" + cvv;
    }
}

