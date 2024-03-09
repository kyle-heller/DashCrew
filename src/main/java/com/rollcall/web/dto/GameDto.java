package com.rollcall.web.dto;

import com.rollcall.web.models.Category;
import com.rollcall.web.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

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
    private Set<Category> categories;
    private int minPlayers;
    private int maxPlayers;
    private int playingTime;
    private double averageRating;
    private String photoURL;
    private String description;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private UserEntity createdBy;
    private String bggLink;


    // Constructor for BGGLinkParser
    public GameDto(String bggLink) {
        this.bggLink = bggLink;
    }

    // Constructor for BGGApiDetailsFetcher

    public GameDto(Long bggId, String title, String type, String photoUrl, String description, int yearPublished, int minPlayers, int maxPlayers, int playingTime, double averageRating, Set<Category> categories, String bggLink) {
        this.bggId = bggId;
        this.title = title;
        this.photoURL = photoUrl;
        this.description = description;
        this.yearPublished = yearPublished;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playingTime = playingTime;
        this.categories = categories;
        this.type = type;
        this.averageRating = averageRating;
        this.bggLink = bggLink;
    }
}