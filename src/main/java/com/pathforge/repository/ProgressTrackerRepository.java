package com.pathforge.repository;

import com.pathforge.model.ProgressTracker;
import com.pathforge.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgressTrackerRepository extends JpaRepository<ProgressTracker, Long> {
    Optional<ProgressTracker> findByStudentProfile(StudentProfile studentProfile);
    Optional<ProgressTracker> findByStudentProfileId(Long studentProfileId);
}
