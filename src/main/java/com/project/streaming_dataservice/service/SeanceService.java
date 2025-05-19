package com.project.streaming_dataservice.service;

import com.project.streaming_dataservice.model.Room;
import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.model.Movie;
import com.project.streaming_dataservice.repos.RoomRepository;
import com.project.streaming_dataservice.repos.SeanceRepository;
import com.project.streaming_dataservice.repos.UserRepository;
import com.project.streaming_dataservice.repos.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;


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


public boolean existsInRoom(UUID roomId, String userId) {
    Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("Room not found with id " + roomId));
    return seanceRepository.existsByRoom(room) && seanceRepository.findByOwnerId(userId).stream()
            .anyMatch(seance -> seance.getRoom().getId().equals(roomId));
}

    @Transactional
    public Seance openSeance(Seance seance, User owner) {
        UUID roomId = seance.getRoom().getId();
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id " + roomId));

        Long movieId = seance.getMovie().getId();
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id " + movieId));

        seance.setRoom(existingRoom);
        seance.setOwner(owner);  // ставим владельца из параметра
        seance.setMovie(existingMovie);

        return seanceRepository.save(seance);
    }

    @Transactional
    public void closeSeanceByOwnerId(String ownerId) {
        System.out.println("Closing seances for ownerId = " + ownerId);
        List<Seance> seances = seanceRepository.findByOwnerId(ownerId);
        seanceRepository.deleteAll(seances);
    }

    public List<Seance> getSeancesByOwnerId(String ownerId) {
        return seanceRepository.findByOwnerId(ownerId);
    }

@Transactional
public void continueSeanceByOwnerId(String ownerId) {
    List<Seance> seances = seanceRepository.findByOwnerId(ownerId);
    for (Seance seance : seances) {
        seance.setLastRenewal(LocalDateTime.now());
        seanceRepository.save(seance);
    }
}

    @Scheduled(fixedRate = 300000) // каждые 5 минут
    @Transactional
    public void checkAndDeleteOldSeances() {
        List<Seance> oldSeances = seanceRepository.findByLastRenewalBefore(LocalDateTime.now().minusMinutes(8));
        seanceRepository.deleteAll(oldSeances);
    }

}
