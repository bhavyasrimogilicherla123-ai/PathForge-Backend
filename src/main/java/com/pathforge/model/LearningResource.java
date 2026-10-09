package com.pathforge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "learning_resources")
public class LearningResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String topic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    private String difficulty = "Beginner"; // Beginner, Intermediate, Advanced

    @Column(name = "resource_type", nullable = false)
    private String resourceType; // Video, Article, Documentation, Course, Practice, Quiz

    @Column(nullable = false)
    private String url;

    private String duration; // e.g., "15 mins", "1.5 hours"

    private String source; // YouTube, MDN, Official Docs, LeetCode, etc.

    public LearningResource() {}

    public LearningResource(String title, String description, String topic, Skill skill, String difficulty, String resourceType, String url, String duration, String source) {
        this.title = title;
        this.description = description;
        this.topic = topic;
        this.skill = skill;
        this.difficulty = difficulty;
        this.resourceType = resourceType;
        this.url = url;
        this.duration = duration;
        this.source = source;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
