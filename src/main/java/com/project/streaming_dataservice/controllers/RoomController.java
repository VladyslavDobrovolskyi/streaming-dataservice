package com.project.streaming_dataservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.streaming_dataservice.service.RoomService;

@RestController
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/api/room/hello")
    public String helloRoom() {
        return roomService.test();
    }
}

