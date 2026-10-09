package com.pathforge.repository;

import com.pathforge.model.CareerRole;
import com.pathforge.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCareerRole(CareerRole careerRole);
    List<Project> findByDifficultyLevel(String difficultyLevel);
}
