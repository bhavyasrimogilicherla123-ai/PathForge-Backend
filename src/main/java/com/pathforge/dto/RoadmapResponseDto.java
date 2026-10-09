package com.pathforge.dto;

import java.util.List;

public class RoadmapResponseDto {

    public static class RoadmapPhaseDto {
        private Long itemId;
        private Integer phaseNumber;
        private String title;
        private String description;
        private String skillName;
        private String status;
        private String whyRecommended;

        public RoadmapPhaseDto() {}

        public RoadmapPhaseDto(Long itemId, Integer phaseNumber, String title, String description, String skillName, String status, String whyRecommended) {
            this.itemId = itemId;
            this.phaseNumber = phaseNumber;
            this.title = title;
            this.description = description;
            this.skillName = skillName;
            this.status = status;
            this.whyRecommended = whyRecommended;
        }

        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }

        public Integer getPhaseNumber() { return phaseNumber; }
        public void setPhaseNumber(Integer phaseNumber) { this.phaseNumber = phaseNumber; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getWhyRecommended() { return whyRecommended; }
        public void setWhyRecommended(String whyRecommended) { this.whyRecommended = whyRecommended; }
    }

    private Long roadmapId;
    private String title;
    private String targetCareerTitle;
    private List<RoadmapPhaseDto> phases;
    private List<RoadmapPhaseDto> todayTasks;

    public RoadmapResponseDto() {}

    public Long getRoadmapId() { return roadmapId; }
    public void setRoadmapId(Long roadmapId) { this.roadmapId = roadmapId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTargetCareerTitle() { return targetCareerTitle; }
    public void setTargetCareerTitle(String targetCareerTitle) { this.targetCareerTitle = targetCareerTitle; }

    public List<RoadmapPhaseDto> getPhases() { return phases; }
    public void setPhases(List<RoadmapPhaseDto> phases) { this.phases = phases; }

    public List<RoadmapPhaseDto> getTodayTasks() { return todayTasks; }
    public void setTodayTasks(List<RoadmapPhaseDto> todayTasks) { this.todayTasks = todayTasks; }
}
