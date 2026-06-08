package com.example.attendance_management.services;

import com.example.attendance_management.dto.TeacherDto;
import com.example.attendance_management.exception.NotFoundException;
import com.example.attendance_management.model.Teacher;
import com.example.attendance_management.repository.TeacherRepository;
import com.example.attendance_management.repository.ClassRepository;
import com.example.attendance_management.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    private final TeacherRepository repo;
    private final ClassRepository classRepository;

    public TeacherService(TeacherRepository repo, ClassRepository classRepository) {
        this.repo = repo;
        this.classRepository = classRepository;
    }

    public TeacherDto create(TeacherDto dto) {
        Teacher t = new Teacher();
        t.setTeacherId(dto.getTeacherId());
        t.setName(dto.getName());
        t.setDepartment(dto.getDepartment());
        t.setSubject(dto.getSubject());
        Teacher saved = repo.save(t);
        return toDto(saved);
    }

    public TeacherDto update(Long id, TeacherDto dto) {
        Teacher t = findEntityById(id);
        t.setTeacherId(dto.getTeacherId());
        t.setName(dto.getName());
        t.setDepartment(dto.getDepartment());
        t.setSubject(dto.getSubject());
        Teacher saved = repo.save(t);
        return toDto(saved);
    }

    public void delete(Long id, StudentRepository studentRepository) {
        // clear teacher link from students to avoid FK issues
        studentRepository.clearTeacherFromStudents(id);
        repo.deleteById(id);
    }

    public List<TeacherDto> listAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Teacher findByTeacherId(String teacherId) {
        return repo.findByTeacherId(teacherId).orElseThrow(() -> new NotFoundException("Teacher not found"));
    }

    public Teacher findEntityById(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Teacher not found"));
    }

    // convert to DTO including computed fields like classesAssigned
    public TeacherDto toDto(Teacher t) {
        int count = 0;
        try {
            long c1 = 0, c2 = 0;
            if (t.getTeacherId() != null && !t.getTeacherId().isBlank()) {
                c1 = classRepository.countByTeacherId(t.getTeacherId());
            }
            if (t.getId() != null) {
                c2 = classRepository.countByTeacherId(String.valueOf(t.getId()));
            }
            // sum both possibilities (some data may store numeric id or external teacherId)
            count = (int) (c1 + c2);
        } catch (Exception e) {
            // on error, keep count as 0
        }
        return new TeacherDto(t.getId(), t.getTeacherId(), t.getName(), t.getDepartment(), t.getSubject(), count);
    }
}
