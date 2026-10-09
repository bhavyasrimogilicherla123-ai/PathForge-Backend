package com.pathforge.dto;

import java.util.Map;

public class ProgressDto {

    private String studentName;
    private String currentStage;
    private String stageReason;
    private String targetCareer;
    private Double overallProgress; // 0 - 100%
    private Double technicalSkillsProgress;
    private Double dsaProgress;
    private Double projectProgress;
    private Double gitProgress;
    private Double interviewPrepProgress;
    private Boolean resumeCompleted;
    private Map<String, Integer> skillBreakdown; // Skill Name -> Score %
    private Integer completedProjectsCount;
    private Integer completedQuizzesCount;

    public ProgressDto() {}

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }

    public String getStageReason() { return stageReason; }
    public void setStageReason(String stageReason) { this.stageReason = stageReason; }

    public String getTargetCareer() { return targetCareer; }
    public void setTargetCareer(String targetCareer) { this.targetCareer = targetCareer; }

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

    public Map<String, Integer> getSkillBreakdown() { return skillBreakdown; }
    public void setSkillBreakdown(Map<String, Integer> skillBreakdown) { this.skillBreakdown = skillBreakdown; }

    public Integer getCompletedProjectsCount() { return completedProjectsCount; }
    public void setCompletedProjectsCount(Integer completedProjectsCount) { this.completedProjectsCount = completedProjectsCount; }

    public Integer getCompletedQuizzesCount() { return completedQuizzesCount; }
    public void setCompletedQuizzesCount(Integer completedQuizzesCount) { this.completedQuizzesCount = completedQuizzesCount; }
}
