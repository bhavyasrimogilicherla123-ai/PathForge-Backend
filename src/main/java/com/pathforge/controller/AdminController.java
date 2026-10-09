package com.pathforge.controller;

import com.pathforge.model.*;
import com.pathforge.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(adminService.getStudentStatistics());
    }

    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getSkills() {
        return ResponseEntity.ok(adminService.getAllSkills());
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) {
        return ResponseEntity.ok(adminService.addOrUpdateSkill(skill));
    }

    @GetMapping("/careers")
    public ResponseEntity<List<CareerRole>> getCareers() {
        return ResponseEntity.ok(adminService.getAllCareerRoles());
    }

    @PostMapping("/careers")
    public ResponseEntity<CareerRole> createCareerRole(@RequestBody CareerRole role) {
        return ResponseEntity.ok(adminService.addOrUpdateCareerRole(role));
    }

    @PostMapping("/careers/{careerId}/map-skill")
    public ResponseEntity<CareerRoleSkill> mapSkill(@PathVariable Long careerId,
                                                      @RequestParam Long skillId,
                                                      @RequestParam Integer requiredScore,
                                                      @RequestParam(defaultValue = "1") Integer priorityOrder) {
        return ResponseEntity.ok(adminService.mapSkillToCareer(careerId, skillId, requiredScore, priorityOrder));
    }

    @PostMapping("/questions")
    public ResponseEntity<AssessmentQuestion> addQuestion(@RequestBody AssessmentQuestion question) {
        return ResponseEntity.ok(adminService.addAssessmentQuestion(question));
    }

    @PostMapping("/resources")
    public ResponseEntity<LearningResource> addResource(@RequestBody LearningResource resource) {
        return ResponseEntity.ok(adminService.addLearningResource(resource));
    }

    @PostMapping("/projects")
    public ResponseEntity<Project> addProject(@RequestBody Project project,
                                                @RequestParam(required = false) List<Long> skillIds) {
        return ResponseEntity.ok(adminService.addProject(project, skillIds));
    }
}
