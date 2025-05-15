package com.project.streaming_dataservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.streaming_dataservice.model.Room;
import com.project.streaming_dataservice.repos.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room room) {
        if (roomRepository.existsById(room.getId())) {
            throw new RuntimeException("This room already exists");
        }
        return roomRepository.save(room);
    }

    public Room findRoomById(UUID id) {
        return roomRepository.findById(id).orElseThrow();
    }

    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(UUID id) {
        Room room = roomRepository.findById(id).orElseThrow();
        roomRepository.delete(room);
    }
     public void test() {
        return 'test'
    }
}
