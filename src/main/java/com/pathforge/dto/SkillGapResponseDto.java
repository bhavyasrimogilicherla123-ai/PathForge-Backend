package com.pathforge.dto;

import java.util.List;

public class SkillGapResponseDto {

    public static class SkillGapItem {
        private String skillName;
        private String category;
        private Integer currentScore;
        private Integer requiredScore;
        private String currentLevel;
        private String gapStatus; // Major Gap, Needs Improvement, Sufficient, Strong
        private String recommendation;

        public SkillGapItem() {}

        public SkillGapItem(String skillName, String category, Integer currentScore, Integer requiredScore, String currentLevel, String gapStatus, String recommendation) {
            this.skillName = skillName;
            this.category = category;
            this.currentScore = currentScore;
            this.requiredScore = requiredScore;
            this.currentLevel = currentLevel;
            this.gapStatus = gapStatus;
            this.recommendation = recommendation;
        }

        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public Integer getCurrentScore() { return currentScore; }
        public void setCurrentScore(Integer currentScore) { this.currentScore = currentScore; }

        public Integer getRequiredScore() { return requiredScore; }
        public void setRequiredScore(Integer requiredScore) { this.requiredScore = requiredScore; }

        public String getCurrentLevel() { return currentLevel; }
        public void setCurrentLevel(String currentLevel) { this.currentLevel = currentLevel; }

        public String getGapStatus() { return gapStatus; }
        public void setGapStatus(String gapStatus) { this.gapStatus = gapStatus; }

        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    }

    private String targetCareer;
    private String currentStage;
    private String stageReason;
    private List<SkillGapItem> skillGaps;

    public SkillGapResponseDto() {}

    public SkillGapResponseDto(String targetCareer, String currentStage, String stageReason, List<SkillGapItem> skillGaps) {
        this.targetCareer = targetCareer;
        this.currentStage = currentStage;
        this.stageReason = stageReason;
        this.skillGaps = skillGaps;
    }

    public String getTargetCareer() { return targetCareer; }
    public void setTargetCareer(String targetCareer) { this.targetCareer = targetCareer; }

    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }

    public String getStageReason() { return stageReason; }
    public void setStageReason(String stageReason) { this.stageReason = stageReason; }

    public List<SkillGapItem> getSkillGaps() { return skillGaps; }
    public void setSkillGaps(List<SkillGapItem> skillGaps) { this.skillGaps = skillGaps; }
}
