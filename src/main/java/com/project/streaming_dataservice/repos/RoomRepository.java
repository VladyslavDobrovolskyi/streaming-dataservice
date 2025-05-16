package com.project.streaming_dataservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import com.project.streaming_dataservice.model.Room;

import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

List<Room> findByCreatedAtBefore(LocalDateTime dateTime);

}
