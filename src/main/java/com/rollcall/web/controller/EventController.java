package com.rollcall.web.controller;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.dto.GameDto;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Event;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.EventService;
import com.rollcall.web.services.GameService;
import com.rollcall.web.services.GroupService;
import com.rollcall.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final GroupService groupService;
    private final GameService gameService;

    public EventController(EventService eventService, UserService userService, GroupService groupService, GameService gameService) {
        this.eventService = eventService;
        this.userService = userService;
        this.groupService = groupService;
        this.gameService = gameService;
    }

    @GetMapping("/events")
    public String eventList(Model model) {
        List<EventDto> events = eventService.findAllEvents();
        UserEntity user = getUser();
        model.addAttribute("events", events);
        model.addAttribute("user", user);
        return "events-list";
    }

    @GetMapping("/events/{groupId}/new")
    public String createEventForm(@PathVariable("groupId") Long groupId, Model model) {
        List<GameDto> games = gameService.findAllGames();
        model.addAttribute("gameList", games);
        model.addAttribute("groupId", groupId);
        model.addAttribute("event", new Event());
        return "events-create";
    }

    @GetMapping("/events/new")
    public String showIntermediateView(Model model) {
        UserEntity user = new UserEntity();
        List<GroupDto> groups = groupService.findAllGroups();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("groups", groups);
        return "events-selectgroup";
    }


    @PostMapping("/events/{groupId}")
    public String createEvent(@PathVariable("groupId") Long groupId, @ModelAttribute("event") EventDto eventDto) {
        eventService.createEvent(groupId, eventDto);
        return "redirect:/groups/" + groupId +"?success";
    }

    @GetMapping("/events/{eventId}")
    public String viewEvent(@PathVariable("eventId") Long eventId, Model model) {
        EventDto eventDto = eventService.findByEventId(eventId);
        UserEntity user = getUser();
        model.addAttribute("group", eventDto.getGroup());
        model.addAttribute("event", eventDto);
        model.addAttribute("user", user);
        return "events-detail";
    }

    @GetMapping("/events/{eventId}/edit")
    public String editEventForm(@PathVariable("eventId") Long eventId, Model model) {
        EventDto eventDto = eventService.findByEventId(eventId);
        model.addAttribute("event", eventDto);
        return "events-edit";
    }

    @PostMapping("/events/{eventId}/edit")
    public String updateEvent(@PathVariable("eventId") Long eventId, @Valid @ModelAttribute("event") EventDto eventDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("event", eventDto);
            return "events-edit";
        }
        eventService.updateEvent(eventDto);
        return "redirect:/events";
    }

    @GetMapping("/events/{eventId}/delete")
    public String deleteEvent(@PathVariable("eventId") Long eventId) {
        eventService.deleteEvent(eventId);
        return "redirect:/events";
    }

    private UserEntity getUser() {
        String username = SecurityUtil.getSessionUser();
        return username != null ? userService.findByUsername(username) : new UserEntity();
    }
}
