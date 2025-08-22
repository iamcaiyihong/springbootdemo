package com.rainbow.demo.student.service;


import com.rainbow.demo.student.entity.Student;
import com.rainbow.demo.student.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public Student createStudent(Student student) {
        studentMapper.insert(student);
        return student;
    }

    public Student getStudent(Long id) {
        return studentMapper.selectById(id);
    }

    public List<Student> getAllStudents() {
        return studentMapper.selectAll();
    }

    public int updateStudent(Student student) {
        return studentMapper.update(student);
    }

    public int deleteStudent(Long id) {
        return studentMapper.delete(id);
    }
}
