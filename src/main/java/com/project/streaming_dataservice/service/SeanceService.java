package com.project.streaming_dataservice.service;

import com.project.streaming_dataservice.model.Seance;
import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.repos.SeanceRepository;
import com.project.streaming_dataservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final UserRepository userRepository;

    @Autowired
    public SeanceService(SeanceRepository seanceRepository, UserRepository userRepository) {
        this.seanceRepository = seanceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Seance openSeance(Seance seance) {
        // Можно добавить дополнительные проверки (например, проверить, существует ли пользователь)
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
