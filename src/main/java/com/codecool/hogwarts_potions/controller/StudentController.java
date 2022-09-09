package com.codecool.hogwarts_potions.controller;

import com.codecool.hogwarts_potions.model.Student;
import com.codecool.hogwarts_potions.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {
    StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student")
    public List<Student> getAllStudent() {
        return studentService.getAllStudent();
    }

    @PostMapping("/room/{room_id}/student")
    public Student addStudent(@RequestBody Student student, @PathVariable("room_id") Long room_id) {
        return studentService.addStudent(student, room_id);
    }
}
