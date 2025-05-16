package com.project.streaming_dataservice.service;

import com.project.streaming_dataservice.model.Movie;
import com.project.streaming_dataservice.model.Room;
import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.repos.MovieRepository;
import com.project.streaming_dataservice.repos.RoomRepository;
import com.project.streaming_dataservice.repos.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final SeanceRepository seanceRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository,
                       MovieRepository movieRepository,
                       SeanceRepository seanceRepository) {
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.seanceRepository = seanceRepository;
    }

    public Room createRoom(Room room, User owner) {
        // Генерируем UUID для комнаты
        room.setUuid(UUID.randomUUID());
        room.setOwner(owner);

        // Загружаем фильм из базы по id, если он есть
        if (room.getMovie() != null && room.getMovie().getId() != null) {
            Movie movie = movieRepository.findById(room.getMovie().getId())
                    .orElseThrow(() -> new RuntimeException("Movie not found"));
            room.setMovie(movie);
        } else {
            throw new RuntimeException("Movie is required to create a room");
        }


        return roomRepository.save(room);
    }

    public boolean joinRoom(UUID roomUUID, String password) {
        Optional<Room> roomOpt = roomRepository.findById(roomUUID);
        if (roomOpt.isEmpty()) {
            return false;
        }

        Room room = roomOpt.get();

        if (room.getPassword() != null && !room.getPassword().isBlank()) {
            if (password == null || !room.getPassword().equals(password)) {
                return false;
            }
        }

        // Здесь нет пользователя, так что сеанс не создаем
        // Если нужно, можно сделать что-то простое, например:
        // return true - пользователь "успешно присоединился"

        return true;
    }

    public String test() {
        return "test";
    }
}
