package com.pathforge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_skills", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_profile_id", "skill_id"})
})
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "score_percentage", nullable = false)
    private Integer scorePercentage = 0; // 0 - 100

    @Column(name = "level_name")
    private String levelName = "Beginner"; // Beginner (0-20), Basic (21-40), Intermediate (41-60), Good (61-80), Strong (81-100)

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public StudentSkill() {}

    public StudentSkill(StudentProfile studentProfile, Skill skill, Integer scorePercentage) {
        this.studentProfile = studentProfile;
        this.skill = skill;
        setScorePercentage(scorePercentage);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentProfile getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfile studentProfile) { this.studentProfile = studentProfile; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public Integer getScorePercentage() { return scorePercentage; }
    public void setScorePercentage(Integer scorePercentage) {
        this.scorePercentage = scorePercentage;
        this.updatedAt = LocalDateTime.now();
        if (scorePercentage <= 20) this.levelName = "Beginner";
        else if (scorePercentage <= 40) this.levelName = "Basic";
        else if (scorePercentage <= 60) this.levelName = "Intermediate";
        else if (scorePercentage <= 80) this.levelName = "Good";
        else this.levelName = "Strong";
    }

    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
