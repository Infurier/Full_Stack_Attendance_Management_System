package com.example.attendance_management.controller;
import com.example.attendance_management.dto.*;
import com.example.attendance_management.security.JwtUtil;
import com.example.attendance_management.services.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TeacherService teacherService;
    private final com.example.attendance_management.services.StudentService studentService;
    public AuthController(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil, TeacherService teacherService, com.example.attendance_management.services.StudentService studentService) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.jwtUtil = jwtUtil;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        List<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String token = jwtUtil.generateToken(req.getUsername(), roles);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // Simple teacher login by teacherId (demo). In production use proper credential checks.
    @GetMapping("/login/teacher/{teacherId}")
    public ResponseEntity<?> loginTeacher(@PathVariable String teacherId) {
        try {
            var t = teacherService.findByTeacherId(teacherId);
            // generate token with role TEACHER and subject info as needed
            String token = jwtUtil.generateToken(t.getTeacherId(), List.of("ROLE_TEACHER"));
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception ex) {
            return ResponseEntity.status(404).body("Teacher not found");
        }
    }

    // Simple student login by roll number (demo). In production use proper credential checks.
    @GetMapping("/login/student/{rollNumber}")
    public ResponseEntity<?> loginStudent(@PathVariable String rollNumber) {
        try {
            var s = studentService.findEntityByRollNumber(rollNumber);
            // generate token with ROLE_STUDENT; subject is rollNumber
            String token = jwtUtil.generateToken(s.getRollNumber(), List.of("ROLE_STUDENT"));
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception ex) {
            return ResponseEntity.status(404).body("Student not found");
        }
    }

    @PostMapping("/login/student")
    public ResponseEntity<?> loginStudentPassword(@RequestBody AuthRequest req) {
        try {
            var s = studentService.findEntityByRollNumber(req.getUsername());
            if (s.getPassword() != null && s.getPassword().equals(req.getPassword())) {
                String token = jwtUtil.generateToken(s.getRollNumber(), List.of("ROLE_STUDENT"));
                return ResponseEntity.ok(new AuthResponse(token));
            } else {
                return ResponseEntity.status(401).body("Invalid student credentials");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body("Student not found");
        }
    }

    /**
     * Allow student login by student name (case-insensitive).
     * This endpoint will check stored password first; if no password is set
     * it will accept login when provided password equals the student's name
     * (case-insensitive) — useful for quick onboarding/testing. In production,
     * consider removing the name-as-password fallback or protecting it behind a flag.
     */
    @PostMapping("/login/student/name")
    public ResponseEntity<?> loginStudentByName(@RequestBody AuthRequest req) {
        try {
            var s = studentService.findEntityByName(req.getUsername());
            String provided = req.getPassword() == null ? "" : req.getPassword();
            // prefer stored password if present
            if (s.getPassword() != null && !s.getPassword().isBlank()) {
                if (s.getPassword().equals(provided)) {
                    String token = jwtUtil.generateToken(s.getRollNumber(), List.of("ROLE_STUDENT"));
                    return ResponseEntity.ok(new AuthResponse(token));
                } else {
                    return ResponseEntity.status(401).body("Invalid student credentials");
                }
            }
            // fallback: accept when provided password equals student name (case-insensitive)
            if (provided != null && provided.equalsIgnoreCase(s.getName())) {
                String token = jwtUtil.generateToken(s.getRollNumber(), List.of("ROLE_STUDENT"));
                return ResponseEntity.ok(new AuthResponse(token));
            }
            return ResponseEntity.status(401).body("Invalid student credentials");
        } catch (Exception ex) {
            return ResponseEntity.status(404).body("Student not found");
        }
    }
}
