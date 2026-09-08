package com.memorylink.service;

import com.memorylink.model.Student;
import com.memorylink.repository.StudentRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public StudentService(StudentRepository repository){this.repository=repository;}

    public Student register(Student s){
        if(repository.existsByUsername(s.getUsername())) throw new IllegalArgumentException("Username is already taken.");
        if(repository.existsByEmail(s.getEmail())) throw new IllegalArgumentException("Email is already registered.");
        if(repository.existsByStudentId(s.getStudentId())) throw new IllegalArgumentException("Student ID is already registered.");
        s.setPassword(encoder.encode(s.getPassword()));
        s.setRole("student");
        return repository.save(s);
    }
    public Optional<Student> login(String username, String password){
        return repository.findByUsername(username)
                .filter(user -> encoder.matches(password, user.getPassword()));
    }
    public String encodePassword(String password){ return encoder.encode(password); }

    public boolean changePassword(String username, String currentPassword, String newPassword){
        return repository.findByUsername(username).map(user -> {
            if(!encoder.matches(currentPassword, user.getPassword())) return false;
            user.setPassword(encoder.encode(newPassword));
            repository.save(user);
            return true;
        }).orElse(false);
    }

    public boolean hasUser(String username){return repository.existsByUsername(username);}
    public void createAdmin(String username,String password){
        if(!repository.existsByUsername(username)){
            Student a=new Student(); a.setFullName("MemoryLink Administrator"); a.setStudentId("ADMIN-001");
            a.setEmail("admin@memorylink.local"); a.setCourse("Administration"); a.setYearLevel("N/A");
            a.setUsername(username); a.setPassword(encoder.encode(password)); a.setRole("admin"); repository.save(a);
        }
    }
}
