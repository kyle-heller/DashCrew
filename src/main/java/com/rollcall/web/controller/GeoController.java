package com.rollcall.web.controller;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.dto.GameDto;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Game;
import com.rollcall.web.models.Group;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.EventService;
import com.rollcall.web.services.GeolocationService;
import com.rollcall.web.services.GroupService;
import com.rollcall.web.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class GeoController {
    private final GroupService groupService;
    private final EventService eventService;
    private final UserService userService;
    private final GeolocationService geolocationService;

    public GeoController(GroupService groupService, EventService eventService, UserService userService, GeolocationService geolocationService) {
        this.groupService = groupService;
        this.eventService = eventService;
        this.userService = userService;
        this.geolocationService = geolocationService;
    }


    @GetMapping("/get/zips")
    @ResponseBody
    public ResponseEntity<?> getClosestZips(@RequestParam(required = false) String filterType, Model model) {
        UserEntity user = getUser();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        List<Integer> closestZips = geolocationService.findClosestZips(user.getProfile().getZip());

        switch (filterType) {
            case "events":
                List<EventDto> events = eventService.findEventsByZipCode(closestZips);
                model.addAttribute("events", events);
                return ResponseEntity.ok().body(events);
            case "groups":
                List<GroupDto> groups = groupService.findGroupsByZipCode(closestZips);
                model.addAttribute("groups", groups);
                return ResponseEntity.ok().body(groups);
            default:
                break;
        }
        return ResponseEntity.ok().body(null);
    }

    private UserEntity getUser() {
        String username = SecurityUtil.getSessionUser();
        return username != null ? userService.findByUsername(username) : new UserEntity();
    }
}
