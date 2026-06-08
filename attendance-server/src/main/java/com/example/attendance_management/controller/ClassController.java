package com.example.attendance_management.controller;

import com.example.attendance_management.model.ClassEntity;
import com.example.attendance_management.repository.ClassRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassRepository repository;

    public ClassController(ClassRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ClassEntity> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassEntity> get(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClassEntity> create(@RequestBody ClassEntity payload, Authentication auth) {
        // creation restricted to authenticated users; SecurityConfig enforces roles as needed
        ClassEntity saved = repository.save(payload);
        return ResponseEntity.created(URI.create("/api/classes/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassEntity> update(@PathVariable Long id, @RequestBody ClassEntity payload) {
        return repository.findById(id).map(existing -> {
            // copy allowed fields
            existing.setDepartment(payload.getDepartment());
            existing.setCourse(payload.getCourse());
            existing.setSubject(payload.getSubject());
            existing.setTeacherId(payload.getTeacherId());
            existing.setCapacity(payload.getCapacity());
            existing.setSemester(payload.getSemester());
            existing.setSemesterNumber(payload.getSemesterNumber());
            existing.setYear(payload.getYear());
            existing.setTerm(payload.getTerm());
            existing.setSection(payload.getSection());
            repository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
