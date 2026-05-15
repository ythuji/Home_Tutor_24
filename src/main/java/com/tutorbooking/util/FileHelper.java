package com.tutorbooking.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    public static List<String> readAllLines(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return lines;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void appendLine(String fileName, String line) {
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateRecordById(String fileName, String id, String newLine) {
        List<String> lines = readAllLines(fileName);
        boolean found = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(id)) {
                    writer.write(newLine);
                    found = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return found;
    }

    public static boolean deleteRecordById(String fileName, String id) {
        List<String> lines = readAllLines(fileName);
        boolean found = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(id)) {
                    found = true;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return found;
    }
}
