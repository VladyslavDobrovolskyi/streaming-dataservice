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

import java.util.List;

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

        Seance openedSeance = seanceService.openSeance(seance, user);

        return new ResponseEntity<>(openedSeance, HttpStatus.CREATED);
    }

    @DeleteMapping("/close")
    public ResponseEntity<String> closeSeances(@CookieValue(value = "userId", required = false) String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
        }
        seanceService.closeSeanceByOwnerId(userId);
        return ResponseEntity.ok("Seances closed for ownerId = " + userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getSeancesByOwner(@CookieValue(value = "userId", required = false) String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
        }
        List<Seance> seances = seanceService.getSeancesByOwnerId(userId);
        return ResponseEntity.ok(seances);
    }
}
