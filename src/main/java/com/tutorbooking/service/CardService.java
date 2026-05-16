package com.tutorbooking.service;

import com.tutorbooking.model.Card;
import com.tutorbooking.repository.CardRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CardService {
    private final CardRepository cardRepository = new CardRepository();

    public List<Card> getCardsByUserId(String userId) {
        return cardRepository.findByUserId(userId);
    }

    public Card getCardById(String cardId) {
        return cardRepository.findById(cardId);
    }

    public void addCard(Card card) {
        if (card.getId() == null || card.getId().isEmpty()) {
            card.setId(generateCardId());
        }
        card.setCardNumber(normalizeCardNumber(card.getCardNumber()));
        cardRepository.save(card);
    }

    public void updateCard(String cardId, String cardholderName, String cardNumber, String expiryDate, String cvv) {
        Card card = cardRepository.findById(cardId);
        if (card != null) {
            card.setCardholderName(cardholderName);
            card.setCardNumber(normalizeCardNumber(cardNumber));
            card.setExpiryDate(expiryDate);
            card.setCvv(cvv);
            cardRepository.save(card);
        }
    }

    public void deleteCard(String cardId) {
        cardRepository.deleteById(cardId);
    }

    public boolean cardBelongsToUser(String cardId, String userId) {
        Card card = getCardById(cardId);
        return card != null && card.getUserId().equals(userId);
    }

    private String generateCardId() {
        return "C" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String normalizeCardNumber(String cardNumber) {
        // Remove spaces and dashes
        String cleaned = cardNumber.replaceAll("[\\s\\-]", "");
        // Validate 16 digits
        if (!cleaned.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
        return cleaned;
    }
}

