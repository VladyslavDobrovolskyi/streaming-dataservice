package com.project.streaming_dataservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(unique = true, nullable = false, updatable = false)
    private UUID roomUUID;

    @Column(nullable = false)
    private Long movieName;

    @Column(nullable = false)
    private String password;

}

