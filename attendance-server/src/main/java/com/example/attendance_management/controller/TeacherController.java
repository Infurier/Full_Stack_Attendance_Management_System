package com.example.attendance_management.controller;

import com.example.attendance_management.dto.TeacherDto;
import com.example.attendance_management.services.TeacherService;
import com.example.attendance_management.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService service;
    private final StudentRepository studentRepository;

    public TeacherController(TeacherService service, StudentRepository studentRepository) {
        this.service = service;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<TeacherDto> all() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public TeacherDto getById(@PathVariable Long id) {
        var t = service.findEntityById(id);
        return service.toDto(t);
    }

    @PostMapping
    public TeacherDto create(@RequestBody TeacherDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public TeacherDto update(@PathVariable Long id, @RequestBody TeacherDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        // service.delete needs StudentRepository to clear links, fetch from context
        // to keep controller simple, call delete with a repository bean
        // we'll obtain StudentRepository via constructor injection
        service.delete(id, studentRepository);
    }

    // simple lookup for teacher login by id
    @GetMapping("/by-teacher-id/{teacherId}")
    public TeacherDto getByTeacherId(@PathVariable String teacherId) {
        var t = service.findByTeacherId(teacherId);
        return service.toDto(t);
    }
}
