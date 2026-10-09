package com.pathforge.service;

import com.pathforge.dto.RoadmapResponseDto;
import com.pathforge.dto.RoadmapResponseDto.RoadmapPhaseDto;
import com.pathforge.dto.SkillGapResponseDto;
import com.pathforge.dto.SkillGapResponseDto.SkillGapItem;
import com.pathforge.model.*;
import com.pathforge.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoadmapEngineService {

    private final RoadmapRepository roadmapRepository;
    private final RoadmapItemRepository roadmapItemRepository;
    private final SkillGapAnalysisService skillGapAnalysisService;
    private final SkillRepository skillRepository;

    public RoadmapEngineService(RoadmapRepository roadmapRepository,
                                RoadmapItemRepository roadmapItemRepository,
                                SkillGapAnalysisService skillGapAnalysisService,
                                SkillRepository skillRepository) {
        this.roadmapRepository = roadmapRepository;
        this.roadmapItemRepository = roadmapItemRepository;
        this.skillGapAnalysisService = skillGapAnalysisService;
        this.skillRepository = skillRepository;
    }

    @Transactional
    public RoadmapResponseDto getOrGeneratePersonalizedRoadmap(StudentProfile profile) {
        CareerRole careerRole = profile.getCareerGoal();
        String careerTitle = careerRole != null ? careerRole.getTitle() : "Backend Developer";

        Optional<Roadmap> existingRoadmapOpt = roadmapRepository.findFirstByStudentProfileAndStatusOrderByGeneratedAtDesc(profile, "ACTIVE");

        Roadmap roadmap;
        if (existingRoadmapOpt.isPresent()) {
            roadmap = existingRoadmapOpt.get();
        } else {
            roadmap = generateNewRoadmap(profile, careerRole, careerTitle);
        }

        List<RoadmapItem> items = roadmapItemRepository.findByRoadmapOrderByPhaseNumberAscOrderIndexAsc(roadmap);

        List<RoadmapPhaseDto> phaseDtos = items.stream().map(item -> new RoadmapPhaseDto(
                item.getId(),
                item.getPhaseNumber(),
                item.getTitle(),
                item.getDescription(),
                item.getSkill() != null ? item.getSkill().getName() : "General CS",
                item.getStatus(),
                item.getWhyRecommended()
        )).collect(Collectors.toList());

        // Daily action tasks ("What should I do today?")
        List<RoadmapPhaseDto> todayTasks = phaseDtos.stream()
                .filter(p -> "IN_PROGRESS".equalsIgnoreCase(p.getStatus()) || "NOT_STARTED".equalsIgnoreCase(p.getStatus()))
                .limit(4)
                .collect(Collectors.toList());

        RoadmapResponseDto response = new RoadmapResponseDto();
        response.setRoadmapId(roadmap.getId());
        response.setTitle("Personalized " + careerTitle + " Learning & Development Roadmap");
        response.setTargetCareerTitle(careerTitle);
        response.setPhases(phaseDtos);
        response.setTodayTasks(todayTasks);

        return response;
    }

    @Transactional
    public Roadmap generateNewRoadmap(StudentProfile profile, CareerRole careerRole, String careerTitle) {
        // Archive existing active roadmaps
        List<Roadmap> oldRoadmaps = roadmapRepository.findByStudentProfileOrderByGeneratedAtDesc(profile);
        for (Roadmap r : oldRoadmaps) {
            r.setStatus("ARCHIVED");
            roadmapRepository.save(r);
        }

        Roadmap newRoadmap = new Roadmap(profile, "Personalized " + careerTitle + " Roadmap", careerRole);
        newRoadmap = roadmapRepository.save(newRoadmap);

        SkillGapResponseDto gapAnalysis = skillGapAnalysisService.analyzeSkillGaps(profile);
        List<SkillGapItem> gaps = gapAnalysis.getSkillGaps();

        int phaseNum = 1;

        for (SkillGapItem gap : gaps) {
            if ("Major Gap".equals(gap.getGapStatus()) || "Needs Improvement".equals(gap.getGapStatus())) {
                Skill skill = skillRepository.findByNameIgnoreCase(gap.getSkillName()).orElse(null);

                String whyNext;
                if ("Major Gap".equals(gap.getGapStatus())) {
                    whyNext = "Your target career is " + careerTitle + ". Your " + gap.getSkillName() + 
                              " score is currently " + gap.getCurrentScore() + "%, which is below the required " + 
                              gap.getRequiredScore() + "%. Mastering " + gap.getSkillName() + " is essential for phase " + phaseNum + ".";
                } else {
                    whyNext = "You have basic familiarity with " + gap.getSkillName() + " (" + gap.getCurrentScore() + 
                              "%), but target roles require " + gap.getRequiredScore() + "%. Strengthening this skill now unlocks practical project implementations.";
                }

                RoadmapItem item = new RoadmapItem(
                        newRoadmap,
                        phaseNum,
                        "Phase " + phaseNum + ": Master " + gap.getSkillName(),
                        "Complete lessons, practice problems, and take quizzes to reach " + gap.getRequiredScore() + "% proficiency in " + gap.getSkillName() + ".",
                        skill,
                        phaseNum,
                        whyNext
                );

                if (phaseNum == 1) {
                    item.setStatus("IN_PROGRESS");
                }
                roadmapItemRepository.save(item);
                phaseNum++;
            }
        }

        // Add Project phase
        RoadmapItem projectPhase = new RoadmapItem(
                newRoadmap,
                phaseNum,
                "Phase " + phaseNum + ": Build Real-World " + careerTitle + " Project",
                "Apply your acquired skills by implementing a production-grade major project with source code and GitHub documentation.",
                null,
                phaseNum,
                "Project experience synthesizes your theoretical knowledge and demonstrates job readiness to recruiters."
        );
        roadmapItemRepository.save(projectPhase);
        phaseNum++;

        // Add Interview Prep phase
        RoadmapItem interviewPhase = new RoadmapItem(
                newRoadmap,
                phaseNum,
                "Phase " + phaseNum + ": Technical Interview Preparation",
                "Practice core system design, DSA problem solving, and branch-specific interview question banks.",
                null,
                phaseNum,
                "Final preparation stage to ensure confidence in technical rounds for " + careerTitle + " positions."
        );
        roadmapItemRepository.save(interviewPhase);

        return newRoadmap;
    }
}
