package com.tutorbooking.model;

public class Subject {
    private String id;
    private String name;
    private String category;
    private String level;

    public Subject(String id, String name, String category, String level) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.level = level;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getLevel() { return level; }

    @Override
    public String toString() {
        return id + "|" + name + "|" + category + "|" + level;
    }
}