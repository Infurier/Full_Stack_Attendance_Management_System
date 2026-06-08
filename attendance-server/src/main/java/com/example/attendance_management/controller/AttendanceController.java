package com.example.attendance_management.controller;



import com.example.attendance_management.services.AttendanceService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


import com.example.attendance_management.dto.AttendanceDto;


import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService service;
    private final com.example.attendance_management.services.StudentService studentService;

    public AttendanceController(AttendanceService service, com.example.attendance_management.services.StudentService studentService) {
        this.service = service;
        this.studentService = studentService;
    }

    // mark / update attendance
    @PostMapping("/mark")
    public AttendanceDto mark(@RequestParam Long studentId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam boolean present,
                              @RequestParam(required = false) String reason) {
        return service.markAttendance(studentId, date, present, reason == null ? "" : reason);
    }

    @GetMapping
    public List<AttendanceDto> list(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date != null) {
            return service.getByDate(date);
        } else {
            return service.listAll();
        }
    }

    // attendance for a department (e.g., MCA)
    @GetMapping("/department/{department}")
    public List<AttendanceDto> byDepartment(@PathVariable String department, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date != null) return service.getByDepartmentAndDate(department, date);
        return service.getByDepartment(department);
    }

    // attendance for a specific course
    @GetMapping("/course/{course}")
    public List<AttendanceDto> byCourse(@PathVariable String course, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date != null) return service.getByCourseAndDate(course, date);
        return service.getByCourse(course);
    }

    // students can query attendance related to their degree via /api/attendance/my
    @GetMapping("/my")
    public List<AttendanceDto> my(org.springframework.security.core.Authentication authentication, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String username = authentication.getName(); // student's rollNumber
        return service.getForStudentDepartment(username, date, studentService);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
