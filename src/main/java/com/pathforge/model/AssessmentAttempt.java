package com.pathforge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_attempts")
public class AssessmentAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    @Column(name = "total_score")
    private Integer totalScore = 0;

    @Column(name = "max_score")
    private Integer maxScore = 0;

    private Double percentage = 0.0;

    @Column(name = "completed_at")
    private LocalDateTime completedAt = LocalDateTime.now();

    public AssessmentAttempt() {}

    public AssessmentAttempt(StudentProfile studentProfile, Integer totalScore, Integer maxScore, Double percentage) {
        this.studentProfile = studentProfile;
        this.totalScore = totalScore;
        this.maxScore = maxScore;
        this.percentage = percentage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentProfile getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfile studentProfile) { this.studentProfile = studentProfile; }

    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }

    public Integer getMaxScore() { return maxScore; }
    public void setMaxScore(Integer maxScore) { this.maxScore = maxScore; }

    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
