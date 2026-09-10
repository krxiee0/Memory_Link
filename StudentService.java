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

        if (password.length() < 6) {
            throw new IllegalArgumentException(
                    "Password must be at least 6 characters."
            );
        }

        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(
                    "Username is already registered."
            );
        }

        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "Email is already registered."
            );
        }

        if (repository.findByStudentId(studentId).isPresent()) {
            throw new IllegalArgumentException(
                    "Student ID is already registered."
            );
        }

        student.setFullName(fullName);
        student.setStudentId(studentId);
        student.setEmail(email);
        student.setCourse(course);
        student.setYearLevel(yearLevel);
        student.setUsername(username);
        student.setPassword(password);

        // Normal registration = student
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
    // FIND BY ID
    // =========================================================
    public Optional<Student> findById(Long id) {
        return repository.findById(id);
    }

    // =========================================================
    // FIND BY USERNAME
    // =========================================================
    public Optional<Student> findByUsername(String username) {

        if (username == null) {
            return Optional.empty();
        }

        return repository.findByUsername(username.trim());
    }

    // =========================================================
    // CREATE ADMIN
    // =========================================================
    public Student createAdmin(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Admin username is required."
            );
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException(
                    "Admin password must be at least 6 characters."
            );
        }

        username = username.trim();

        Optional<Student> existing =
                repository.findByUsername(username);

        if (existing.isPresent()) {
            return existing.get();
        }

        Student admin = new Student();

        admin.setFullName("Administrator");
        admin.setStudentId("ADMIN");
        admin.setEmail(username + "@memorylink.local");
        admin.setCourse("Administration");
        admin.setYearLevel("Admin");
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole("admin");

        return repository.save(admin);
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================
    public boolean changePassword(
            String username,
            String oldPassword,
            String newPassword) {

        if (username == null
                || oldPassword == null
                || newPassword == null) {
            return false;
        }

        if (newPassword.length() < 6) {
            return false;
        }

        Optional<Student> result =
                repository.findByUsername(username.trim());

        if (result.isEmpty()) {
            return false;
        }

        Student student = result.get();

        if (!oldPassword.equals(student.getPassword())) {
            return false;
        }

        student.setPassword(newPassword);

        repository.save(student);

        return true;
    }
}
