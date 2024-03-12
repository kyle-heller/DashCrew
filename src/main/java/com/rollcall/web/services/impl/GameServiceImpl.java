package com.rollcall.web.services.impl;

import com.rollcall.web.dto.GameDto;
import com.rollcall.web.mapper.GameMapper;
import com.rollcall.web.models.Game;
import com.rollcall.web.models.Group;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.repository.CategoryRepository;
import com.rollcall.web.repository.GameRepository;
import com.rollcall.web.repository.UserRepository;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.BggApiService;
import com.rollcall.web.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.rollcall.web.mapper.GameMapper.mapToGame;
import static com.rollcall.web.mapper.GameMapper.mapToGameDto;
import static com.rollcall.web.mapper.GroupMapper.mapToGroupDto;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Override //fetches a page of Game entities from the database
    public Page<Game> findPaginated(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override //Converts a list of Game entities to GameDto objects and collects them into a list
    public List<GameDto> findAllGames() {
        return gameRepository.findAll().stream().map(GameMapper::mapToGameDto).collect(Collectors.toList());
    }

    @Override //Saves a new Game entity to the database using the provided GameDto and assigns the current user as the creator
    public GameDto saveGame(GameDto gameDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);
        Game game = mapToGame(gameDto);
        game.setCreatedBy(user);
        gameRepository.save(game);
        return null;
    }

    @Override // Retrieves a Game entity by its ID and converts it to a GameDto, throws if not found
    public GameDto findByGameId(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        return GameMapper.mapToGameDto(game);
    }

    @Override
    public void updateGame(GameDto gameDto) {
        // Implementation for update
    }

    @Override
    public void deleteGame(Long gameId) {
        // Implementation for delete
    }

    @Override
    public List<GameDto> searchGames(String query) {
        List<Game> games = gameRepository.searchGames(query);
        return games.stream().map(game -> mapToGameDto(game)).collect(Collectors.toList());
    }
}
