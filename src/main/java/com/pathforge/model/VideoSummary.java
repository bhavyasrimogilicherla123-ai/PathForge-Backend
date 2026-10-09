package com.pathforge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "video_summaries")
public class VideoSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id")
    private StudentProfile studentProfile;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "video_title", nullable = false)
    private String videoTitle;

    private String duration;

    private String channel;

    @Column(name = "summary_text", columnDefinition = "TEXT")
    private String summaryText;

    @Column(name = "important_concepts", columnDefinition = "TEXT")
    private String importantConcepts;

    @Column(name = "beginner_explanation", columnDefinition = "TEXT")
    private String beginnerExplanation;

    @Column(name = "practical_examples", columnDefinition = "TEXT")
    private String practicalExamples;

    @Column(name = "important_points", columnDefinition = "TEXT")
    private String importantPoints;

    @Column(name = "quiz_json", columnDefinition = "LONGTEXT")
    private String quizJson;

    @Column(name = "interview_questions_json", columnDefinition = "LONGTEXT")
    private String interviewQuestionsJson;

    @Column(name = "mapped_skills", columnDefinition = "TEXT")
    private String mappedSkills;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public VideoSummary() {}

    public VideoSummary(StudentProfile studentProfile, String videoUrl, String videoTitle, String duration, String channel,
                        String summaryText, String importantConcepts, String beginnerExplanation,
                        String practicalExamples, String importantPoints, String quizJson,
                        String interviewQuestionsJson, String mappedSkills) {
        this.studentProfile = studentProfile;
        this.videoUrl = videoUrl;
        this.videoTitle = videoTitle;
        this.duration = duration;
        this.channel = channel;
        this.summaryText = summaryText;
        this.importantConcepts = importantConcepts;
        this.beginnerExplanation = beginnerExplanation;
        this.practicalExamples = practicalExamples;
        this.importantPoints = importantPoints;
        this.quizJson = quizJson;
        this.interviewQuestionsJson = interviewQuestionsJson;
        this.mappedSkills = mappedSkills;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentProfile getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfile studentProfile) { this.studentProfile = studentProfile; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getVideoTitle() { return videoTitle; }
    public void setVideoTitle(String videoTitle) { this.videoTitle = videoTitle; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getSummaryText() { return summaryText; }
    public void setSummaryText(String summaryText) { this.summaryText = summaryText; }

    public String getImportantConcepts() { return importantConcepts; }
    public void setImportantConcepts(String importantConcepts) { this.importantConcepts = importantConcepts; }

    public String getBeginnerExplanation() { return beginnerExplanation; }
    public void setBeginnerExplanation(String beginnerExplanation) { this.beginnerExplanation = beginnerExplanation; }

    public String getPracticalExamples() { return practicalExamples; }
    public void setPracticalExamples(String practicalExamples) { this.practicalExamples = practicalExamples; }

    public String getImportantPoints() { return importantPoints; }
    public void setImportantPoints(String importantPoints) { this.importantPoints = importantPoints; }

    public String getQuizJson() { return quizJson; }
    public void setQuizJson(String quizJson) { this.quizJson = quizJson; }

    public String getInterviewQuestionsJson() { return interviewQuestionsJson; }
    public void setInterviewQuestionsJson(String interviewQuestionsJson) { this.interviewQuestionsJson = interviewQuestionsJson; }

    public String getMappedSkills() { return mappedSkills; }
    public void setMappedSkills(String mappedSkills) { this.mappedSkills = mappedSkills; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
