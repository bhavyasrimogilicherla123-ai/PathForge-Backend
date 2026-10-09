package com.pathforge;

import com.pathforge.dto.RoadmapResponseDto;
import com.pathforge.dto.SkillGapResponseDto;
import com.pathforge.model.CareerRole;
import com.pathforge.model.StudentProfile;
import com.pathforge.model.User;
import com.pathforge.repository.CareerRoleRepository;
import com.pathforge.repository.StudentProfileRepository;
import com.pathforge.repository.UserRepository;
import com.pathforge.service.RoadmapEngineService;
import com.pathforge.service.SkillGapAnalysisService;
import com.pathforge.service.StageClassificationService;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
class PathForgeApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private CareerRoleRepository careerRoleRepository;

    @Autowired
    private SkillGapAnalysisService skillGapAnalysisService;

    @Autowired
    private StageClassificationService stageClassificationService;

    @Autowired
    private RoadmapEngineService roadmapEngineService;

    @Test
    void testDemoStudentSeededAndEvaluated() {
        User demoUser = userRepository.findByEmail("demo@pathforge.com").orElse(null);
        assertNotNull(demoUser, "Demo student account should exist");

        StudentProfile profile = studentProfileRepository.findByUser(demoUser).orElse(null);
        assertNotNull(profile, "Demo student profile should exist");
        assertEquals("CSE", profile.getBranch());
        assertEquals("3rd Year", profile.getYear());

        // Test Stage Evaluation
        stageClassificationService.evaluateAndUpdateStage(profile);
        assertNotNull(profile.getCurrentStage());
        assertTrue(Arrays.asList("Explorer", "Foundation", "Builder", "Developer", "Job Ready").contains(profile.getCurrentStage()),
                "Stage should be classified into a valid PathForge stage");

        // Test Skill Gap Analysis
        SkillGapResponseDto gaps = skillGapAnalysisService.analyzeSkillGaps(profile);
        assertNotNull(gaps);
        assertEquals("Backend Developer", gaps.getTargetCareer());
        assertFalse(gaps.getSkillGaps().isEmpty(), "Skill gaps should be identified against Backend Developer requirements");

        // Test Roadmap Generation
        RoadmapResponseDto roadmap = roadmapEngineService.getOrGeneratePersonalizedRoadmap(profile);
        assertNotNull(roadmap);
        assertFalse(roadmap.getPhases().isEmpty(), "Personalized roadmap phases should be generated");
    }
}
