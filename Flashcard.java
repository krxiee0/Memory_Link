package com.memorylink.model;
import jakarta.persistence.*;
@Entity @Table(name="flashcards")
public class Flashcard {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String strand, difficulty;
 @Column(length=5000) private String question;
 @Column(length=5000) private String answer;
 private String lesson;
 private String lessonNumber;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public String getStrand(){return strand;} public void setStrand(String v){strand=v;}
 public String getDifficulty(){return difficulty;} public void setDifficulty(String v){difficulty=v;}
 public String getQuestion(){return question;} public void setQuestion(String v){question=v;}
 public String getAnswer(){return answer;} public void setAnswer(String v){answer=v;}
 public String getLesson(){return lesson;} public void setLesson(String v){lesson=v;}
 public String getLessonNumber(){return lessonNumber;} public void setLessonNumber(String v){lessonNumber=v;}
}
