package com.example.attendance_management.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private String rollNumber;
    private String department;
    private String course;
    private String subject;
    private Long teacherId;
}
