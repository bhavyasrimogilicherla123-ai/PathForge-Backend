package com.pathforge.service;

import com.pathforge.dto.SkillGapResponseDto;
import com.pathforge.dto.SkillGapResponseDto.SkillGapItem;
import com.pathforge.model.*;
import com.pathforge.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkillGapAnalysisService {

    private final StudentSkillRepository studentSkillRepository;
    private final CareerRoleSkillRepository careerRoleSkillRepository;
    private final SkillScoringService skillScoringService;

    public SkillGapAnalysisService(StudentSkillRepository studentSkillRepository,
                                  CareerRoleSkillRepository careerRoleSkillRepository,
                                  SkillScoringService skillScoringService) {
        this.studentSkillRepository = studentSkillRepository;
        this.careerRoleSkillRepository = careerRoleSkillRepository;
        this.skillScoringService = skillScoringService;
    }

    public SkillGapResponseDto analyzeSkillGaps(StudentProfile profile) {
        CareerRole targetRole = profile.getCareerGoal();
        String targetTitle = targetRole != null ? targetRole.getTitle() : "Backend Developer";

        List<CareerRoleSkill> requiredSkills = targetRole != null 
                ? careerRoleSkillRepository.findByCareerRole(targetRole)
                : Collections.emptyList();

        List<StudentSkill> currentStudentSkills = studentSkillRepository.findByStudentProfile(profile);
        Map<Long, StudentSkill> studentSkillMap = new HashMap<>();
        for (StudentSkill ss : currentStudentSkills) {
            studentSkillMap.put(ss.getSkill().getId(), ss);
        }

        List<SkillGapItem> gapItems = new ArrayList<>();

        for (CareerRoleSkill crs : requiredSkills) {
            Skill skill = crs.getSkill();
            int requiredScore = crs.getRequiredScorePercentage();

            StudentSkill studentSkill = studentSkillMap.get(skill.getId());
            int currentScore = studentSkill != null ? studentSkill.getScorePercentage() : 0;
            String currentLevel = skillScoringService.getLevelName(currentScore);

            String gapStatus;
            String recommendation;

            if (currentScore == 0 || (currentScore < requiredScore * 0.35)) {
                gapStatus = "Major Gap";
                recommendation = "High priority: Begin foundational learning for " + skill.getName() + ".";
            } else if (currentScore < requiredScore) {
                gapStatus = "Needs Improvement";
                recommendation = "Medium priority: Practice and complete quizzes to reach target score (" + requiredScore + "%).";
            } else if (currentScore < requiredScore * 1.25) {
                gapStatus = "Sufficient";
                recommendation = "Requirement met. Maintain proficiency through real-world project usage.";
            } else {
                gapStatus = "Strong";
                recommendation = "Exceeds requirement. Ready for advanced interview preparation in " + skill.getName() + ".";
            }

            gapItems.add(new SkillGapItem(skill.getName(), skill.getCategory(), currentScore, requiredScore, currentLevel, gapStatus, recommendation));
        }

        return new SkillGapResponseDto(targetTitle, profile.getCurrentStage(), profile.getStageReason(), gapItems);
    }
}
