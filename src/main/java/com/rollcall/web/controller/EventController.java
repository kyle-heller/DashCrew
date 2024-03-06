package com.rollcall.web.controller;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.models.Event;
import com.rollcall.web.services.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EventController {
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events/{groupId}/new")
    public String createEventForm(@PathVariable("groupId") Long groupId, Model model) {
        Event event = new Event();
        model.addAttribute("groupId", groupId);
        model.addAttribute("event", event);
        return "events-create";
    }

    @PostMapping("/events/{groupId}")
    public String createEvent(@PathVariable("groupId") Long groupId, @ModelAttribute("event") EventDto eventDto, Model model) {
        eventService.createEvent(groupId, eventDto);
        return "redirect:/groups/" + groupId;
    }

    @GetMapping("/events")
    public String eventList(Model model) {
        List<EventDto> events = eventService.findAllEvents();
        model.addAttribute("events", events);
        return "events-list";
    }
}
