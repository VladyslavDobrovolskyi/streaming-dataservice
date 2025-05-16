package com.project.streaming_dataservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "seances")
@Getter
@Setter
@NoArgsConstructor
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Создатель сеанса (владелец)
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Комната, в которой проходит сеанс
    @ManyToOne
    @JoinColumn(name = "room_uuid", nullable = false)
    private Room room;

    // Фильм
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // Все пользователи, участвующие в просмотре
    @ManyToMany
    @JoinTable(
        name = "seance_users",
        joinColumns = @JoinColumn(name = "seance_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();

    // Дата и время создания
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
