package com.pathforge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_trackers")
public class ProgressTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_profile_id", nullable = false, unique = true)
    private StudentProfile studentProfile;

    @Column(name = "overall_progress")
    private Double overallProgress = 0.0;

    @Column(name = "technical_skills_progress")
    private Double technicalSkillsProgress = 0.0;

    @Column(name = "dsa_progress")
    private Double dsaProgress = 0.0;

    @Column(name = "project_progress")
    private Double projectProgress = 0.0;

    @Column(name = "git_progress")
    private Double gitProgress = 0.0;

    @Column(name = "interview_prep_progress")
    private Double interviewPrepProgress = 0.0;

    @Column(name = "resume_completed")
    private Boolean resumeCompleted = false;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public ProgressTracker() {}

    public ProgressTracker(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentProfile getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfile studentProfile) { this.studentProfile = studentProfile; }

    public Double getOverallProgress() { return overallProgress; }
    public void setOverallProgress(Double overallProgress) { this.overallProgress = overallProgress; }

    public Double getTechnicalSkillsProgress() { return technicalSkillsProgress; }
    public void setTechnicalSkillsProgress(Double technicalSkillsProgress) { this.technicalSkillsProgress = technicalSkillsProgress; }

    public Double getDsaProgress() { return dsaProgress; }
    public void setDsaProgress(Double dsaProgress) { this.dsaProgress = dsaProgress; }

    public Double getProjectProgress() { return projectProgress; }
    public void setProjectProgress(Double projectProgress) { this.projectProgress = projectProgress; }

    public Double getGitProgress() { return gitProgress; }
    public void setGitProgress(Double gitProgress) { this.gitProgress = gitProgress; }

    public Double getInterviewPrepProgress() { return interviewPrepProgress; }
    public void setInterviewPrepProgress(Double interviewPrepProgress) { this.interviewPrepProgress = interviewPrepProgress; }

    public Boolean getResumeCompleted() { return resumeCompleted; }
    public void setResumeCompleted(Boolean resumeCompleted) { this.resumeCompleted = resumeCompleted; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
