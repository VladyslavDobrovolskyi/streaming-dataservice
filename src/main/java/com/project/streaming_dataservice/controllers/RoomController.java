package com.project.streaming_dataservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.streaming_dataservice.service.RoomService;
import com.project.streaming_dataservice.model.Room;

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


/*************  ✨ Windsurf Command ⭐  *************/
    /**
     * This is a simple test method to check that the room endpoint is working
     * correctly. It calls the test method on the roomService and returns the
     * result as a string.
     *
     * @return The result of the roomService.test() method as a string.
     */
/*******  5c9956ec-2f0f-4f30-90ee-619ab944f9e7  *******/
    @GetMapping("/hello")
    public String helloRoom() {
        return roomService.test();
    }
}

