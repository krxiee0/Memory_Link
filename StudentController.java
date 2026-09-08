package com.memorylink.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.memorylink.model.Student;
import com.memorylink.repository.StudentRepository;
import com.memorylink.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository repository;
    private final StudentService service;

    public StudentController(
            StudentRepository repository,
            StudentService service
    ) {
        this.repository = repository;
        this.service = service;
    }

    // =========================
    // GET ALL STUDENTS
    // =========================
    @GetMapping
    public List<Student> all() {
        return repository.findAll();
    }

    // =========================
    // GET STUDENT BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Student> one(@PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // UPDATE STUDENT
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Student incoming
    ) {

        Optional<Student> found = repository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = found.get();

        if (incoming.getFullName() != null) {
            student.setFullName(incoming.getFullName());
        }

        if (incoming.getStudentId() != null) {
            student.setStudentId(incoming.getStudentId());
        }

        if (incoming.getEmail() != null) {
            student.setEmail(incoming.getEmail());
        }

        if (incoming.getCourse() != null) {
            student.setCourse(incoming.getCourse());
        }

        if (incoming.getYearLevel() != null) {
            student.setYearLevel(incoming.getYearLevel());
        }

        if (incoming.getUsername() != null) {
            student.setUsername(incoming.getUsername());
        }

        if (incoming.getRole() != null) {
            student.setRole(incoming.getRole());
        }

        // Update password only when a new password is supplied
        if (incoming.getPassword() != null
                && !incoming.getPassword().isBlank()) {

            student.setPassword(
                    service.encodePassword(
                            incoming.getPassword()
                    )
            );
        }

        return ResponseEntity.ok(
                repository.save(student)
        );
    }

    // =========================
    // DELETE STUDENT
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    // =========================
    // SYNC STUDENTS
    // =========================
    @PutMapping("/sync")
    public List<Student> sync(
            @RequestBody List<Student> data
    ) {

        for (Student incoming : data) {

            // Skip invalid records
            if (incoming.getUsername() == null
                    || incoming.getPassword() == null) {

                continue;
            }

            Optional<Student> existing =
                    repository.findByUsername(
                            incoming.getUsername()
                    );

            if (existing.isPresent()) {

                // =========================
                // UPDATE EXISTING STUDENT
                // =========================

                Student student = existing.get();

                if (incoming.getFullName() != null) {
                    student.setFullName(
                            incoming.getFullName()
                    );
                }

                if (incoming.getStudentId() != null) {
                    student.setStudentId(
                            incoming.getStudentId()
                    );
                }

                if (incoming.getEmail() != null) {
                    student.setEmail(
                            incoming.getEmail()
                    );
                }

                if (incoming.getCourse() != null) {
                    student.setCourse(
                            incoming.getCourse()
                    );
                }

                if (incoming.getYearLevel() != null) {
                    student.setYearLevel(
                            incoming.getYearLevel()
                    );
                }

                if (incoming.getRole() != null) {
                    student.setRole(
                            incoming.getRole()
                    );
                } else {
                    student.setRole("student");
                }

                // Only encode if password isn't already BCrypt
                if (!isBCryptPassword(
                        incoming.getPassword()
                )) {

                    student.setPassword(
                            service.encodePassword(
                                    incoming.getPassword()
                            )
                    );
                }

                repository.save(student);

            } else {

                // =========================
                // CREATE NEW STUDENT
                // =========================

                /*
                 * IMPORTANT:
                 *
                 * Do NOT use:
                 *
                 * incoming.setId(null);
                 *
                 * Student.java already uses:
                 *
                 * @GeneratedValue(
                 *     strategy = GenerationType.IDENTITY
                 * )
                 *
                 * Therefore MySQL will automatically
                 * generate the ID.
                 */

                if (!isBCryptPassword(
                        incoming.getPassword()
                )) {

                    incoming.setPassword(
                            service.encodePassword(
                                    incoming.getPassword()
                            )
                    );
                }

                if (incoming.getRole() == null) {
                    incoming.setRole("student");
                }

                repository.save(incoming);
            }
        }

        return repository.findAll();
    }

    // =========================
    // CHANGE PASSWORD
    // =========================
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, String> request
    ) {

        String username =
                request.getOrDefault(
                        "username",
                        ""
                ).trim();

        String currentPassword =
                request.getOrDefault(
                        "currentPassword",
                        ""
                );

        String newPassword =
                request.getOrDefault(
                        "newPassword",
                        ""
                );

        if (username.isBlank()) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Username is required."
                            )
                    );
        }

        if (newPassword.length() < 6) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Password must be at least 6 characters."
                            )
                    );
        }

        boolean changed =
                service.changePassword(
                        username,
                        currentPassword,
                        newPassword
                );

        if (changed) {

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Password updated successfully!"
                    )
            );
        }

        return ResponseEntity
                .status(401)
                .body(
                        Map.of(
                                "message",
                                "Current password is incorrect."
                        )
                );
    }

    // =========================
    // CHECK BCrypt PASSWORD
    // =========================
    private boolean isBCryptPassword(
            String password
    ) {

        if (password == null) {
            return false;
        }

        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }
}