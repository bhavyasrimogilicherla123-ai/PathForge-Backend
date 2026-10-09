package com.pathforge.service;

import com.pathforge.model.StudentProfile;
import com.pathforge.model.StudentProject;
import com.pathforge.model.StudentSkill;
import com.pathforge.repository.StudentProjectRepository;
import com.pathforge.repository.StudentSkillRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StageClassificationService {

    private final StudentSkillRepository studentSkillRepository;
    private final StudentProjectRepository studentProjectRepository;

    public StageClassificationService(StudentSkillRepository studentSkillRepository,
                                       StudentProjectRepository studentProjectRepository) {
        this.studentSkillRepository = studentSkillRepository;
        this.studentProjectRepository = studentProjectRepository;
    }

    public void evaluateAndUpdateStage(StudentProfile profile) {
        List<StudentSkill> skills = studentSkillRepository.findByStudentProfile(profile);
        List<StudentProject> completedProjects = studentProjectRepository.findByStudentProfileAndStatus(profile, "COMPLETED");

        Map<String, Integer> scoreMap = new HashMap<>();
        for (StudentSkill ss : skills) {
            scoreMap.put(ss.getSkill().getName().toUpperCase(), ss.getScorePercentage());
        }

        int programmingMax = Math.max(scoreMap.getOrDefault("JAVA", 0),
                             Math.max(scoreMap.getOrDefault("PYTHON", 0),
                             Math.max(scoreMap.getOrDefault("C", 0), scoreMap.getOrDefault("JAVASCRIPT", 0))));

        int dbmsScore = Math.max(scoreMap.getOrDefault("DBMS", 0), scoreMap.getOrDefault("SQL", 0));
        int dsaScore = scoreMap.getOrDefault("DSA", 0);
        int gitScore = Math.max(scoreMap.getOrDefault("GIT", 0), scoreMap.getOrDefault("GITHUB", 0));
        int frameworkScore = Math.max(scoreMap.getOrDefault("SPRING BOOT", 0),
                             Math.max(scoreMap.getOrDefault("REST API", 0), scoreMap.getOrDefault("REACT", 0)));

        int projectCount = completedProjects.size();

        String stage;
        StringBuilder reason = new StringBuilder();

        if (programmingMax >= 65 && dbmsScore >= 60 && dsaScore >= 50 && gitScore >= 50 && frameworkScore >= 50 && projectCount >= 2) {
            stage = "Job Ready";
            reason.append("• Technical Fundamentals: Strong (").append(programmingMax).append("%)\n");
            reason.append("• Database & SQL: Good (").append(dbmsScore).append("%)\n");
            reason.append("• DSA & Problem Solving: ").append(dsaScore).append("%\n");
            reason.append("• Version Control (Git/GitHub): ").append(gitScore).append("%\n");
            reason.append("• Frameworks & APIs: ").append(frameworkScore).append("%\n");
            reason.append("• Completed Real-World Projects: ").append(projectCount);
        } else if (frameworkScore >= 40 || (programmingMax >= 50 && dbmsScore >= 50 && gitScore >= 30)) {
            stage = "Developer";
            reason.append("• Programming Fundamentals: ").append(programmingMax >= 60 ? "Good" : "Intermediate").append(" (").append(programmingMax).append("%)\n");
            reason.append("• Database & SQL: ").append(dbmsScore).append("%\n");
            reason.append("• Frameworks & APIs: ").append(frameworkScore).append("%\n");
            reason.append("• Version Control (Git): ").append(gitScore).append("%\n");
            reason.append("• Projects: ").append(projectCount);
        } else if (programmingMax >= 40 && (dbmsScore >= 40 || projectCount >= 1)) {
            stage = "Builder";
            reason.append("• Programming Fundamentals: Intermediate (").append(programmingMax).append("%)\n");
            reason.append("• DBMS/SQL: ").append(dbmsScore).append("%\n");
            reason.append("• Projects Completed: ").append(projectCount).append("\n");
            reason.append("• Git/GitHub: ").append(gitScore).append("% (Needs practice)");
        } else if (programmingMax >= 20 || dbmsScore >= 20) {
            stage = "Foundation";
            reason.append("• Programming Fundamentals: ").append(programmingMax > 30 ? "Good" : "Basic").append(" (").append(programmingMax).append("%)\n");
            reason.append("• DBMS: ").append(dbmsScore > 0 ? dbmsScore + "%" : "Beginner").append("\n");
            reason.append("• DSA: ").append(dsaScore > 0 ? dsaScore + "%" : "Beginner").append("\n");
            reason.append("• Projects: ").append(projectCount).append("\n");
            reason.append("• Git: ").append(gitScore > 0 ? gitScore + "%" : "Beginner");
        } else {
            stage = "Explorer";
            reason.append("• Currently exploring technical subjects.\n");
            reason.append("• Take diagnostic assessments to build your baseline skill score!");
        }

        profile.setCurrentStage(stage);
        profile.setStageReason(reason.toString());
    }
}
