package com.pathforge.repository;

import com.pathforge.model.LearningResource;
import com.pathforge.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, Long> {
    List<LearningResource> findBySkill(Skill skill);
    List<LearningResource> findBySkillId(Long skillId);
    List<LearningResource> findBySkillAndDifficulty(Skill skill, String difficulty);
}
