package com.rollcall.web.services.impl;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.dto.GameDto;
import com.rollcall.web.models.Event;
import com.rollcall.web.models.Game;
import com.rollcall.web.models.Group;
import com.rollcall.web.repository.GameRepository;
import com.rollcall.web.services.GameService;
import com.rollcall.web.services.external.BGGApiDetailsFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rollcall.web.mapper.EventMapper.mapToEventDto;
import static com.rollcall.web.mapper.GameMapper.mapToGame;
import static com.rollcall.web.mapper.GameMapper.mapToGameDto;

import javax.persistence.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import static com.rollcall.web.mapper.GroupMapper.mapToGroupDto;

@Service
public class GameImportServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private BGGApiDetailsFetcher bggApiDetailsFetcher;

    public void updateGamePhotoUrl(Long bggId, String photoUrl) {
        Game game = gameRepository.findByBggId(bggId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with bggId: " + bggId));
        game.setPhotoURL(photoUrl);
        gameRepository.save(game);
    }

    public void fetchAndUpdateGamePhotoUrl(long bggId) {
        // Fetch the game details from BGG API
        GameDto gameDetails = bggApiDetailsFetcher.fetchGameDetails(bggId);

        // Retrieve the Game entity by bggId from the database
        Game game = gameRepository.findByBggId(bggId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with bggId: " + bggId));

        // Update the Game entity with the fetched details
        game.setPhotoURL(gameDetails.getPhotoURL());
        game.setDescription(gameDetails.getDescription());

        // Save the updated Game entity back to the database
        gameRepository.save(game);
    }



    @Transactional
    public void importGamesFromCsv(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Improved split to handle commas inside quotes
                long bggId = Long.parseLong(values[1]); // Assuming BGG ID is at index 1
                // Check if game already exists to update it or create a new one
                Game game = gameRepository.findByBggId(bggId).orElse(new Game());

                // Populate or update game's properties from CSV
                game.setTitle(values[0]);
                game.setBggId(bggId);
                game.setType(values[2]);
                game.setAverageRating(Double.parseDouble(values[3]));
                // Parse and set new fields based on their positions in the CSV
                game.setYearPublished(Integer.parseInt(values[5]));
                game.setMinPlayers(Integer.parseInt(values[6]));
                game.setMaxPlayers(Integer.parseInt(values[7]));
                int minPlayTime = Integer.parseInt(values[8]);
                int maxPlayTime = Integer.parseInt(values[9]);
                // Assuming playingTime should be set to maxPlayTime for consistency or calculate an average
                game.setPlayingTime(maxPlayTime);
                game.setCategories(values[12]);
                // Note: If categories, mechanics, designers, artists, or publishers are comma-separated lists within a single CSV field,
                // you may want to process them further into more structured data if your database schema supports it.
                gameRepository.save(game);
                // Before saving, fetch and set the photo URL
                fetchAndUpdateGamePhotoUrl(bggId); // Fetch and update photo URL

                // Save the game to the database
                gameRepository.save(game);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<GameDto> findAllGames() {
        List<Game> games = gameRepository.findAll();
        return games.stream().map(game -> mapToGameDto(game) ).collect(Collectors.toList());
    }

    @Override
    public Group saveGame(GameDto gameDto) {
        return null;
    }

    @Override
    public GameDto findByGameId(Long gameId) {
        Game game = gameRepository.findById(gameId).get();
        return mapToGameDto(game);
    }

    @Override
    public void updateGame(GameDto game) {

    }

    @Override
    public void deleteGame(Long gameId) {

    }

    @Override
    public List<GameDto> searchGames(String query) {
        return null;
    }
}
