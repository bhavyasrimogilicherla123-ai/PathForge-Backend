package com.pathforge.service;

import com.pathforge.dto.StudentProfileDto;
import com.pathforge.model.*;
import com.pathforge.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentProfileRepository studentProfileRepository;
    private final CareerRoleRepository careerRoleRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final StudentProjectRepository studentProjectRepository;
    private final StageClassificationService stageClassificationService;

    public StudentService(StudentProfileRepository studentProfileRepository,
                          CareerRoleRepository careerRoleRepository,
                          SkillRepository skillRepository,
                          StudentSkillRepository studentSkillRepository,
                          StudentProjectRepository studentProjectRepository,
                          StageClassificationService stageClassificationService) {
        this.studentProfileRepository = studentProfileRepository;
        this.careerRoleRepository = careerRoleRepository;
        this.skillRepository = skillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.studentProjectRepository = studentProjectRepository;
        this.stageClassificationService = stageClassificationService;
    }

    public StudentProfileDto getProfile(User user) {
        StudentProfile profile = studentProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        return convertToDto(profile);
    }

    @Transactional
    public StudentProfileDto updateProfile(User user, StudentProfileDto dto) {
        StudentProfile profile = studentProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        if (dto.getBranch() != null) profile.setBranch(dto.getBranch());
        if (dto.getYear() != null) profile.setYear(dto.getYear());
        if (dto.getSemester() != null) profile.setSemester(dto.getSemester());
        if (dto.getInterests() != null) profile.setInterests(dto.getInterests());

        if (dto.getCareerGoalId() != null) {
            CareerRole careerRole = careerRoleRepository.findById(dto.getCareerGoalId()).orElse(null);
            if (careerRole != null) {
                profile.setCareerGoal(careerRole);
            }
        }

        // Update self-reported skills if provided
        if (dto.getSelfReportedSkills() != null && !dto.getSelfReportedSkills().isEmpty()) {
            for (Map.Entry<String, Integer> entry : dto.getSelfReportedSkills().entrySet()) {
                String skillName = entry.getKey();
                Integer score = entry.getValue();
                Optional<Skill> skillOpt = skillRepository.findByNameIgnoreCase(skillName);
                if (skillOpt.isPresent()) {
                    Skill skill = skillOpt.get();
                    StudentSkill ss = studentSkillRepository.findByStudentProfileAndSkill(profile, skill)
                            .orElse(new StudentSkill(profile, skill, 0));
                    ss.setScorePercentage(score);
                    studentSkillRepository.save(ss);
                }
            }
        }

        // Re-evaluate stage classification
        stageClassificationService.evaluateAndUpdateStage(profile);
        profile = studentProfileRepository.save(profile);

        return convertToDto(profile);
    }

    private StudentProfileDto convertToDto(StudentProfile profile) {
        StudentProfileDto dto = new StudentProfileDto();
        dto.setId(profile.getId());
        dto.setName(profile.getUser().getName());
        dto.setEmail(profile.getUser().getEmail());
        dto.setBranch(profile.getBranch());
        dto.setYear(profile.getYear());
        dto.setSemester(profile.getSemester());
        dto.setInterests(profile.getInterests());
        dto.setCurrentStage(profile.getCurrentStage());
        dto.setStageReason(profile.getStageReason());

        if (profile.getCareerGoal() != null) {
            dto.setCareerGoalId(profile.getCareerGoal().getId());
            dto.setCareerGoalTitle(profile.getCareerGoal().getTitle());
        }

        List<StudentSkill> studentSkills = studentSkillRepository.findByStudentProfile(profile);
        Map<String, Integer> skillsMap = new HashMap<>();
        for (StudentSkill ss : studentSkills) {
            skillsMap.put(ss.getSkill().getName(), ss.getScorePercentage());
        }
        dto.setSelfReportedSkills(skillsMap);

        List<StudentProject> completedProjects = studentProjectRepository.findByStudentProfileAndStatus(profile, "COMPLETED");
        dto.setCompletedProjects(completedProjects.stream().map(sp -> sp.getProject().getTitle()).collect(Collectors.toList()));

        return dto;
    }
}
