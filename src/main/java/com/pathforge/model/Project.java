package com.pathforge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "problem_statement", columnDefinition = "TEXT")
    private String problemStatement;

    @Column(name = "difficulty_level", nullable = false)
    private String difficultyLevel = "Intermediate"; // Beginner, Intermediate, Advanced

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "career_role_id")
    private CareerRole careerRole;

    @Column(name = "why_this_project", columnDefinition = "TEXT")
    private String whyThisProject;

    public Project() {}

    public Project(String title, String description, String problemStatement, String difficultyLevel, CareerRole careerRole, String whyThisProject) {
        this.title = title;
        this.description = description;
        this.problemStatement = problemStatement;
        this.difficultyLevel = difficultyLevel;
        this.careerRole = careerRole;
        this.whyThisProject = whyThisProject;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProblemStatement() { return problemStatement; }
    public void setProblemStatement(String problemStatement) { this.problemStatement = problemStatement; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public CareerRole getCareerRole() { return careerRole; }
    public void setCareerRole(CareerRole careerRole) { this.careerRole = careerRole; }

    public String getWhyThisProject() { return whyThisProject; }
    public void setWhyThisProject(String whyThisProject) { this.whyThisProject = whyThisProject; }
}
