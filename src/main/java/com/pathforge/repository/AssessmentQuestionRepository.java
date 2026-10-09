package com.pathforge.repository;

import com.pathforge.model.AssessmentQuestion;
import com.pathforge.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Long> {
    List<AssessmentQuestion> findBySkill(Skill skill);
    List<AssessmentQuestion> findBySkillId(Long skillId);
    List<AssessmentQuestion> findByCategory(String category);
    List<AssessmentQuestion> findByLevel(Integer level);
}
