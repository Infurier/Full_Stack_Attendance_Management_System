package com.example.attendance_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teachers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", unique = true, nullable = false)
    private String teacherId;

    @Column(nullable = false)
    private String name;

    private String department;

    private String subject;
}
