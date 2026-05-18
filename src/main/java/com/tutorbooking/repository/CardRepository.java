package com.tutorbooking.repository;

import com.tutorbooking.model.Card;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CardRepository {
    private static final String FILE_PATH = "src/main/resources/data/cards.txt";

    public List<Card> findAll() {
        List<Card> cards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    cards.add(parseCard(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public List<Card> findByUserId(String userId) {
        return findAll().stream()
                .filter(card -> card.getUserId().equals(userId))
                .toList();
    }

    public Card findById(String id) {
        return findAll().stream()
                .filter(card -> card.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Card card) {
        try {
            List<Card> cards = findAll();
            cards.removeIf(c -> c.getId().equals(card.getId()));
            cards.add(card);
            writeToFile(cards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(String id) {
        try {
            List<Card> cards = findAll();
            cards.removeIf(c -> c.getId().equals(id));
            writeToFile(cards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(List<Card> cards) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Card card : cards) {
                writer.write(card.toString());
                writer.newLine();
            }
        }
    }

    private Card parseCard(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length >= 6) {
            return new Card(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        }
        return null;
    }
}

