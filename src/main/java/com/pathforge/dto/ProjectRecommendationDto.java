package com.pathforge.dto;

import java.util.List;

public class ProjectRecommendationDto {

    private Long id;
    private String title;
    private String description;
    private String problemStatement;
    private String difficultyLevel;
    private String careerRoleTitle;
    private String whyThisProject;
    private List<String> skillsYouWillImprove;
    private Boolean isEnrolled = false;
    private String studentProjectStatus;

    public ProjectRecommendationDto() {}

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

    public String getCareerRoleTitle() { return careerRoleTitle; }
    public void setCareerRoleTitle(String careerRoleTitle) { this.careerRoleTitle = careerRoleTitle; }

    public String getWhyThisProject() { return whyThisProject; }
    public void setWhyThisProject(String whyThisProject) { this.whyThisProject = whyThisProject; }

    public List<String> getSkillsYouWillImprove() { return skillsYouWillImprove; }
    public void setSkillsYouWillImprove(List<String> skillsYouWillImprove) { this.skillsYouWillImprove = skillsYouWillImprove; }

    public Boolean getIsEnrolled() { return isEnrolled; }
    public void setIsEnrolled(Boolean isEnrolled) { this.isEnrolled = isEnrolled; }

    public String getStudentProjectStatus() { return studentProjectStatus; }
    public void setStudentProjectStatus(String studentProjectStatus) { this.studentProjectStatus = studentProjectStatus; }
}
