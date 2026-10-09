package com.pathforge.controller;

import com.pathforge.dto.ProgressDto;
import com.pathforge.model.StudentProfile;
import com.pathforge.model.User;
import com.pathforge.repository.StudentProfileRepository;
import com.pathforge.service.ProgressTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
public class ProgressController {

    private final ProgressTrackingService progressTrackingService;
    private final StudentProfileRepository profileRepository;

    public ProgressController(ProgressTrackingService progressTrackingService, StudentProfileRepository profileRepository) {
        this.progressTrackingService = progressTrackingService;
        this.profileRepository = profileRepository;
    }

    @GetMapping
    public ResponseEntity<ProgressDto> getProgress(@AuthenticationPrincipal User user) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        return ResponseEntity.ok(progressTrackingService.getStudentProgress(profile));
    }
}
