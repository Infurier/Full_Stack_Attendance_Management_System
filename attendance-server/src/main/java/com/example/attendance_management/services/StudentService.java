package com.example.attendance_management.services;

import com.example.attendance_management.dto.StudentDto;
import com.example.attendance_management.exception.NotFoundException;
import com.example.attendance_management.model.Student;
import com.example.attendance_management.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository repo;
    private final TeacherService teacherService;
    private final com.example.attendance_management.repository.AttendanceRepository attendanceRepository;

    public StudentService(StudentRepository repo, TeacherService teacherService,
            com.example.attendance_management.repository.AttendanceRepository attendanceRepository) {
        this.repo = repo;
        this.teacherService = teacherService;
        this.attendanceRepository = attendanceRepository;
    }

    public StudentDto create(StudentDto dto) {
        Student s = new Student();
        s.setName(dto.getName());
        s.setRollNumber(dto.getRollNumber());
        s.setDepartment(dto.getDepartment());
        // Password handling: if client provides a password (deprecated) use it;
        // otherwise assign a sensible default so newly created students can login.
        // Default: use the roll number as the initial password (admins can change
        // later).
        if (dto.getRollNumber() != null && !dto.getRollNumber().isBlank()) {
            s.setPassword(dto.getRollNumber());
        } else {
            // fallback to empty password if roll number not provided
            s.setPassword("");
        }
        s.setCourse(dto.getCourse());
        s.setSubject(dto.getSubject());
        // set teacher association if provided in dto
        if (dto.getTeacherId() != null) {
            var teacher = teacherService.findEntityById(dto.getTeacherId());
            s.setTeacher(teacher);
        }
        Student saved = repo.save(s);
        return toDto(saved);
    }

    public StudentDto update(Long id, StudentDto dto) {
        Student existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Student not found"));
        existing.setName(dto.getName());
        existing.setRollNumber(dto.getRollNumber());
        existing.setDepartment(dto.getDepartment());
        // do not set password from DTO - password updates must go through a secure
        // endpoint
        existing.setCourse(dto.getCourse());
        existing.setSubject(dto.getSubject());
        // set teacher relationship if dto.teacherId provided
        if (dto.getTeacherId() != null) {
            var teacher = teacherService.findEntityById(dto.getTeacherId());
            existing.setTeacher(teacher);
        } else {
            // if client explicitly cleared teacherId (null), remove association
            existing.setTeacher(null);
        }
        Student saved = repo.save(existing);
        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        // remove attendance entries first to avoid foreign key constraint errors
        try {
            attendanceRepository.deleteByStudentId(id);
        } catch (Exception ex) {
            // if the delete fails for some reason, log or rethrow as needed - for now we
            // swallow to attempt student deletion
        }
        repo.deleteById(id);
    }

    public List<StudentDto> listAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public StudentDto getById(Long id) {
        Student s = repo.findById(id).orElseThrow(() -> new NotFoundException("Student not found"));
        return toDto(s);
    }

    public StudentDto getByRollNumber(String rollNumber) {
        Student s = repo.findByRollNumber(rollNumber).orElseThrow(() -> new NotFoundException("Student not found"));
        return toDto(s);
    }

    public Student findEntityById(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public Student findEntityByRollNumber(String rollNumber) {
        return repo.findByRollNumber(rollNumber).orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public Student findEntityByName(String name) {
        return repo.findByNameIgnoreCase(name).orElseThrow(() -> new NotFoundException("Student not found"));
    }

    /**
     * Change password for a student identified by roll number or name.
     * Returns true if password was changed successfully, false if current password
     * doesn't match.
     */
    @Transactional
    public boolean changePassword(String identifier, String currentPassword, String newPassword) {
        System.out.println("Changing password for identifier: " + identifier);

        // Try to find by roll number first, then by name
        Student student = repo.findByRollNumber(identifier)
                .orElseGet(() -> repo.findByNameIgnoreCase(identifier)
                        .orElseThrow(() -> new NotFoundException("Student not found")));

        System.out.println("Found student: " + student.getName() + ", Roll: " + student.getRollNumber());

        // Verify current password
        String storedPassword = student.getPassword();
        boolean isValid = false;

        System.out.println("Stored password: " + storedPassword);
        System.out.println("Provided current password: " + currentPassword);

        if (storedPassword != null && !storedPassword.isBlank()) {
            isValid = storedPassword.equals(currentPassword);
            System.out.println("Comparing with stored password. Match? " + isValid);
        } else {
            // If no password set, accept roll number or name as current password
            isValid = student.getRollNumber().equals(currentPassword)
                    || student.getName().equalsIgnoreCase(currentPassword);
            System.out.println("Checking against roll/name. Match? " + isValid);
        }

        if (!isValid) {
            System.out.println("Validation failed.");
            return false;
        }

        // Update password
        student.setPassword(newPassword);
        repo.save(student);
        System.out.println("Password updated successfully.");
        return true;
    }

    private StudentDto toDto(Student s) {
        Long teacherId = s.getTeacher() != null ? s.getTeacher().getId() : null;
        return new StudentDto(s.getId(), s.getName(), s.getRollNumber(), s.getDepartment(), s.getCourse(),
                s.getSubject(), teacherId);
    }
}
