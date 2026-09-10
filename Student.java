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

    // =========================================================
    // REGISTER
    // =========================================================
    public Student register(Student student) {

        if (student == null) {
            throw new IllegalArgumentException("Invalid student data.");
        }

        // Clean values
        String fullName = student.getFullName() == null
                ? ""
                : student.getFullName().trim();

        String studentId = student.getStudentId() == null
                ? ""
                : student.getStudentId().trim();

        String email = student.getEmail() == null
                ? ""
                : student.getEmail().trim().toLowerCase();

        String course = student.getCourse() == null
                ? ""
                : student.getCourse().trim();

        String yearLevel = student.getYearLevel() == null
                ? ""
                : student.getYearLevel().trim();

        String username = student.getUsername() == null
                ? ""
                : student.getUsername().trim();

        String password = student.getPassword() == null
                ? ""
                : student.getPassword();

        // Required fields
        if (fullName.isEmpty()
                || studentId.isEmpty()
                || email.isEmpty()
                || course.isEmpty()
                || yearLevel.isEmpty()
                || username.isEmpty()
                || password.isEmpty()) {

            throw new IllegalArgumentException(
                    "Please complete all required fields."
            );
        }

        // Password
        if (password.length() < 6) {
            throw new IllegalArgumentException(
                    "Password must be at least 6 characters."
            );
        }

        // Username duplicate
        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(
                    "Username is already registered."
            );
        }

        // Email duplicate
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "Email is already registered."
            );
        }

        // Student ID duplicate
        if (repository.findByStudentId(studentId).isPresent()) {
            throw new IllegalArgumentException(
                    "Student ID is already registered."
            );
        }

        // Set cleaned values
        student.setFullName(fullName);
        student.setStudentId(studentId);
        student.setEmail(email);
        student.setCourse(course);
        student.setYearLevel(yearLevel);
        student.setUsername(username);
        student.setPassword(password);

        // ALWAYS student for normal registration
        student.setRole("student");

        return repository.save(student);
    }

    // =========================================================
    // LOGIN
    // =========================================================
    public Optional<Student> login(String username, String password) {

        if (username == null || password == null) {
            return Optional.empty();
        }

        username = username.trim();

        if (username.isEmpty() || password.isEmpty()) {
            return Optional.empty();
        }

        Optional<Student> result =
                repository.findByUsername(username);

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Student student = result.get();

        if (student.getPassword() == null) {
            return Optional.empty();
        }

        if (!student.getPassword().equals(password)) {
            return Optional.empty();
        }

        return Optional.of(student);
    }

    // =========================================================
    // FIND STUDENT
    // =========================================================
    public Optional<Student> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Student> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }

        return repository.findByUsername(username.trim());
    }
}
