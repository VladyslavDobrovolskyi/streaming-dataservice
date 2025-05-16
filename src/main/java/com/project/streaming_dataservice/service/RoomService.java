package com.project.streaming_dataservice.service;

import com.project.streaming_dataservice.model.Movie;
import com.project.streaming_dataservice.model.Room;
import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.repos.MovieRepository;
import com.project.streaming_dataservice.repos.RoomRepository;
import com.project.streaming_dataservice.repos.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final SeanceRepository seanceRepository;
    private final PasswordEncoder passwordEncoder; // добавляем

    @Autowired
    public RoomService(RoomRepository roomRepository,
                       MovieRepository movieRepository,
                       SeanceRepository seanceRepository,
                       PasswordEncoder passwordEncoder) {  // в конструктор
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.seanceRepository = seanceRepository;
        this.passwordEncoder = passwordEncoder; // сохраняем
    }

    public Room createRoom(Room room) {
        if (room.getUuid() == null) {
            throw new RuntimeException("UUID is required to create a room");
        }
        if (room.getOwner() == null) {
            throw new RuntimeException("Owner is required to create a room");
        }
        if (room.getMovie() == null || room.getMovie().getId() == null) {
            throw new RuntimeException("Movie is required to create a room");
        }

        // Проверяем, что фильм существует в базе
        Movie movie = movieRepository.findById(room.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        room.setMovie(movie);

        // Если пароль есть — шифруем
        if (room.getPassword() != null && !room.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(room.getPassword());
            room.setPassword(encodedPassword);
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
            if (password == null || !passwordEncoder.matches(password, room.getPassword())) {
                return false;
            }
        }

        return true;
    }

    public String test() {
        return "test";
    }
}
