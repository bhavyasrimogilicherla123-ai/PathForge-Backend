package com.pathforge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "career_role_skills")
public class CareerRoleSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "career_role_id", nullable = false)
    private CareerRole careerRole;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "required_score_percentage", nullable = false)
    private Integer requiredScorePercentage = 60;

    @Column(name = "priority_order")
    private Integer priorityOrder = 1;

    public CareerRoleSkill() {}

    public CareerRoleSkill(CareerRole careerRole, Skill skill, Integer requiredScorePercentage, Integer priorityOrder) {
        this.careerRole = careerRole;
        this.skill = skill;
        this.requiredScorePercentage = requiredScorePercentage;
        this.priorityOrder = priorityOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CareerRole getCareerRole() { return careerRole; }
    public void setCareerRole(CareerRole careerRole) { this.careerRole = careerRole; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public Integer getRequiredScorePercentage() { return requiredScorePercentage; }
    public void setRequiredScorePercentage(Integer requiredScorePercentage) { this.requiredScorePercentage = requiredScorePercentage; }

    public Integer getPriorityOrder() { return priorityOrder; }
    public void setPriorityOrder(Integer priorityOrder) { this.priorityOrder = priorityOrder; }
}
