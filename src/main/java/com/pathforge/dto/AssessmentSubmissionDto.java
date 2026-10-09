package com.pathforge.dto;

import java.util.Map;

public class AssessmentSubmissionDto {

    private Map<Long, String> answers; // QuestionId -> SelectedOption (A, B, C, D)

    public AssessmentSubmissionDto() {}

    public AssessmentSubmissionDto(Map<Long, String> answers) {
        this.answers = answers;
    }

    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> answers) { this.answers = answers; }
}
