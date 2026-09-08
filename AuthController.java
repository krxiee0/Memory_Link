package com.memorylink.controller;

import com.memorylink.model.Student;
import com.memorylink.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final StudentService service;
    public AuthController(StudentService service){this.service=service;}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student){
        try {
            if(student.getFullName()==null || student.getStudentId()==null || student.getEmail()==null || student.getCourse()==null || student.getYearLevel()==null || student.getUsername()==null || student.getPassword()==null)
                return ResponseEntity.badRequest().body(Map.of("message","Please complete all required fields."));
            if(student.getPassword().length()<6) return ResponseEntity.badRequest().body(Map.of("message","Password must be at least 6 characters."));
            Student saved=service.register(student);
            return ResponseEntity.ok(Map.of("message","Account created successfully!","id",saved.getId()));
        } catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(Map.of("message",e.getMessage())); }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> request){
        String username=request.getOrDefault("username","").trim();
        String password=request.getOrDefault("password","");
        return service.login(username,password)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(Map.of("message","Login successful","id",user.getId(),"username",user.getUsername(),"role",user.getRole(),"fullName",user.getFullName())))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("message","Invalid username or password.")));
    }
}
