package com.example.attendance_management.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    private Long id;
    private String teacherId;
    private String name;
    private String department;
    private String subject;
    private Integer classesAssigned;
}
