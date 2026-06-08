package com.example.attendance_management.repository;


import com.example.attendance_management.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByDate(LocalDate date);
    Optional<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date);
    List<Attendance> findByStudentDepartment(String department);
    List<Attendance> findByStudentCourse(String course);
    List<Attendance> findByStudentDepartmentAndDate(String department, LocalDate date);
    List<Attendance> findByStudentCourseAndDate(String course, LocalDate date);
    // delete attendance records for a student (used when deleting a student)
    @Modifying
    @Transactional
    void deleteByStudentId(Long studentId);
}

