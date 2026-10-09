package com.pathforge.repository;

import com.pathforge.model.AssessmentAttempt;
import com.pathforge.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, Long> {
    List<AssessmentAttempt> findByStudentProfileOrderByCompletedAtDesc(StudentProfile studentProfile);
    List<AssessmentAttempt> findByStudentProfileIdOrderByCompletedAtDesc(Long studentProfileId);
}
