package com.memorylink.controller;

import com.memorylink.model.Student;
import com.memorylink.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final StudentService service;

    public AuthController(StudentService service) {
        this.service = service;
    }

    // =========================================================
    // REGISTER
    // =========================================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {

        try {

            if (student == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "message",
                                "Invalid registration data."
                        ));
            }

            if (student.getFullName() == null
                    || student.getStudentId() == null
                    || student.getEmail() == null
                    || student.getCourse() == null
                    || student.getYearLevel() == null
                    || student.getUsername() == null
                    || student.getPassword() == null) {

                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "message",
                                "Please complete all required fields."
                        ));
            }

            Student saved = service.register(student);

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Account created successfully!",
                            "id",
                            saved.getId()
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message",
                            "Registration failed. Please try again."
                    ));
        }
    }

    // =========================================================
    // LOGIN
    // =========================================================
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request) {

        try {

            String username = request.getOrDefault(
                    "username",
                    ""
            ).trim();

            String password = request.getOrDefault(
                    "password",
                    ""
            );

            if (username.isEmpty() || password.isEmpty()) {

                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "message",
                                "Please enter both username and password."
                        ));
            }

            return service.login(username, password)
                    .<ResponseEntity<?>>map(user ->
                            ResponseEntity.ok(
                                    Map.of(
                                            "message",
                                            "Login successful",
                                            "id",
                                            user.getId(),
                                            "username",
                                            user.getUsername(),
                                            "role",
                                            user.getRole(),
                                            "fullName",
                                            user.getFullName()
                                    )
                            )
                    )
                    .orElseGet(() ->
                            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body(
                                            Map.of(
                                                    "message",
                                                    "Invalid username or password."
                                            )
                                    )
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "message",
                                    "Login failed. Please try again."
                            )
                    );
        }
    }
}
