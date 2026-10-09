package com.pathforge.controller;

import com.pathforge.dto.AssessmentSubmissionDto;
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
@RequestMapping("/api/assessment")
@CrossOrigin(origins = "*")
public class AssessmentController {

    private final AssessmentQuestionRepository questionRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final AssessmentAnswerRepository answerRepository;
    private final StudentProfileRepository profileRepository;
    private final SkillScoringService skillScoringService;
    private final StageClassificationService stageClassificationService;
    private final RoadmapEngineService roadmapEngineService;

    public AssessmentController(AssessmentQuestionRepository questionRepository,
                                AssessmentAttemptRepository attemptRepository,
                                AssessmentAnswerRepository answerRepository,
                                StudentProfileRepository profileRepository,
                                SkillScoringService skillScoringService,
                                StageClassificationService stageClassificationService,
                                RoadmapEngineService roadmapEngineService) {
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
        this.answerRepository = answerRepository;
        this.profileRepository = profileRepository;
        this.skillScoringService = skillScoringService;
        this.stageClassificationService = stageClassificationService;
        this.roadmapEngineService = roadmapEngineService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<AssessmentQuestion>> getQuestions() {
        List<AssessmentQuestion> allQuestions = questionRepository.findAll();
        return ResponseEntity.ok(allQuestions);
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitAssessment(@AuthenticationPrincipal User user,
                                                                 @RequestBody AssessmentSubmissionDto submission) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        int totalScore = 0;
        int maxScore = 0;

        Map<Long, Integer> skillObtainedMap = new HashMap<>();
        Map<Long, Integer> skillMaxMap = new HashMap<>();
        Map<Long, Skill> skillRefMap = new HashMap<>();

        AssessmentAttempt attempt = new AssessmentAttempt(profile, 0, 0, 0.0);
        attempt = attemptRepository.save(attempt);

        if (submission.getAnswers() != null) {
            for (Map.Entry<Long, String> entry : submission.getAnswers().entrySet()) {
                Long questionId = entry.getKey();
                String selectedOption = entry.getValue();

                Optional<AssessmentQuestion> qOpt = questionRepository.findById(questionId);
                if (qOpt.isPresent()) {
                    AssessmentQuestion q = qOpt.get();
                    Skill skill = q.getSkill();
                    int marks = q.getMarks() != null ? q.getMarks() : 10;
                    maxScore += marks;

                    boolean isCorrect = q.getCorrectOption().equalsIgnoreCase(selectedOption);
                    int obtained = isCorrect ? marks : 0;
                    totalScore += obtained;

                    if (skill != null) {
                        skillObtainedMap.put(skill.getId(), skillObtainedMap.getOrDefault(skill.getId(), 0) + obtained);
                        skillMaxMap.put(skill.getId(), skillMaxMap.getOrDefault(skill.getId(), 0) + marks);
                        skillRefMap.put(skill.getId(), skill);
                    }

                    AssessmentAnswer answer = new AssessmentAnswer(attempt, q, selectedOption, isCorrect, obtained);
                    answerRepository.save(answer);
                }
            }
        }

        double overallPercentage = maxScore > 0 ? ((double) totalScore / maxScore) * 100.0 : 0.0;
        attempt.setTotalScore(totalScore);
        attempt.setMaxScore(maxScore);
        attempt.setPercentage(Math.round(overallPercentage * 10.0) / 10.0);
        attemptRepository.save(attempt);

        // Update Skill scores
        Map<String, Integer> updatedSkillScores = new HashMap<>();
        for (Long skillId : skillObtainedMap.keySet()) {
            Skill skill = skillRefMap.get(skillId);
            int obtained = skillObtainedMap.get(skillId);
            int max = skillMaxMap.get(skillId);
            int pct = max > 0 ? (int) Math.round(((double) obtained / max) * 100.0) : 0;

            skillScoringService.updateOrCreateStudentSkill(profile, skill, pct);
            updatedSkillScores.put(skill.getName(), pct);
        }

        // Re-evaluate Stage & Regenerate Roadmap
        stageClassificationService.evaluateAndUpdateStage(profile);
        profileRepository.save(profile);

        roadmapEngineService.generateNewRoadmap(profile, profile.getCareerGoal(),
                profile.getCareerGoal() != null ? profile.getCareerGoal().getTitle() : "Backend Developer");

        Map<String, Object> result = new HashMap<>();
        result.put("attemptId", attempt.getId());
        result.put("totalScore", totalScore);
        result.put("maxScore", maxScore);
        result.put("percentage", Math.round(overallPercentage * 10.0) / 10.0);
        result.put("currentStage", profile.getCurrentStage());
        result.put("stageReason", profile.getStageReason());
        result.put("updatedSkillScores", updatedSkillScores);

        return ResponseEntity.ok(result);
    }
}
