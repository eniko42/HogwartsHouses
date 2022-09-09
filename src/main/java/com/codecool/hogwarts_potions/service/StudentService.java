package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.Student;
import com.codecool.hogwarts_potions.service.repositories.RoomDao;
import com.codecool.hogwarts_potions.service.repositories.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StudentService {

    private final StudentDao studentDao;
    private final RoomDao roomDao;

    @Autowired
    public StudentService(StudentDao studentDao, RoomDao roomDao) {
        this.studentDao = studentDao;
        this.roomDao = roomDao;
    }

    public Student addStudent(Student student, Long room_id) {
        student.setRoom(roomDao.findById(room_id).orElseThrow(() -> new NoSuchElementException(String.format("No room exist with given id: %d", room_id))));
        return studentDao.save(student);
    }

    public List<Student> getAllStudent() {
        return studentDao.findAll();
    }
}
