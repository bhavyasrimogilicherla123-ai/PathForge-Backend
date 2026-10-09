package com.pathforge.repository;

import com.pathforge.model.Project;
import com.pathforge.model.StudentProfile;
import com.pathforge.model.StudentProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProjectRepository extends JpaRepository<StudentProject, Long> {
    List<StudentProject> findByStudentProfile(StudentProfile studentProfile);
    Optional<StudentProject> findByStudentProfileAndProject(StudentProfile studentProfile, Project project);
    List<StudentProject> findByStudentProfileAndStatus(StudentProfile studentProfile, String status);
}
