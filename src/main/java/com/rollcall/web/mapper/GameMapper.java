package com.rollcall.web.mapper;

import com.rollcall.web.dto.GameDto;
import com.rollcall.web.models.Game;

public class GameMapper {

    public static Game mapToGame(GameDto gameDto) {
        return Game.builder()
                .id(gameDto.getId())
                .bggId(gameDto.getBggId())
                .title(gameDto.getTitle())
                .yearPublished(gameDto.getYearPublished())
                .type(gameDto.getType())
                .categories(gameDto.getCategories())
                .minPlayers(gameDto.getMinPlayers())
                .maxPlayers(gameDto.getMaxPlayers())
                .playingTime(gameDto.getPlayingTime())
                .averageRating(gameDto.getAverageRating())
                .photoURL(gameDto.getPhotoURL())
                .description(gameDto.getDescription())
                .bggLink(gameDto.getBggLink())
                .build();
    }

    public static GameDto mapToGameDto(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .bggId(game.getBggId())
                .title(game.getTitle())
                .yearPublished(game.getYearPublished())
                .type(game.getType())
                .categories(game.getCategories())
                .minPlayers(game.getMinPlayers())
                .maxPlayers(game.getMaxPlayers())
                .playingTime(game.getPlayingTime())
                .averageRating(game.getAverageRating())
                .photoURL(game.getPhotoURL())
                .description(game.getDescription())
                .bggLink(game.getBggLink())
                .build();
    }
}
