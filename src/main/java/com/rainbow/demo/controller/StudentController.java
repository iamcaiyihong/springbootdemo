package com.rainbow.demo.controller;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rainbow.demo.entity.Student;
import com.rainbow.demo.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Create
    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    // Read
    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAllStudents();
    }

    // Update
    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        int rows = studentService.updateStudent(student);
        return rows > 0 ? "Update success" : "Update failed";
    }

    // Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        int rows = studentService.deleteStudent(id);
        return rows > 0 ? "Delete success" : "Delete failed";
    }
}
