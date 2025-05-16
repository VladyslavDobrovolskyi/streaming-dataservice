package com.project.streaming_dataservice.controller;

import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/seances")
public class SeanceController {

    private final SeanceService seanceService;

    @Autowired
    public SeanceController(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    // Открыть сеанс - данные в теле запроса, включая owner
    @PostMapping("/open")
    public ResponseEntity<Seance> openSeance(@RequestBody @Valid Seance seance) {
        Seance opened = seanceService.openSeance(seance);
        return new ResponseEntity<>(opened, HttpStatus.CREATED);
    }

    // Закрыть сеанс(ы) владельца по ownerId в параметре запроса
    @DeleteMapping("/close")
    public ResponseEntity<String> closeSeances(@RequestParam Long ownerId) {
        seanceService.closeSeancesByOwnerId(ownerId);
        return ResponseEntity.ok("Seances closed for ownerId = " + ownerId);
    }
}
