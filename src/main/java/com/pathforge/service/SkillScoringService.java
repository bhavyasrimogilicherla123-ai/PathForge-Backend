package com.pathforge.service;

import com.pathforge.model.Skill;
import com.pathforge.model.StudentProfile;
import com.pathforge.model.StudentSkill;
import com.pathforge.repository.StudentSkillRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkillScoringService {

    private final StudentSkillRepository studentSkillRepository;

    public SkillScoringService(StudentSkillRepository studentSkillRepository) {
        this.studentSkillRepository = studentSkillRepository;
    }

    public String getLevelName(Integer percentage) {
        if (percentage == null || percentage <= 20) return "Beginner";
        if (percentage <= 40) return "Basic";
        if (percentage <= 60) return "Intermediate";
        if (percentage <= 80) return "Good";
        return "Strong";
    }

    public Map<String, Object> getStudentSkillSummary(StudentProfile profile) {
        List<StudentSkill> skills = studentSkillRepository.findByStudentProfile(profile);
        Map<String, Object> result = new HashMap<>();

        Map<String, Integer> scoreMap = new HashMap<>();
        Map<String, String> levelMap = new HashMap<>();

        for (StudentSkill ss : skills) {
            scoreMap.put(ss.getSkill().getName(), ss.getScorePercentage());
            levelMap.put(ss.getSkill().getName(), ss.getLevelName());
        }

        result.put("scores", scoreMap);
        result.put("levels", levelMap);
        return result;
    }

    public StudentSkill updateOrCreateStudentSkill(StudentProfile profile, Skill skill, Integer newScore) {
        StudentSkill ss = studentSkillRepository.findByStudentProfileAndSkill(profile, skill)
                .orElse(new StudentSkill(profile, skill, 0));
        
        // Take highest or weighted progress
        ss.setScorePercentage(Math.max(ss.getScorePercentage(), Math.min(100, newScore)));
        return studentSkillRepository.save(ss);
    }
}
