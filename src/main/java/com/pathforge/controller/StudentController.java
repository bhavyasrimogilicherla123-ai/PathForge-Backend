package com.pathforge.controller;

import com.pathforge.dto.StudentProfileDto;
import com.pathforge.model.CareerRole;
import com.pathforge.model.Skill;
import com.pathforge.model.User;
import com.pathforge.repository.CareerRoleRepository;
import com.pathforge.repository.SkillRepository;
import com.pathforge.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;
    private final SkillRepository skillRepository;
    private final CareerRoleRepository careerRoleRepository;

    public StudentController(StudentService studentService, SkillRepository skillRepository, CareerRoleRepository careerRoleRepository) {
        this.studentService = studentService;
        this.skillRepository = skillRepository;
        this.careerRoleRepository = careerRoleRepository;
    }

    @GetMapping("/student/profile")
    public ResponseEntity<StudentProfileDto> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(studentService.getProfile(user));
    }

    @PutMapping("/student/profile")
    public ResponseEntity<StudentProfileDto> updateProfile(@AuthenticationPrincipal User user, @RequestBody StudentProfileDto dto) {
        return ResponseEntity.ok(studentService.updateProfile(user, dto));
    }

    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillRepository.findAll());
    }

    @GetMapping("/careers")
    public ResponseEntity<List<CareerRole>> getAllCareers() {
        return ResponseEntity.ok(careerRoleRepository.findAll());
    }
}
