package com.pathforge.repository;

import com.pathforge.model.QuizAttempt;
import com.pathforge.model.Skill;
import com.pathforge.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByStudentProfileOrderByAttemptedAtDesc(StudentProfile studentProfile);
    List<QuizAttempt> findByStudentProfileAndSkill(StudentProfile studentProfile, Skill skill);
}
