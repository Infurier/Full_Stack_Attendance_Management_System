package com.example.attendance_management.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.attendance_management.dto.StudentDto;
import com.example.attendance_management.services.StudentService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<StudentDto> all() {
        return service.listAll();
    }

    @GetMapping("/me")
    public StudentDto me(org.springframework.security.core.Authentication authentication) {
        String username = authentication.getName(); // rollNumber for students
        return service.getByRollNumber(username);
    }

    @GetMapping("/{id}")
    public StudentDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public StudentDto create(@RequestBody StudentDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public StudentDto update(@PathVariable Long id, @RequestBody StudentDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Change password for the currently authenticated student
     */
    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(
            org.springframework.security.core.Authentication authentication,
            @RequestBody PasswordChangeRequest request) {
        String rollNumber = authentication.getName();
        System.out.println("=== Password Change Request ===");
        System.out.println("Authentication name (rollNumber): " + rollNumber);
        System.out.println("Current password provided: " + request.getCurrentPassword());
        System.out.println("New password provided: " + request.getNewPassword());
        boolean success = service.changePassword(rollNumber, request.getCurrentPassword(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok().body(java.util.Map.of("message", "Password changed successfully"));
        } else {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "Current password is incorrect"));
        }
    }

    /**
     * DTO for password change request
     */
    public static class PasswordChangeRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
