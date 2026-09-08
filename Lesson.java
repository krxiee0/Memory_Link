package com.memorylink.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="lessons")
public class Lesson {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String category;
    private String duration;
    @Column(length = 3000) private String description;
    private Integer numberOfLessons;
    private String status;
    private String submittedBy;

    @Lob @Column(columnDefinition="LONGTEXT")
    private String subLessonsJson;

    @Transient
    private List<Map<String,Object>> subLessons = new ArrayList<>();

    @PostLoad
    private void loadSubLessons() {
        subLessons = parseSubLessons(subLessonsJson);
    }

    @PrePersist @PreUpdate
    private void saveSubLessons() {
        if (subLessons != null) {
            try {
                subLessonsJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(subLessons);
            } catch (Exception ignored) {}
        }
    }

    private static List<Map<String,Object>> parseSubLessons(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json,
                new com.fasterxml.jackson.core.type.TypeReference<List<Map<String,Object>>>() {});
        } catch (Exception e) { return new ArrayList<>(); }
    }

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getDuration(){return duration;} public void setDuration(String v){duration=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public Integer getNumberOfLessons(){return numberOfLessons;} public void setNumberOfLessons(Integer v){numberOfLessons=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getSubmittedBy(){return submittedBy;} public void setSubmittedBy(String v){submittedBy=v;}
    public String getSubLessonsJson(){return subLessonsJson;} public void setSubLessonsJson(String v){subLessonsJson=v; subLessons=parseSubLessons(v);}
    public List<Map<String,Object>> getSubLessons(){return subLessons;}
    public void setSubLessons(List<Map<String,Object>> v){subLessons=v == null ? new ArrayList<>() : v;}
}
