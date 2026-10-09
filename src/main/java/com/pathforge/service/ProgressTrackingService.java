package com.pathforge.service;

import com.pathforge.dto.ProgressDto;
import com.pathforge.model.*;
import com.pathforge.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProgressTrackingService {

    private final ProgressTrackerRepository progressTrackerRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final StudentProjectRepository studentProjectRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final StageClassificationService stageClassificationService;

    public ProgressTrackingService(ProgressTrackerRepository progressTrackerRepository,
                                   StudentSkillRepository studentSkillRepository,
                                   StudentProjectRepository studentProjectRepository,
                                   QuizAttemptRepository quizAttemptRepository,
                                   StageClassificationService stageClassificationService) {
        this.progressTrackerRepository = progressTrackerRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.studentProjectRepository = studentProjectRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.stageClassificationService = stageClassificationService;
    }

    @Transactional
    public ProgressDto getStudentProgress(StudentProfile profile) {
        ProgressTracker tracker = progressTrackerRepository.findByStudentProfile(profile)
                .orElseGet(() -> progressTrackerRepository.save(new ProgressTracker(profile)));

        // Recalculate metrics
        recalculateProgressMetrics(profile, tracker);

        ProgressDto dto = new ProgressDto();
        dto.setStudentName(profile.getUser().getName());
        dto.setCurrentStage(profile.getCurrentStage());
        dto.setStageReason(profile.getStageReason());
        dto.setTargetCareer(profile.getCareerGoal() != null ? profile.getCareerGoal().getTitle() : "Backend Developer");
        dto.setOverallProgress(tracker.getOverallProgress());
        dto.setTechnicalSkillsProgress(tracker.getTechnicalSkillsProgress());
        dto.setDsaProgress(tracker.getDsaProgress());
        dto.setProjectProgress(tracker.getProjectProgress());
        dto.setGitProgress(tracker.getGitProgress());
        dto.setInterviewPrepProgress(tracker.getInterviewPrepProgress());
        dto.setResumeCompleted(tracker.getResumeCompleted());

        List<StudentSkill> studentSkills = studentSkillRepository.findByStudentProfile(profile);
        Map<String, Integer> skillMap = new HashMap<>();
        for (StudentSkill ss : studentSkills) {
            skillMap.put(ss.getSkill().getName(), ss.getScorePercentage());
        }
        dto.setSkillBreakdown(skillMap);

        List<StudentProject> completedProjects = studentProjectRepository.findByStudentProfileAndStatus(profile, "COMPLETED");
        dto.setCompletedProjectsCount(completedProjects.size());

        List<QuizAttempt> quizzes = quizAttemptRepository.findByStudentProfileOrderByAttemptedAtDesc(profile);
        dto.setCompletedQuizzesCount(quizzes.size());

        return dto;
    }

    @Transactional
    public void recalculateProgressMetrics(StudentProfile profile, ProgressTracker tracker) {
        // Re-evaluate stage classification
        stageClassificationService.evaluateAndUpdateStage(profile);

        List<StudentSkill> skills = studentSkillRepository.findByStudentProfile(profile);

        Map<String, Integer> scoreMap = new HashMap<>();
        double totalTechSum = 0;
        int count = 0;

        for (StudentSkill ss : skills) {
            scoreMap.put(ss.getSkill().getName().toUpperCase(), ss.getScorePercentage());
            totalTechSum += ss.getScorePercentage();
            count++;
        }

        double avgTech = count > 0 ? totalTechSum / count : 0.0;
        double dsaScore = (double) scoreMap.getOrDefault("DSA", 0);
        double gitScore = (double) Math.max(scoreMap.getOrDefault("GIT", 0), scoreMap.getOrDefault("GITHUB", 0));

        List<StudentProject> completedProjects = studentProjectRepository.findByStudentProfileAndStatus(profile, "COMPLETED");
        double projectScore = Math.min(100.0, completedProjects.size() * 50.0); // 1 project = 50%, 2+ = 100%

        double interviewScore = Math.min(100.0, (avgTech + dsaScore) / 2.0);

        // Overall progress weighted composite: 35% Tech + 20% DSA + 25% Projects + 10% Git + 10% Interview Prep
        double overall = (avgTech * 0.35) + (dsaScore * 0.20) + (projectScore * 0.25) + (gitScore * 0.10) + (interviewScore * 0.10);

        tracker.setTechnicalSkillsProgress(Math.round(avgTech * 10.0) / 10.0);
        tracker.setDsaProgress(dsaScore);
        tracker.setProjectProgress(projectScore);
        tracker.setGitProgress(gitScore);
        tracker.setInterviewPrepProgress(Math.round(interviewScore * 10.0) / 10.0);
        tracker.setOverallProgress(Math.round(overall * 10.0) / 10.0);
        tracker.setResumeCompleted(completedProjects.size() >= 1);
        tracker.setLastUpdated(LocalDateTime.now());

        progressTrackerRepository.save(tracker);
    }
}
