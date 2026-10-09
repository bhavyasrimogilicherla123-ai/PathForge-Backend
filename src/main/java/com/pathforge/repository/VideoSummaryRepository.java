package com.pathforge.repository;

import com.pathforge.model.StudentProfile;
import com.pathforge.model.VideoSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoSummaryRepository extends JpaRepository<VideoSummary, Long> {
    List<VideoSummary> findByStudentProfileOrderByCreatedAtDesc(StudentProfile studentProfile);
}
