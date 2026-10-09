package com.pathforge.repository;

import com.pathforge.model.Skill;
import com.pathforge.model.StudentProfile;
import com.pathforge.model.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {
    List<StudentSkill> findByStudentProfile(StudentProfile studentProfile);
    List<StudentSkill> findByStudentProfileId(Long studentProfileId);
    Optional<StudentSkill> findByStudentProfileAndSkill(StudentProfile studentProfile, Skill skill);
}
