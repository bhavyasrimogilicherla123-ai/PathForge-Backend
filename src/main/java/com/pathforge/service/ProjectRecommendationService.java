package com.pathforge.service;

import com.pathforge.dto.ProjectRecommendationDto;
import com.pathforge.model.*;
import com.pathforge.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectRecommendationService {

    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final StudentProjectRepository studentProjectRepository;

    public ProjectRecommendationService(ProjectRepository projectRepository,
                                         ProjectSkillRepository projectSkillRepository,
                                         StudentProjectRepository studentProjectRepository) {
        this.projectRepository = projectRepository;
        this.projectSkillRepository = projectSkillRepository;
        this.studentProjectRepository = studentProjectRepository;
    }

    public List<ProjectRecommendationDto> getRecommendedProjects(StudentProfile profile) {
        CareerRole careerRole = profile.getCareerGoal();
        List<Project> allProjects = projectRepository.findAll();

        List<StudentProject> enrolledProjects = studentProjectRepository.findByStudentProfile(profile);
        Map<Long, StudentProject> enrolledMap = new HashMap<>();
        for (StudentProject sp : enrolledProjects) {
            enrolledMap.put(sp.getProject().getId(), sp);
        }

        List<ProjectRecommendationDto> dtos = new ArrayList<>();

        for (Project project : allProjects) {
            ProjectRecommendationDto dto = new ProjectRecommendationDto();
            dto.setId(project.getId());
            dto.setTitle(project.getTitle());
            dto.setDescription(project.getDescription());
            dto.setProblemStatement(project.getProblemStatement());
            dto.setDifficultyLevel(project.getDifficultyLevel());
            dto.setCareerRoleTitle(project.getCareerRole() != null ? project.getCareerRole().getTitle() : "Software Developer");

            // Fetch skills improved
            List<ProjectSkill> psList = projectSkillRepository.findByProject(project);
            List<String> skillNames = new ArrayList<>();
            for (ProjectSkill ps : psList) {
                skillNames.add(ps.getSkill().getName());
            }
            dto.setSkillsYouWillImprove(skillNames);

            // Generate "Why this project?" explanation
            String whyExplanation;
            if (project.getWhyThisProject() != null && !project.getWhyThisProject().isEmpty()) {
                whyExplanation = project.getWhyThisProject();
            } else {
                whyExplanation = "Because: You have foundational technical knowledge, but need practical implementation experience in " + 
                                 String.join(", ", skillNames) + " for " + dto.getCareerRoleTitle() + " roles.";
            }
            dto.setWhyThisProject(whyExplanation);

            if (enrolledMap.containsKey(project.getId())) {
                dto.setIsEnrolled(true);
                dto.setStudentProjectStatus(enrolledMap.get(project.getId()).getStatus());
            } else {
                dto.setIsEnrolled(false);
                dto.setStudentProjectStatus("NOT_ENROLLED");
            }

            dtos.add(dto);
        }

        // Sort: projects matching career role first, then beginner/intermediate
        dtos.sort((p1, p2) -> {
            boolean p1Matches = careerRole != null && p1.getCareerRoleTitle().equalsIgnoreCase(careerRole.getTitle());
            boolean p2Matches = careerRole != null && p2.getCareerRoleTitle().equalsIgnoreCase(careerRole.getTitle());
            if (p1Matches != p2Matches) return p1Matches ? -1 : 1;
            return 0;
        });

        return dtos;
    }
}
