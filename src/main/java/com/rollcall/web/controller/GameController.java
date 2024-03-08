package com.rollcall.web.controller;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.dto.GameDto;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Event;
import com.rollcall.web.models.Game;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.EventService;
import com.rollcall.web.services.GameService;
import com.rollcall.web.services.GroupService;
import com.rollcall.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class GameController {
    private GameService gameService;
    private UserService userService;

    @Autowired
    public GameController(GameService gameService, UserService userService) {
        this.userService = userService;
        this.gameService = gameService;
    }


    @GetMapping("/games")
    public String listGroups(Model model) {
        GameDto game = new GameDto();
        List<GameDto> games = gameService.findAllGames();
        model.addAttribute("games", games);
        return "games-list";
    }

    @GetMapping("/games/{gameId}")
    public String viewGame(@PathVariable("gameId") Long gameId, Model model) {
        GameDto game = gameService.findByGameId(gameId);
        model.addAttribute("gameTitle", game.getTitle());
        model.addAttribute("game", game);
        return "games-detail";
    }
}
