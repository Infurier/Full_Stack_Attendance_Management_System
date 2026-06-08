package com.example.attendance_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "classes")
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String department;
    private String course;
    private String subject;
    private String teacherId;
    private Integer capacity;

    // legacy/compatibility fields
    private String semester; // e.g., '2025-Fall' or 'Sem 1'
    private Integer semesterNumber;
    private Integer year;
    private String term;
    private String section;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public Integer getSemesterNumber() { return semesterNumber; }
    public void setSemesterNumber(Integer semesterNumber) { this.semesterNumber = semesterNumber; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
}
