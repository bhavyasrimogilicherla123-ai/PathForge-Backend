package com.pathforge.dto;

import java.util.List;

public class QuizSubmissionDto {

    public static class AnswerPair {
        private Integer questionIndex;
        private String selectedOption;
        private String correctOption;

        public AnswerPair() {}

        public AnswerPair(Integer questionIndex, String selectedOption, String correctOption) {
            this.questionIndex = questionIndex;
            this.selectedOption = selectedOption;
            this.correctOption = correctOption;
        }

        public Integer getQuestionIndex() { return questionIndex; }
        public void setQuestionIndex(Integer questionIndex) { this.questionIndex = questionIndex; }

        public String getSelectedOption() { return selectedOption; }
        public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }

        public String getCorrectOption() { return correctOption; }
        public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }
    }

    private Long skillId;
    private String skillName;
    private List<AnswerPair> userAnswers;

    public QuizSubmissionDto() {}

    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public List<AnswerPair> getUserAnswers() { return userAnswers; }
    public void setUserAnswers(List<AnswerPair> userAnswers) { this.userAnswers = userAnswers; }
}
