package com.project.streaming_dataservice.repos;

import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByOwnerId(Long ownerId);
    boolean existsByRoom(Room room);
}
