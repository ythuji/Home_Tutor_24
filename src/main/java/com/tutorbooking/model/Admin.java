package com.tutorbooking.model;

public class Admin extends User {
    private String adminLevel; // ADMIN or SUPER_ADMIN
    private String createdDate;
    private boolean active;

    public Admin() {
        super();
        this.setRole("ADMIN");
        this.active = true;
    }

    public Admin(String id, String name, String email, String password, String adminLevel, String createdDate) {
        super(id, name, email, password, "ADMIN");
        this.adminLevel = adminLevel;
        this.createdDate = createdDate;
        this.active = true;
    }

    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return getId() + "|" + getName() + "|" + getEmail() + "|" + getPassword() + "|" + adminLevel + "|" + createdDate;
    }
}