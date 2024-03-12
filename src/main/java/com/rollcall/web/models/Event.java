package com.rollcall.web.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer zip;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String type;
    private String photoURL;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name="group_id", nullable = false)
    private Group group;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;
}