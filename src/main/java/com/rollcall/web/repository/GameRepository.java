package com.rollcall.web.repository;

import com.rollcall.web.models.Game;
import com.rollcall.web.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByBggId(Long bggId);

    Optional<Game> findByTitle(String url);
    @Query("SELECT c FROM Game c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Game> searchGames(@Param("query") String query);

}