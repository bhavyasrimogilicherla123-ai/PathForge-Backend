package com.pathforge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String branch; // CSE, IT, AI & ML, Data Science, ECE, EEE, etc.

    @Column(name = "academic_year", nullable = false)
    private String year; // 1st Year, 2nd Year, 3rd Year, 4th Year

    private String semester; // Semester 1 - 8

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "career_goal_id")
    private CareerRole careerGoal;

    @Column(columnDefinition = "TEXT")
    private String interests;

    @Column(name = "current_stage")
    private String currentStage = "Explorer"; // Explorer, Foundation, Builder, Developer, Job Ready

    @Column(name = "stage_reason", columnDefinition = "TEXT")
    private String stageReason;

    public StudentProfile() {}

    public StudentProfile(User user, String branch, String year, String semester, CareerRole careerGoal, String interests) {
        this.user = user;
        this.branch = branch;
        this.year = year;
        this.semester = semester;
        this.careerGoal = careerGoal;
        this.interests = interests;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public CareerRole getCareerGoal() { return careerGoal; }
    public void setCareerGoal(CareerRole careerGoal) { this.careerGoal = careerGoal; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }

    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }

    public String getStageReason() { return stageReason; }
    public void setStageReason(String stageReason) { this.stageReason = stageReason; }
}
