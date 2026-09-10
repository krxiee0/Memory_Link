package com.memorylink.service;

import com.memorylink.model.Student;
import com.memorylink.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student register(Student student) {

        if (repository.findByUsername(student.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (repository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (repository.findByStudentId(student.getStudentId()).isPresent()) {
            throw new IllegalArgumentException("Student ID already exists.");
        }

        if (student.getRole() == null || student.getRole().isBlank()) {
            student.setRole("student");
        }

        return repository.save(student);
    }

    public Optional<Student> login(String username, String password) {
        return repository.findByUsername(username)
                .filter(student -> student.getPassword().equals(password));
    }

    public Optional<Student> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Student> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Student createAdmin(String username, String password) {

        Optional<Student> existing = repository.findByUsername(username);

        if (existing.isPresent()) {
            return existing.get();
        }

        Student admin = new Student();

        admin.setFullName("Administrator");
        admin.setStudentId("ADMIN");
        admin.setEmail(username + "@memorylink.local");
        admin.setCourse("Administration");
        admin.setYearLevel("N/A");
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole("admin");

        return repository.save(admin);
    }

    public boolean changePassword(
            String username,
            String oldPassword,
            String newPassword) {

        Optional<Student> optional = repository.findByUsername(username);

        if (optional.isEmpty()) {
            return false;
        }

        Student student = optional.get();

        if (!student.getPassword().equals(oldPassword)) {
            return false;
        }

        student.setPassword(newPassword);
        repository.save(student);

        return true;
    }

    // IMPORTANT:
    // StudentController.java expects this method.
    public String encodePassword(String password) {
        return password;
    }
}
