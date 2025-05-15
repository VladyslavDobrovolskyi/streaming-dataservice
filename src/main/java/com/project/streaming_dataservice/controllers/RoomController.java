package com.project.streaming_dataservice.controllers;

//import com.project.streaming_dataservice.service.AuthClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.project.streaming_dataservice.model.Room;

import com.project.streaming_dataservice.service.RoomService;

import java.security.Principal;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

@PostMapping("/create")
public ResponseEntity<Room> createRoom(@RequestBody Room room) {
    Room createdRoom = roomService.createRoom(room);
    return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
}


    @GetMapping("/hello")
    public String helloRoom() {
        return roomService.test();
    }
}

