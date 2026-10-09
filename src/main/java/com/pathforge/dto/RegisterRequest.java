package com.pathforge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Branch is required")
    private String branch; // CSE, IT, AI & ML, Data Science, ECE, EEE, etc.

    @NotBlank(message = "Year is required")
    private String year; // 1st Year, 2nd Year, 3rd Year, 4th Year

    private String semester;

    private Long careerGoalId;

    public RegisterRequest() {}

    public RegisterRequest(String name, String email, String password, String branch, String year, String semester, Long careerGoalId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.branch = branch;
        this.year = year;
        this.semester = semester;
        this.careerGoalId = careerGoalId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Long getCareerGoalId() { return careerGoalId; }
    public void setCareerGoalId(Long careerGoalId) { this.careerGoalId = careerGoalId; }
}
