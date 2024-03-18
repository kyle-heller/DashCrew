package com.rollcall.web.controller;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.dto.GameDto;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.dto.UserProfileDto;
import com.rollcall.web.mapper.UserProfileMapper;
import com.rollcall.web.models.Event;
import com.rollcall.web.models.UserCommentDto;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.models.UserProfile;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final GroupService groupService;
    private final GameService gameService;

    private final CommentService commentService;


    public EventController(EventService eventService, UserService userService, GroupService groupService, GameService gameService, CommentService commentService) {
        this.eventService = eventService;
        this.userService = userService;
        this.groupService = groupService;
        this.gameService = gameService;
        this.commentService = commentService;
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
    public String viewEvent(@PathVariable("eventId") Long eventId, Model model, Principal principal) {
        EventDto eventDto = eventService.findByEventId(eventId);
        List<UserCommentDto> comments = commentService.findEventCommentById(eventId);
        boolean isUserJoined = false;
        if (principal != null) {
            isUserJoined = userService.isUserJoinedEvent(principal.getName(), eventId);
        }
        Map<Long, UserProfileDto> userAvatars = new HashMap<>();

        for (UserCommentDto comment : comments) {
            UserEntity userEntity = comment.getUser();
            if (userEntity != null && userEntity.getProfile() != null) {
                Long userId = userEntity.getId();
                UserProfileDto userProfile = UserProfileMapper.mapToUserProfileDto(userEntity.getProfile());
                userAvatars.put(userId, userProfile);
            }
        }
        System.out.println(userAvatars);
        UserEntity currentUser = getUser();

        model.addAttribute("group", eventDto.getGroup());
        model.addAttribute("event", eventDto);
        model.addAttribute("user", currentUser);
        model.addAttribute("comments", comments);
        model.addAttribute("userAvatars", userAvatars);
        model.addAttribute("resourceType", "events");
        model.addAttribute("resourceId", eventId);
        model.addAttribute("isUserJoined", isUserJoined);


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

    @PostMapping("/events/join")
    public String joinEvent(@RequestParam("eventId") Long eventId, Principal principal, RedirectAttributes redirectAttributes) {
        // Principal is used here to get the current logged-in user's information.
        // Your logic to determine whether to join or unjoin the event goes here.
        // For demonstration, let's assume a service method does this:
        UserEntity user = userService.findByUsername(principal.getName());
        eventService.toggleUserParticipationInEvent(user.getId(), eventId);
        // Redirect to avoid double submission and provide feedback to the user.
        redirectAttributes.addFlashAttribute("message", "Successfully updated your participation status.");
        return "redirect:/events/" + eventId;
    }


    private UserEntity getUser() {
        String username = SecurityUtil.getSessionUser();
        return username != null ? userService.findByUsername(username) : new UserEntity();
    }
}
