package com.project.streaming_dataservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    @GetMapping("/api/room/hello")
    public String helloRoom() {
        return "Room Service";
    }
}
