package com.codecool.hogwarts_potions.service.repositories;

import com.codecool.hogwarts_potions.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomDao extends JpaRepository<Room, Long> {
    List<Room> findAll();
}
