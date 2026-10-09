package com.pathforge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "roadmaps")
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "career_role_id")
    private CareerRole careerRole;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt = LocalDateTime.now();

    private String status = "ACTIVE"; // ACTIVE, ARCHIVED

    public Roadmap() {}

    public Roadmap(StudentProfile studentProfile, String title, CareerRole careerRole) {
        this.studentProfile = studentProfile;
        this.title = title;
        this.careerRole = careerRole;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentProfile getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfile studentProfile) { this.studentProfile = studentProfile; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public CareerRole getCareerRole() { return careerRole; }
    public void setCareerRole(CareerRole careerRole) { this.careerRole = careerRole; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
