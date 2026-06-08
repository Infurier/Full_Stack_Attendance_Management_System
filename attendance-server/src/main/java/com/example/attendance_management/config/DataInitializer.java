package com.example.attendance_management.config;

import com.example.attendance_management.model.Teacher;
import com.example.attendance_management.repository.TeacherRepository;
import com.example.attendance_management.model.Student;
import com.example.attendance_management.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public DataInitializer(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (teacherRepository.count() == 0) {
            Teacher t1 = new Teacher();
            t1.setTeacherId("T001");
            t1.setName("Dr. Smith");
            t1.setDepartment("Computer Science");
            t1.setSubject("Algorithms");

            Teacher t2 = new Teacher();
            t2.setTeacherId("T002");
            t2.setName("Dr. Jones");
            t2.setDepartment("Mathematics");
            t2.setSubject("Calculus");

            teacherRepository.saveAll(List.of(t1, t2));
        }
        if (studentRepository.count() == 0) {
            Student s1 = new Student();
            s1.setName("Alice");
            s1.setRollNumber("R001");
            s1.setDepartment("Computer Science");
            s1.setCourse("BSc CS");
            s1.setSubject("Algorithms");
            s1.setPassword("studentdemo");
            var teacher1 = teacherRepository.findByTeacherId("T001").orElse(null);
            s1.setTeacher(teacher1);

            Student s2 = new Student();
            s2.setName("Bob");
            s2.setRollNumber("R002");
            s2.setDepartment("Mathematics");
            s2.setCourse("BSc Math");
            s2.setSubject("Calculus");
            s2.setPassword("studentdemo");
            var teacher2 = teacherRepository.findByTeacherId("T002").orElse(null);
            s2.setTeacher(teacher2);

            studentRepository.saveAll(List.of(s1, s2));
        }
    }
}
