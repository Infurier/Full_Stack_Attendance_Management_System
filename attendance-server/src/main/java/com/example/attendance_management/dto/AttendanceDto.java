package com.example.attendance_management.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private LocalDate date;
    private boolean present;
    private String reason;
}