package com.rollcall.web.services;

import com.rollcall.web.dto.GameDto;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Game;
import com.rollcall.web.models.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;
public interface GameService {
    List<GameDto> findAllGames();

    GameDto saveGame(GameDto gameDto);

    GameDto findByGameId(Long gameId);

    void updateGame(GameDto game);

    void deleteGame(Long gameId);

    List<GameDto> searchGames(String query);

    Page<Game> findPaginated(Pageable pageable);

}
