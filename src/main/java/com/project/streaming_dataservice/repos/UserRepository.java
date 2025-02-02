package com.project.streaming_dataservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.streaming_dataservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String email);

    boolean existsByUsername(String username);
}