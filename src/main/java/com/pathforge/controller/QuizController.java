package com.pathforge.controller;

import com.pathforge.dto.QuizSubmissionDto;
import com.pathforge.dto.QuizSubmissionDto.AnswerPair;
import com.pathforge.model.*;
import com.pathforge.repository.*;
import com.pathforge.service.RoadmapEngineService;
import com.pathforge.service.SkillScoringService;
import com.pathforge.service.StageClassificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizController {

    private final StudentProfileRepository profileRepository;
    private final SkillRepository skillRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final SkillScoringService skillScoringService;
    private final StageClassificationService stageClassificationService;
    private final RoadmapEngineService roadmapEngineService;

    public QuizController(StudentProfileRepository profileRepository,
                          SkillRepository skillRepository,
                          QuizAttemptRepository quizAttemptRepository,
                          SkillScoringService skillScoringService,
                          StageClassificationService stageClassificationService,
                          RoadmapEngineService roadmapEngineService) {
        this.profileRepository = profileRepository;
        this.skillRepository = skillRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.skillScoringService = skillScoringService;
        this.stageClassificationService = stageClassificationService;
        this.roadmapEngineService = roadmapEngineService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitQuiz(@AuthenticationPrincipal User user,
                                                          @RequestBody QuizSubmissionDto submission) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        int total = submission.getUserAnswers() != null ? submission.getUserAnswers().size() : 0;
        int correct = 0;

        if (submission.getUserAnswers() != null) {
            for (AnswerPair pair : submission.getUserAnswers()) {
                if (pair.getCorrectOption() != null && pair.getCorrectOption().equalsIgnoreCase(pair.getSelectedOption())) {
                    correct++;
                }
            }
        }

        double pct = total > 0 ? ((double) correct / total) * 100.0 : 0.0;

        Skill targetSkill = null;
        if (submission.getSkillId() != null) {
            targetSkill = skillRepository.findById(submission.getSkillId()).orElse(null);
        } else if (submission.getSkillName() != null) {
            targetSkill = skillRepository.findByNameIgnoreCase(submission.getSkillName()).orElse(null);
        } else {
            // Default to Spring Boot / REST API if from YouTube analyzer demo
            targetSkill = skillRepository.findByNameIgnoreCase("Spring Boot").orElse(null);
        }

        QuizAttempt attempt = new QuizAttempt(profile, targetSkill, total, correct, Math.round(pct * 10.0) / 10.0);
        quizAttemptRepository.save(attempt);

        // Update skill score for the learned skill!
        if (targetSkill != null) {
            int newScore = (int) Math.round(pct);
            skillScoringService.updateOrCreateStudentSkill(profile, targetSkill, newScore);
        }

        // Re-evaluate Stage & Updated Roadmap
        stageClassificationService.evaluateAndUpdateStage(profile);
        profileRepository.save(profile);

        roadmapEngineService.generateNewRoadmap(profile, profile.getCareerGoal(),
                profile.getCareerGoal() != null ? profile.getCareerGoal().getTitle() : "Backend Developer");

        Map<String, Object> result = new HashMap<>();
        result.put("totalQuestions", total);
        result.put("correctAnswers", correct);
        result.put("scorePercentage", Math.round(pct * 10.0) / 10.0);
        result.put("skillUpdated", targetSkill != null ? targetSkill.getName() : "General");
        result.put("currentStage", profile.getCurrentStage());
        result.put("stageReason", profile.getStageReason());

        return ResponseEntity.ok(result);
    }
}
