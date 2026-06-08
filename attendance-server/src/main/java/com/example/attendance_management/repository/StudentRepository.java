package com.example.attendance_management.repository;


import com.example.attendance_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRollNumber(String rollNumber);
    Optional<Student> findByNameIgnoreCase(String name);

    @Modifying
    @Transactional
    @Query("update Student s set s.teacher = null where s.teacher.id = :teacherId")
    void clearTeacherFromStudents(Long teacherId);
}

