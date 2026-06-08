package com.example.attendance_management.services;


import com.example.attendance_management.model.Attendance;
import com.example.attendance_management.model.Student;
import com.example.attendance_management.repository.AttendanceRepository;
import com.example.attendance_management.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;


import com.example.attendance_management.dto.AttendanceDto;
import com.example.attendance_management.exception.NotFoundException;

import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    public AttendanceDto markAttendance(Long studentId, LocalDate date, boolean present, String reason) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));
        // avoid duplicate for same date+student: update existing if present
        Attendance attendance = attendanceRepository.findByStudentIdAndDate(studentId, date)
                .orElseGet(() -> {
                    Attendance a = new Attendance();
                    a.setStudent(student);
                    a.setDate(date);
                    return a;
                });
        attendance.setPresent(present);
        attendance.setReason(reason);
        Attendance saved = attendanceRepository.save(attendance);
        return toDto(saved);
    }

    public List<AttendanceDto> getByDate(LocalDate date) {
        return attendanceRepository.findByDate(date).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AttendanceDto> getByDepartment(String department) {
        return attendanceRepository.findByStudentDepartment(department).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AttendanceDto> getByDepartmentAndDate(String department, LocalDate date) {
        return attendanceRepository.findByStudentDepartmentAndDate(department, date).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AttendanceDto> getByCourse(String course) {
        return attendanceRepository.findByStudentCourse(course).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AttendanceDto> getByCourseAndDate(String course, LocalDate date) {
        return attendanceRepository.findByStudentCourseAndDate(course, date).stream().map(this::toDto).collect(Collectors.toList());
    }

    // convenience: attendance for a student's department (found by roll number)
    public List<AttendanceDto> getForStudentDepartment(String rollNumber, LocalDate date, StudentService studentService) {
        Student s = studentService.findEntityByRollNumber(rollNumber);
        String dept = s.getDepartment();
        if (date != null) return getByDepartmentAndDate(dept, date);
        return getByDepartment(dept);
    }

    public List<AttendanceDto> listAll() {
        return attendanceRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }

    private AttendanceDto toDto(Attendance a) {
        return new AttendanceDto(a.getId(), a.getStudent().getId(), a.getStudent().getName(), a.getDate(), a.isPresent(), a.getReason());
    }
}
