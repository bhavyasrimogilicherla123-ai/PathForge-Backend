package com.pathforge.controller;

import com.pathforge.dto.YoutubeAnalysisDto;
import com.pathforge.model.StudentProfile;
import com.pathforge.model.User;
import com.pathforge.repository.StudentProfileRepository;
import com.pathforge.service.YoutubeAnalyzerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/youtube")
@CrossOrigin(origins = "*")
public class YoutubeController {

    private final YoutubeAnalyzerService youtubeAnalyzerService;
    private final StudentProfileRepository profileRepository;

    public YoutubeController(YoutubeAnalyzerService youtubeAnalyzerService, StudentProfileRepository profileRepository) {
        this.youtubeAnalyzerService = youtubeAnalyzerService;
        this.profileRepository = profileRepository;
    }

    @PostMapping("/analyze")
    public ResponseEntity<YoutubeAnalysisDto> analyzeVideo(@AuthenticationPrincipal User user,
                                                            @RequestBody Map<String, String> body) {
        StudentProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        String videoUrl = body.get("videoUrl");
        return ResponseEntity.ok(youtubeAnalyzerService.analyzeVideoUrl(profile, videoUrl));
    }
}
