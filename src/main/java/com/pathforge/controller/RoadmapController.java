package com.pathforge.controller;

import com.pathforge.dto.RoadmapResponseDto;
import com.pathforge.model.StudentProfile;
import com.pathforge.model.User;
import com.pathforge.repository.StudentProfileRepository;
import com.pathforge.service.RoadmapEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roadmap")
@CrossOrigin(origins = "*")
public class RoadmapController {

    private final RoadmapEngineService roadmapEngineService;
    private final StudentProfileRepository profileRepository;

    public RoadmapController(RoadmapEngineService roadmapEngineService, StudentProfileRepository profileRepository) {
        this.roadmapEngineService = roadmapEngineService;
        this.profileRepository = profileRepository;
    }

    @GetMapping
    public ResponseEntity<RoadmapResponseDto> getRoadmap(@AuthenticationPrincipal User user) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        return ResponseEntity.ok(roadmapEngineService.getOrGeneratePersonalizedRoadmap(profile));
    }

    @GetMapping("/today")
    public ResponseEntity<Object> getTodayTasks(@AuthenticationPrincipal User user) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        RoadmapResponseDto roadmap = roadmapEngineService.getOrGeneratePersonalizedRoadmap(profile);
        return ResponseEntity.ok(roadmap.getTodayTasks());
    }
}
