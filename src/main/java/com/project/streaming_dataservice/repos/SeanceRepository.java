package com.project.streaming_dataservice.repos;

import com.project.streaming_dataservice.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {

}
