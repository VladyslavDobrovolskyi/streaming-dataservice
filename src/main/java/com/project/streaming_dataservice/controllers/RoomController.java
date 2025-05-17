package com.project.streaming_dataservice.controllers;

import com.project.streaming_dataservice.model.Room;
import com.project.streaming_dataservice.requests.JoinRoomRequest;
import com.project.streaming_dataservice.service.RoomService;
import com.project.streaming_dataservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;
     private final UserService userService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) {
        Room createdRoom = roomService.createRoom(room);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

@PostMapping("/join")
public ResponseEntity<String> joinRoom(
        @RequestBody JoinRoomRequest request,
        @CookieValue(value = "userId", required = false) String userId) {

    // Проверка наличия userId
    if (userId == null || userId.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
    }

    // Проверка существования пользователя
    boolean userExists = userService.findUserById(userId);
    if (!userExists) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }

    // Попытка входа в комнату
    boolean joined = roomService.joinRoom(request.getRoomUUID(), request.getPassword(), userId);
    if (joined) {
        return ResponseEntity.ok("Successfully joined the room");
    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to join the room: Invalid UUID or password");
    }
}

    @GetMapping("/hello")
    public String helloRoom() {
        return roomService.test();
    }
}
