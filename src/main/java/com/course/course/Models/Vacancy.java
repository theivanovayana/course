package com.course.course.Models;

public class Vacancy {
    private String id;
    private String title;
    private String companyName;
    private String salary;
    private String description;
    private String phone;
    private boolean isActive;
    private String companyId;


    // Геттеры и сеттеры
    public String getTitle() { return title; }
    public String getCompanyName() { return companyName; }
    public String getSalary() { return salary; }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}