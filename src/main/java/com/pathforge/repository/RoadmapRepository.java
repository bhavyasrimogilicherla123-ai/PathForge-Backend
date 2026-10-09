package com.pathforge.repository;

import com.pathforge.model.Roadmap;
import com.pathforge.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    List<Roadmap> findByStudentProfileOrderByGeneratedAtDesc(StudentProfile studentProfile);
    Optional<Roadmap> findFirstByStudentProfileAndStatusOrderByGeneratedAtDesc(StudentProfile studentProfile, String status);
}
