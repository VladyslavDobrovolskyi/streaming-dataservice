package com.project.streaming_dataservice.controller;

import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.service.SeanceService;
import com.project.streaming_dataservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seances")
public class SeanceController {

    private final SeanceService seanceService;
    private final UserService userService;

    @Autowired
    public SeanceController(SeanceService seanceService, UserService userService) {
        this.seanceService = seanceService;
        this.userService = userService;
    }

    @PostMapping("/open")
    public ResponseEntity<?> openSeance(
            @RequestBody @Valid Seance seance,
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


        seanceService.closeSeanceByOwnerId(userId);
        Seance openedSeance = seanceService.openSeance(seance, user);

        return new ResponseEntity<>(openedSeance, HttpStatus.CREATED);
    }

    @DeleteMapping("/close")
public ResponseEntity<?> closeSeances(@CookieValue(value = "userId", required = false) String userId) {
    if (userId == null || userId.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: missing user cookie");
    }

    seanceService.closeSeanceByOwnerId(userId);
    return ResponseEntity.noContent().build(); // 204 No Content
}

    @GetMapping("/owner")
    public ResponseEntity<?> getSeancesByOwner(@CookieValue(value = "userId", required = false) String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
        }
        List<Seance> seances = seanceService.getSeancesByOwnerId(userId);
        return ResponseEntity.ok(seances);
    }

    
    @GetMapping("/handshake/{roomId}")
    public ResponseEntity<?> checkUserSeanceInRoom(@PathVariable UUID roomId, @CookieValue(value = "userId", required = false) String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
        }
        boolean hasSeance = seanceService.existsInRoom(roomId, userId);
        return ResponseEntity.ok(Map.of("status", hasSeance));
    }

    
    @PatchMapping("/continue")
    public ResponseEntity<?> continueSeance(@CookieValue(value = "userId", required = false) String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
        }
        seanceService.continueSeanceByOwnerId(userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
