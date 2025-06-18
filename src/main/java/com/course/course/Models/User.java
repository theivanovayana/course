package com.course.course.Models;

public class User {
    private String id;
    private  String login;
    private String password;
    private boolean isApplicant;
    private String fullName;
    private String qualification;
    private String profession;
    private String phone;
    private String companyName;
    private String companyPhone;
    private String address;
    private ActivityType activityType;


    public void setId(String id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public String getAddress() {
        return address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean getIsApplicant() {
        return isApplicant;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfession() {
        return profession;
    }

    public String getQualification() {
        return qualification;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setIsApplicant(boolean isApplicant) {
        this.isApplicant = isApplicant;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
