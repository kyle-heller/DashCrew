
package com.rollcall.web.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

//bgg csv fields - name,game_id,type,rating,weight,year_published,min_players,max_players,min_play_time,max_play_time,min_age,owned_by,categories,mechanics,designers,artists,publishers

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long bggId;
    private String title;
    private int yearPublished;
    private String type;
    private String categories;
    private int minPlayers;
    private int maxPlayers;
    private int playingTime;
    private double averageRating;
    private String photoURL;
    @Column(columnDefinition = "TEXT")
    private String description;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

}
