package com.memorylink.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "student_id")
})
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name", nullable = false) private String fullName;
    @Column(name = "student_id", nullable = false) private String studentId;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String course;
    @Column(name = "year_level", nullable = false) private String yearLevel;
    @Column(nullable = false) private String username;
    @Column(nullable = false) private String password;
    @Column(nullable = false) private String role = "student";

    public Student() {}
    public Long getId(){return id;} public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getStudentId(){return studentId;} public void setStudentId(String v){studentId=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getCourse(){return course;} public void setCourse(String v){course=v;}
    public String getYearLevel(){return yearLevel;} public void setYearLevel(String v){yearLevel=v;}
    public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getRole(){return role;} public void setRole(String v){role=v;}
}
