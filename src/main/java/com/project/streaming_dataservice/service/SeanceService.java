package com.project.streaming_dataservice.service;

import com.project.streaming_dataservice.model.Room;
import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.model.Movie;
import com.project.streaming_dataservice.repos.RoomRepository;
import com.project.streaming_dataservice.repos.SeanceRepository;
import com.project.streaming_dataservice.repos.UserRepository;
import com.project.streaming_dataservice.repos.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public SeanceService(SeanceRepository seanceRepository, UserRepository userRepository,
                         RoomRepository roomRepository, MovieRepository movieRepository) {
        this.seanceRepository = seanceRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Seance openSeance(Seance seance) {
        // Загружаем существующий Room по id
        UUID roomId = seance.getRoom().getId();
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Загружаем существующего User (владельца)
        Long ownerId = seance.getOwner().getId();
        User existingOwner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        // Загружаем существующий Movie
        Long movieId = seance.getMovie().getId();
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Устанавливаем загруженные сущности
        seance.setRoom(existingRoom);
        seance.setOwner(existingOwner);
        seance.setMovie(existingMovie);

        return seanceRepository.save(seance);
    }

    @Transactional
    public void closeSeanceByOwnerId(Long ownerId) {
        List<Seance> seances = seanceRepository.findByOwnerId(ownerId);
        seanceRepository.deleteAll(seances);
    }

    public List<Seance> getSeancesByOwnerId(Long ownerId) {
        return seanceRepository.findByOwnerId(ownerId);
    }
}
