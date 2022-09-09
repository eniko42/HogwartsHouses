package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.PetType;
import com.codecool.hogwarts_potions.model.Room;
import com.codecool.hogwarts_potions.service.repositories.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomDao roomDao;


    @Autowired
    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<Room> getAllRooms() {
        return roomDao.findAll();
    }

    public Room addRoom(Room room) {
        return roomDao.save(room);
    }

    public Room getRoomById(Long id) {
        return roomDao.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("No room exist with given id: %d", id)));
    }

    public Room updateRoomById(Long id, Room updatedRoom) {
        if (getRoomById(id) != null) {
            updatedRoom.setId(id);
        }
        return roomDao.save(updatedRoom);
    }

    public void deleteRoomById(Long id) {
        if (getRoomById(id) != null) {
            roomDao.deleteById(id);
        }
    }

    public List<Room> getRoomsForRatOwners() {
        List<Room> rooms = roomDao.findAll();
        return rooms.stream().filter(room ->
                        room.getResidents().stream()
                                .noneMatch(student -> PetType.dangerousPetsForRatOwners().contains(student.getPetType())))
                .collect(Collectors.toList());
    }
}
