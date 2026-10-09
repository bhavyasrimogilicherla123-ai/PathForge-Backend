package com.pathforge.service;

import com.pathforge.model.*;
import com.pathforge.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AdminService {

    private final SkillRepository skillRepository;
    private final CareerRoleRepository careerRoleRepository;
    private final CareerRoleSkillRepository careerRoleSkillRepository;
    private final AssessmentQuestionRepository questionRepository;
    private final LearningResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final StudentProfileRepository studentProfileRepository;

    public AdminService(SkillRepository skillRepository,
                        CareerRoleRepository careerRoleRepository,
                        CareerRoleSkillRepository careerRoleSkillRepository,
                        AssessmentQuestionRepository questionRepository,
                        LearningResourceRepository resourceRepository,
                        ProjectRepository projectRepository,
                        ProjectSkillRepository projectSkillRepository,
                        StudentProfileRepository studentProfileRepository) {
        this.skillRepository = skillRepository;
        this.careerRoleRepository = careerRoleRepository;
        this.careerRoleSkillRepository = careerRoleSkillRepository;
        this.questionRepository = questionRepository;
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
        this.projectSkillRepository = projectSkillRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    public List<Skill> getAllSkills() { return skillRepository.findAll(); }

    @Transactional
    public Skill addOrUpdateSkill(Skill skill) { return skillRepository.save(skill); }

    public List<CareerRole> getAllCareerRoles() { return careerRoleRepository.findAll(); }

    @Transactional
    public CareerRole addOrUpdateCareerRole(CareerRole role) { return careerRoleRepository.save(role); }

    @Transactional
    public CareerRoleSkill mapSkillToCareer(Long careerRoleId, Long skillId, Integer requiredScore, Integer priorityOrder) {
        CareerRole careerRole = careerRoleRepository.findById(careerRoleId)
                .orElseThrow(() -> new RuntimeException("Career role not found"));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        CareerRoleSkill crs = new CareerRoleSkill(careerRole, skill, requiredScore, priorityOrder);
        return careerRoleSkillRepository.save(crs);
    }

    @Transactional
    public AssessmentQuestion addAssessmentQuestion(AssessmentQuestion question) { return questionRepository.save(question); }

    @Transactional
    public LearningResource addLearningResource(LearningResource resource) { return resourceRepository.save(resource); }

    @Transactional
    public Project addProject(Project project, List<Long> skillIds) {
        Project savedProject = projectRepository.save(project);
        if (skillIds != null) {
            for (Long skillId : skillIds) {
                skillRepository.findById(skillId).ifPresent(skill -> {
                    ProjectSkill ps = new ProjectSkill(savedProject, skill);
                    projectSkillRepository.save(ps);
                });
            }
        }
        return savedProject;
    }

    public Map<String, Object> getStudentStatistics() {
        long totalStudents = studentProfileRepository.count();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", totalStudents);
        stats.put("totalSkills", skillRepository.count());
        stats.put("totalCareers", careerRoleRepository.count());
        stats.put("totalQuestions", questionRepository.count());
        stats.put("totalProjects", projectRepository.count());
        return stats;
    }
}
