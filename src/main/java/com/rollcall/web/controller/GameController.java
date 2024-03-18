package com.rollcall.web.controller;

import com.rollcall.web.dto.GameDto;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Game;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.GameService;
import com.rollcall.web.services.UserService;
import com.rollcall.web.services.external.BggApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private final BggApiServiceImpl bggApiServiceImpl;

    @Autowired
    public GameController(GameService gameService, UserService userService, BggApiServiceImpl bggApiServiceImpl) {
        this.userService = userService;
        this.gameService = gameService;
        this.bggApiServiceImpl = bggApiServiceImpl;
    }

    @GetMapping("/games")
    public String listGames(@RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size,
                            Model model) {
        int currentPage = page.orElse(1); // Default to page 1
        int pageSize = size.orElse(20); // Default to 20 items per page

        Page<Game> gamePage = gameService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("games", gamePage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", gamePage.getTotalPages());

        return "games-list";
    }

    @GetMapping("/games/new")
    public String createGameForm(Model model) {
        model.addAttribute("game", new Game());
        return "games-create";
    }

    @PostMapping("/games/new")
    public String saveGameNew(@Valid @ModelAttribute("game") GameDto gameDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("game", gameDto);
            return "games-create";
        }
        gameService.saveGame(gameDto);
        return "redirect:/games";
    }

    @PostMapping("/games/newbgglink")
    public String saveGameBggLinkNew(@Valid @ModelAttribute("game") GameDto gameDto, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            model.addAttribute("game", gameDto);
            return "games-create";
        }
        Long id = bggApiServiceImpl.parseBggLink(gameDto.getBggLink());
        GameDto gameDetails = bggApiServiceImpl.fetchGameDetails(id);
        gameDetails.setBggLink(gameDto.getBggLink());
        gameService.saveGame(gameDetails);
        return "redirect:/games";
    }

    @GetMapping("/games/{gameId}")
    public String viewGame(@PathVariable("gameId") Long gameId, Model model) {
        GameDto game = gameService.findByGameId(gameId);
        model.addAttribute("game", game);
        model.addAttribute("gameTitle", game.getTitle());
        return "games-detail";
    }

    @GetMapping("/games/search")
    public String searchGroups(@RequestParam(value = "query") String query, Model model) {
        List<GameDto> games = gameService.searchGames(query);

        System.out.println(games);

        model.addAttribute("games", games);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);

        return "games-list";
    }
}
