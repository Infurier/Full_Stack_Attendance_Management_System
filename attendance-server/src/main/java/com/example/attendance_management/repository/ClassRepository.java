package com.example.attendance_management.repository;

import com.example.attendance_management.model.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByCourse(String course);
    long countByTeacherId(String teacherId);
}
