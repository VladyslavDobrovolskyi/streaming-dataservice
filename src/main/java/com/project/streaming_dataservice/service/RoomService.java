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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        room.setCreatedBy(authentication.getName());
        return roomRepository.save(room);
    }
     public String test() {
        return "test";
    }
}
