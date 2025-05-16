package com.project.streaming_dataservice.repository;

import com.project.streaming_dataservice.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByOwnerId(Long ownerId);
}
