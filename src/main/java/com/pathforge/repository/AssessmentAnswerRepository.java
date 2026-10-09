package com.pathforge.repository;

import com.pathforge.model.AssessmentAnswer;
import com.pathforge.model.AssessmentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentAnswerRepository extends JpaRepository<AssessmentAnswer, Long> {
    List<AssessmentAnswer> findByAttempt(AssessmentAttempt attempt);
}
