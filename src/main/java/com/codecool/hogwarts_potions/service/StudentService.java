package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.Student;
import com.codecool.hogwarts_potions.service.repositories.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private StudentDao studentDao;

    @Autowired
    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void addStudent(Student student) {
        studentDao.save(student);
    }

    public List<Student> getAllStudent() {
        return studentDao.findAll();
    }
}
