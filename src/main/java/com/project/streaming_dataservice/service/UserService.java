package com.project.streaming_dataservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.repos.UserRepository;
import java.util.UUID;
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   public User registerUser(User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
        throw new RuntimeException("This email is already in use");
    }
    if (user.getId() == null) {
        user.setId(UUID.randomUUID().toString());
    }
    return userRepository.save(user);
}

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(String id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }
}
