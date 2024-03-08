package com.rollcall.web.dto;

import com.rollcall.web.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class GameDto {

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
    private String description;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private UserEntity createdBy;

    // Constructor for BGGApiDetailsFetcher
    public GameDto(String photoUrl, String description) {
        this.photoURL = photoUrl;
        this.description = description;
    }

  }