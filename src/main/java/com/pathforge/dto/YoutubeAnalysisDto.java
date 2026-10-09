package com.pathforge.dto;

import java.util.List;
import java.util.Map;

public class YoutubeAnalysisDto {

    public static class QuizQuestionDto {
        private String question;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String correctOption;
        private String explanation;

        public QuizQuestionDto() {}

        public QuizQuestionDto(String question, String optionA, String optionB, String optionC, String optionD, String correctOption, String explanation) {
            this.question = question;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.correctOption = correctOption;
            this.explanation = explanation;
        }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public String getOptionA() { return optionA; }
        public void setOptionA(String optionA) { this.optionA = optionA; }

        public String getOptionB() { return optionB; }
        public void setOptionB(String optionB) { this.optionB = optionB; }

        public String getOptionC() { return optionC; }
        public void setOptionC(String optionC) { this.optionC = optionC; }

        public String getOptionD() { return optionD; }
        public void setOptionD(String optionD) { this.optionD = optionD; }

        public String getCorrectOption() { return correctOption; }
        public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }

    private String videoUrl;
    private String title;
    private String duration;
    private String channel;
    private String summary;
    private List<String> importantConcepts;
    private String beginnerExplanation;
    private List<String> practicalExamples;
    private List<String> importantPoints;
    private List<QuizQuestionDto> generatedQuiz;
    private List<String> interviewQuestions;
    private List<String> mappedSkills;
    private Boolean transcriptAvailable = true;
    private String message;

    public YoutubeAnalysisDto() {}

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<String> getImportantConcepts() { return importantConcepts; }
    public void setImportantConcepts(List<String> importantConcepts) { this.importantConcepts = importantConcepts; }

    public String getBeginnerExplanation() { return beginnerExplanation; }
    public void setBeginnerExplanation(String beginnerExplanation) { this.beginnerExplanation = beginnerExplanation; }

    public List<String> getPracticalExamples() { return practicalExamples; }
    public void setPracticalExamples(List<String> practicalExamples) { this.practicalExamples = practicalExamples; }

    public List<String> getImportantPoints() { return importantPoints; }
    public void setImportantPoints(List<String> importantPoints) { this.importantPoints = importantPoints; }

    public List<QuizQuestionDto> getGeneratedQuiz() { return generatedQuiz; }
    public void setGeneratedQuiz(List<QuizQuestionDto> generatedQuiz) { this.generatedQuiz = generatedQuiz; }

    public List<String> getInterviewQuestions() { return interviewQuestions; }
    public void setInterviewQuestions(List<String> interviewQuestions) { this.interviewQuestions = interviewQuestions; }

    public List<String> getMappedSkills() { return mappedSkills; }
    public void setMappedSkills(List<String> mappedSkills) { this.mappedSkills = mappedSkills; }

    public Boolean getTranscriptAvailable() { return transcriptAvailable; }
    public void setTranscriptAvailable(Boolean transcriptAvailable) { this.transcriptAvailable = transcriptAvailable; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
