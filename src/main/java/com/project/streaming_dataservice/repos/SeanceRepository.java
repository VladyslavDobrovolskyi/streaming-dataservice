package com.project.streaming_dataservice.repos;

import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SeanceRepository extends JpaRepository<Seance, String> {
    List<Seance> findByOwnerId(String ownerId);
    boolean existsByRoom(Room room);
}
