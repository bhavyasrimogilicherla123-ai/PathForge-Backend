package com.pathforge.repository;

import com.pathforge.model.Project;
import com.pathforge.model.ProjectSkill;
import com.pathforge.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, Long> {
    List<ProjectSkill> findByProject(Project project);
    List<ProjectSkill> findBySkill(Skill skill);
}
