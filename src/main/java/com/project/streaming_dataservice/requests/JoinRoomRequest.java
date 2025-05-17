package com.project.streaming_dataservice.requests;

import java.util.UUID;

public class JoinRoomRequest {
    private UUID roomUUID;
    private String password;

    public UUID getRoomUUID() {
        return roomUUID;
    }

    public void setRoomUUID(UUID roomUUID) {
        this.roomUUID = roomUUID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
