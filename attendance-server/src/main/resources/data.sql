INSERT INTO teachers (id, teacher_id, name, department, subject) VALUES (1, 'T001', 'Dr. Smith', 'Computer Science', 'Algorithms');
INSERT INTO teachers (id, teacher_id, name, department, subject) VALUES (2, 'T002', 'Dr. Jones', 'Mathematics', 'Calculus');

-- include course and subject for students; teacher_id stays as FK
-- set password for seeded students to a common demo password for testing
INSERT INTO students (id, name, roll_number, department, course, subject, teacher_id, password) VALUES (1, 'Alice', 'R001', 'Computer Science', 'BSc CS', 'Algorithms', 1, 'studentdemo');
INSERT INTO students (id, name, roll_number, department, course, subject, teacher_id, password) VALUES (2, 'Bob', 'R002', 'Mathematics', 'BSc Math', 'Calculus', 2, 'studentdemo');

-- seed some classes so the CoursesPage has content to show
INSERT INTO classes (id, department, course, subject, teacher_id, capacity, semester, semester_number, year, term, section) VALUES (1, 'Computer Science', 'BSc CS', 'Algorithms', '1', 50, 'Sem 1', 1, NULL, '', 'A');
INSERT INTO classes (id, department, course, subject, teacher_id, capacity, semester, semester_number, year, term, section) VALUES (2, 'Mathematics', 'BSc Math', 'Calculus', '2', 40, 'Sem 1', 1, NULL, '', 'A');
INSERT INTO classes (id, department, course, subject, teacher_id, capacity, semester, semester_number, year, term, section) VALUES (3, 'Computer Science', 'BCA', 'Database Management', '1', 60, '2025-Fall', NULL, 2025, 'Fall', 'General');
