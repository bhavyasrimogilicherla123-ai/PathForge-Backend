package com.pathforge.controller;

import com.pathforge.dto.SkillGapResponseDto;
import com.pathforge.model.StudentProfile;
import com.pathforge.model.User;
import com.pathforge.repository.StudentProfileRepository;
import com.pathforge.service.SkillGapAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillGapController {

    private final SkillGapAnalysisService skillGapAnalysisService;
    private final StudentProfileRepository profileRepository;

    public SkillGapController(SkillGapAnalysisService skillGapAnalysisService, StudentProfileRepository profileRepository) {
        this.skillGapAnalysisService = skillGapAnalysisService;
        this.profileRepository = profileRepository;
    }

    @GetMapping("/gaps")
    public ResponseEntity<SkillGapResponseDto> getSkillGaps(@AuthenticationPrincipal User user) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        return ResponseEntity.ok(skillGapAnalysisService.analyzeSkillGaps(profile));
    }
}
