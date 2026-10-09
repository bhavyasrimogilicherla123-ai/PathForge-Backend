package com.pathforge.dto;

import java.util.List;
import java.util.Map;

public class StudentProfileDto {

    private Long id;
    private String name;
    private String email;
    private String branch;
    private String year;
    private String semester;
    private Long careerGoalId;
    private String careerGoalTitle;
    private String interests;
    private String currentStage;
    private String stageReason;
    private Map<String, Integer> selfReportedSkills; // Skill Name -> Score %
    private List<String> completedProjects;

    public StudentProfileDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Long getCareerGoalId() { return careerGoalId; }
    public void setCareerGoalId(Long careerGoalId) { this.careerGoalId = careerGoalId; }

    public String getCareerGoalTitle() { return careerGoalTitle; }
    public void setCareerGoalTitle(String careerGoalTitle) { this.careerGoalTitle = careerGoalTitle; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }

    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }

    public String getStageReason() { return stageReason; }
    public void setStageReason(String stageReason) { this.stageReason = stageReason; }

    public Map<String, Integer> getSelfReportedSkills() { return selfReportedSkills; }
    public void setSelfReportedSkills(Map<String, Integer> selfReportedSkills) { this.selfReportedSkills = selfReportedSkills; }

    public List<String> getCompletedProjects() { return completedProjects; }
    public void setCompletedProjects(List<String> completedProjects) { this.completedProjects = completedProjects; }
}
