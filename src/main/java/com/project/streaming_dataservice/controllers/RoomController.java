package com.project.streaming_dataservice.controllers;

import com.project.streaming_dataservice.model.Room;
import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.requests.JoinRoomRequest;
import com.project.streaming_dataservice.service.RoomService;
import com.project.streaming_dataservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


import java.util.UUID;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;
    private final UserService userService;

    public RoomController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

  @PostMapping("/create")
public ResponseEntity<?> createRoom(
        @Valid @RequestBody Room room,
        @CookieValue(value = "userId", required = false) String userId) {

    if (userId == null || userId.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
    }

    User user;
    try {
        user = userService.findUserById(userId);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }

    room.setOwner(user); // устанавливаем владельца комнаты

    Room createdRoom = roomService.createRoom(room);
    return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
}
@PostMapping("/join")
public ResponseEntity<String> joinRoom(
        @RequestBody JoinRoomRequest request,
        @CookieValue(value = "userId", required = false) String userId) {

    if (userId == null || userId.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
    }

    User user;
    try {
        user = userService.findUserById(userId);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }

    boolean joined = roomService.joinRoom(request.getRoomUUID(), request.getPassword(), user);

    if (joined) {
        return ResponseEntity.ok(Map.of("result", "Successfully joined the room"));
    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Failed to join the room: Invalid UUID or password"));
    }
}

@GetMapping("/info")
public ResponseEntity<?> getRoomInfo(
        @RequestParam UUID roomUUID,
        @CookieValue(value = "userId", required = false) String userId) {

    if (userId == null || userId.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
    }

    Optional<Room> roomOpt = roomService.getRoomById(roomUUID);
    if (roomOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
    }

    Room room = roomOpt.get();
    if (room.getMovie() == null || room.getMovie().getId() == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found for this room");
    }

    return ResponseEntity.ok(room.getMovie().getId());
}

    @GetMapping("/hello")
    public String helloRoom() {
        return "test";
    }
}
