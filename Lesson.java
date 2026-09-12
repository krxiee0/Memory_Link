package com.memorylink.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String category;

    private String duration;

    @Column(length = 3000)
    private String description;

    private Integer numberOfLessons;

    private String status;

    private String submittedBy;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String subLessonsJson;

    @Transient
    private List<Map<String, Object>> subLessons =
            new ArrayList<>();


    public Lesson() {
    }


    @PostLoad
    private void loadSubLessons() {

        subLessons =
                parseSubLessons(subLessonsJson);
    }


    @PrePersist
    @PreUpdate
    private void saveSubLessons() {

        if (subLessons == null) {
            subLessons = new ArrayList<>();
        }

        try {

            subLessonsJson =
                    new com.fasterxml.jackson.databind.ObjectMapper()
                            .writeValueAsString(subLessons);

        } catch (Exception e) {

            subLessonsJson = "[]";
        }
    }


    private static List<Map<String, Object>>
    parseSubLessons(String json) {

        if (json == null || json.isBlank()) {

            return new ArrayList<>();
        }

        try {

            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(
                            json,
                            new com.fasterxml.jackson.core.type.TypeReference<
                                    List<Map<String, Object>>>() {}
                    );

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getNumberOfLessons() {
        return numberOfLessons;
    }

    public void setNumberOfLessons(Integer numberOfLessons) {
        this.numberOfLessons = numberOfLessons;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }


    public String getSubLessonsJson() {
        return subLessonsJson;
    }

    public void setSubLessonsJson(String subLessonsJson) {

        this.subLessonsJson = subLessonsJson;

        this.subLessons =
                parseSubLessons(subLessonsJson);
    }


    public List<Map<String, Object>> getSubLessons() {
        return subLessons;
    }

    public void setSubLessons(
            List<Map<String, Object>> subLessons) {

        this.subLessons =
                subLessons == null
                        ? new ArrayList<>()
                        : subLessons;
    }
}
