package com.project.streaming_dataservice.service;

import com.project.streaming_dataservice.model.Movie;
import com.project.streaming_dataservice.model.Room;
import com.project.streaming_dataservice.repos.MovieRepository;
import com.project.streaming_dataservice.repos.RoomRepository;
import com.project.streaming_dataservice.repos.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final SeanceRepository seanceRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RoomService(RoomRepository roomRepository,
                       MovieRepository movieRepository,
                       SeanceRepository seanceRepository,
                       PasswordEncoder passwordEncoder) {
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.seanceRepository = seanceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Room createRoom(Room room) {
        if (room.getId() == null) {
            throw new RuntimeException("UUID is required to create a room");
        }
        if (room.getOwner() == null) {
            throw new RuntimeException("Owner is required to create a room");
        }
        if (room.getMovie() == null || room.getMovie().getId() == null) {
            throw new RuntimeException("Movie is required to create a room");
        }

        Movie movie = movieRepository.findById(room.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        room.setMovie(movie);

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

    /**
     * Обновление состояния всех комнат: устанавливаем или сбрасываем emptySince.
     * Вызывать можно по крону или вручную.
     */
    @Transactional
    @Scheduled(fixedRate = 60 * 1000) // каждую минуту
    public void updateRoomStates() {
        List<Room> rooms = roomRepository.findAll();

        for (Room room : rooms) {
            boolean hasSeances = seanceRepository.existsByRoom(room);

            if (hasSeances) {
                // Если сеансы появились — сбросим таймер
                if (room.getEmptySince() != null) {
                    room.setEmptySince(null);
                    roomRepository.save(room);
                }
            } else {
                // Если сеансов нет — начать отсчет
                if (room.getEmptySince() == null) {
                    room.setEmptySince(LocalDateTime.now());
                    roomRepository.save(room);
                }
            }
        }
    }

    /**
     * Удаление комнат, у которых не было сеансов 10 минут
     */
    @Transactional
    @Scheduled(fixedRate = 5 * 60 * 1000) // каждые 5 минут
    public void deleteInactiveRooms() {
        LocalDateTime now = LocalDateTime.now();
        List<Room> rooms = roomRepository.findAll();

        for (Room room : rooms) {
            if (room.getEmptySince() != null &&
                room.getEmptySince().plusMinutes(10).isBefore(now)) {
                roomRepository.delete(room);
                System.out.println("Deleted inactive room with id: " + room.getId());
            }
        }
    }
}
