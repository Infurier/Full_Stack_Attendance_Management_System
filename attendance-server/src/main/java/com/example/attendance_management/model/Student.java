package com.example.attendance_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "roll_number", unique = true, nullable = false)
    private String rollNumber;
    @Column(name = "department")
    private String department;
    @Column(name = "course")
    private String course;

    @Column(name = "subject")
    private String subject;
    private String password;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
