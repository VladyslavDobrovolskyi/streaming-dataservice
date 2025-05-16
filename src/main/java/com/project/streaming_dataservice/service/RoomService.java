package com.project.streaming_dataservice.service;

import com.project.streaming_dataservice.model.Movie;
import com.project.streaming_dataservice.model.Room;
import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.repos.MovieRepository;
import com.project.streaming_dataservice.repos.RoomRepository;
import com.project.streaming_dataservice.repos.SeanceRepository;
import com.project.streaming_dataservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final SeanceRepository seanceRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository,
                       UserRepository userRepository,
                       MovieRepository movieRepository,
                       SeanceRepository seanceRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.seanceRepository = seanceRepository;
    }

    public Room createRoom(Room room, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(room.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        room.setUuid(UUID.randomUUID());
        room.setOwner(user);
        room.setMovie(movie);

        return roomRepository.save(room);
    }

    public boolean joinRoom(UUID roomUUID, String password, Principal principal) {
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

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Найдём сеанс или создадим новый, если нужно (уникальность по пользователю + комнате можно настраивать отдельно)
        Seance seance = new Seance();
        seance.setOwner(room.getOwner());
        seance.setRoom(room);
        seance.setMovie(room.getMovie());
        seance.setCreatedAt(LocalDateTime.now());
        seance.getParticipants().add(user);

        seanceRepository.save(seance);

        return true;
    }

    public String test() {
        return "test";
    }
}
