package com.project.streaming_dataservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.streaming_dataservice.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByMovieName(Long movieName);

    boolean existsByMovieName(Long movieName);
}
