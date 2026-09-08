package com.memorylink.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String strand;

    @Column(length = 3000)
    private String description;

    private String lesson;
    private String lessonNumber;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String questionsJson;

    @Transient
    @JsonIgnore
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Quiz() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStrand() { return strand; }
    public void setStrand(String strand) { this.strand = strand; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLesson() { return lesson; }
    public void setLesson(String lesson) { this.lesson = lesson; }
    public String getLessonNumber() { return lessonNumber; }
    public void setLessonNumber(String lessonNumber) { this.lessonNumber = lessonNumber; }
    public String getQuestionsJson() { return questionsJson; }
    public void setQuestionsJson(String questionsJson) { this.questionsJson = questionsJson; }

    @JsonProperty("questions")
    public List<Map<String, Object>> getQuestions() {
        if (questionsJson == null || questionsJson.isBlank()) return new ArrayList<>();
        try {
            return OBJECT_MAPPER.readValue(questionsJson, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @JsonProperty("questions")
    public void setQuestions(List<Map<String, Object>> questions) {
        try {
            this.questionsJson = OBJECT_MAPPER.writeValueAsString(
                    questions == null ? new ArrayList<>() : questions
            );
        } catch (Exception e) {
            this.questionsJson = "[]";
        }
    }
}
