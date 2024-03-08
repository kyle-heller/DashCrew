package com.rollcall.web.services;

import com.rollcall.web.dto.GameDto;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Group;
import org.springframework.stereotype.Service;

import java.util.List;
public interface GameService {
    List<GameDto> findAllGames();

    Group saveGame(GameDto gameDto);

    GameDto findByGameId(Long gameId);

    void updateGame(GameDto game);

    void deleteGame(Long gameId);

    List<GameDto> searchGames(String query);
}
