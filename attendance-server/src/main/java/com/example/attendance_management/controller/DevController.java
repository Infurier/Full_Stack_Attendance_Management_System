package com.example.attendance_management.controller;

import com.example.attendance_management.model.Student;
import com.example.attendance_management.model.Teacher;
import com.example.attendance_management.repository.StudentRepository;
import com.example.attendance_management.repository.TeacherRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dev")
public class DevController {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public DevController(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    // Dev-only helper: populate or fix demo students (ids/rolls from data.sql / DataInitializer)
    @PostMapping("/fix-students")
    public ResponseEntity<?> fixStudents(Authentication authentication) {
        // basic auth check: require ADMIN role
        if (authentication == null || authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        List<Student> updated = new ArrayList<>();

        // Ensure teacher references exist
        Teacher t1 = teacherRepository.findByTeacherId("T001").orElse(null);
        Teacher t2 = teacherRepository.findByTeacherId("T002").orElse(null);

        // Alice (R001)
        Student alice = studentRepository.findByRollNumber("R001").orElseGet(() -> new Student());
        alice.setName("Alice");
        alice.setRollNumber("R001");
        alice.setDepartment("Computer Science");
        alice.setCourse("BSc CS");
        alice.setSubject("Algorithms");
        alice.setPassword("alicepass");
        alice.setTeacher(t1);
        updated.add(alice);

        // Bob (R002)
        Student bob = studentRepository.findByRollNumber("R002").orElseGet(() -> new Student());
        bob.setName("Bob");
        bob.setRollNumber("R002");
        bob.setDepartment("Mathematics");
        bob.setCourse("BSc Math");
        bob.setSubject("Calculus");
        bob.setPassword("bobpass");
        bob.setTeacher(t2);
        updated.add(bob);

        studentRepository.saveAll(updated);

        return ResponseEntity.ok("Students fixed/updated: " + updated.size());
    }
}
