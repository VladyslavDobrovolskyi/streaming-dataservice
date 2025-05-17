package com.project.streaming_dataservice.controller;

import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/seances")
public class SeanceController {

    private final SeanceService seanceService;

    @Autowired
    public SeanceController(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    @PostMapping("/open")
    public ResponseEntity<Seance> openSeance(@RequestBody @Valid Seance seance) {
        Seance opened = seanceService.openSeance(seance);
        return new ResponseEntity<>(opened, HttpStatus.CREATED);
    }

    @DeleteMapping("/close")
    public ResponseEntity<String> closeSeances(@RequestParam Long ownerId) {
        seanceService.closeSeanceByOwnerId(ownerId);
        return ResponseEntity.ok("Seances closed for ownerId = " + ownerId);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<Seance>> getSeancesByOwner(@RequestParam Long ownerId) {
        List<Seance> seances = seanceService.getSeancesByOwnerId(ownerId);
        return ResponseEntity.ok(seances);
    }
}
