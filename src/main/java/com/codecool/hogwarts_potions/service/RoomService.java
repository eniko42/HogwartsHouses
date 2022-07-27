package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.Room;
import com.codecool.hogwarts_potions.model.Student;
import com.codecool.hogwarts_potions.service.repositories.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private RoomDao roomDao;

    @Autowired
    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<Room> getAllRooms() {
        return roomDao.findAll();
    }

    public void addRoom(Room room) {
        roomDao.save(room);
    }

    public Room getRoomById(Long id) {
        return roomDao.findById(id).get();
    }

    public void updateRoomById(Long id, Room updatedRoom) {
        //TODO
    }

    public void deleteRoomById(Long id) {
        //TODO
    }

    public List<Room> getRoomsForRatOwners() {
        //TODO
        return null;
    }
}
