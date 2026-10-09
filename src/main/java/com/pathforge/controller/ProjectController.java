package com.pathforge.controller;

import com.pathforge.dto.ProjectRecommendationDto;
import com.pathforge.model.*;
import com.pathforge.repository.*;
import com.pathforge.service.ProjectRecommendationService;
import com.pathforge.service.SkillScoringService;
import com.pathforge.service.StageClassificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectRecommendationService projectRecommendationService;
    private final StudentProfileRepository profileRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final StudentProjectRepository studentProjectRepository;
    private final SkillScoringService skillScoringService;
    private final StageClassificationService stageClassificationService;

    public ProjectController(ProjectRecommendationService projectRecommendationService,
                             StudentProfileRepository profileRepository,
                             ProjectRepository projectRepository,
                             ProjectSkillRepository projectSkillRepository,
                             StudentProjectRepository studentProjectRepository,
                             SkillScoringService skillScoringService,
                             StageClassificationService stageClassificationService) {
        this.projectRecommendationService = projectRecommendationService;
        this.profileRepository = profileRepository;
        this.projectRepository = projectRepository;
        this.projectSkillRepository = projectSkillRepository;
        this.studentProjectRepository = studentProjectRepository;
        this.skillScoringService = skillScoringService;
        this.stageClassificationService = stageClassificationService;
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<ProjectRecommendationDto>> getRecommendedProjects(@AuthenticationPrincipal User user) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        return ResponseEntity.ok(projectRecommendationService.getRecommendedProjects(profile));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> startProject(@AuthenticationPrincipal User user, @PathVariable Long id) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        StudentProject sp = studentProjectRepository.findByStudentProfileAndProject(profile, project)
                .orElse(new StudentProject(profile, project));

        sp.setStatus("IN_PROGRESS");
        studentProjectRepository.save(sp);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Successfully enrolled in project: " + project.getTitle());
        result.put("status", sp.getStatus());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeProject(@AuthenticationPrincipal User user,
                                                                @PathVariable Long id,
                                                                @RequestBody(required = false) Map<String, String> body) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        StudentProject sp = studentProjectRepository.findByStudentProfileAndProject(profile, project)
                .orElse(new StudentProject(profile, project));

        sp.setStatus("COMPLETED");
        if (body != null && body.containsKey("githubLink")) {
            sp.setGithubLink(body.get("githubLink"));
        }
        sp.setCompletedAt(LocalDateTime.now());
        studentProjectRepository.save(sp);

        // Boost scores for project skills by +25%
        List<ProjectSkill> projectSkills = projectSkillRepository.findByProject(project);
        List<String> boostedSkills = new ArrayList<>();

        for (ProjectSkill ps : projectSkills) {
            Skill skill = ps.getSkill();
            skillScoringService.updateOrCreateStudentSkill(profile, skill, 65); // boost to at least 65% (Good level)
            boostedSkills.add(skill.getName());
        }

        // Re-evaluate stage classification (Requirement #19 continuous loop)
        stageClassificationService.evaluateAndUpdateStage(profile);
        profileRepository.save(profile);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Congratulations! Project completed successfully: " + project.getTitle());
        result.put("status", sp.getStatus());
        result.put("currentStage", profile.getCurrentStage());
        result.put("stageReason", profile.getStageReason());
        result.put("boostedSkills", boostedSkills);

        return ResponseEntity.ok(result);
    }
}
