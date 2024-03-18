package com.rollcall.web.controller;

import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.mapper.EventMapper;
import com.rollcall.web.mapper.GroupMapper;
import com.rollcall.web.models.*;
import com.rollcall.web.models.UserCommentDto;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.CommentService;
import com.rollcall.web.services.EventService;
import com.rollcall.web.services.GroupService;
import com.rollcall.web.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final EventService eventService;
    private final GroupService groupService;
    private final UserService userService;


    public CommentController(CommentService commentService, EventService eventService, GroupService groupService, UserService userService) {
        this.commentService = commentService;
        this.eventService = eventService;
        this.groupService = groupService;
        this.userService = userService;
    }

//    @GetMapping
//    public List<UserComment> retrieveComments() {
//        return null;
//    }
//
////    @GetMapping("/groups/{groupId}/newcomment")
////    public String createGroupCommentForm(Model model) {
////        UserComment comment = new UserComment();
////        model.addAttribute("comment", comment);
////        return "comment-create";
////    }

    @GetMapping("/events/{eventId}/newcomment")
    public String createEventsCommentForm(@PathVariable("eventId") Long eventId, Model model) {
        UserComment comment = new UserComment();
        model.addAttribute("eventId", eventId);
        model.addAttribute("comment", comment);
        model.addAttribute("resourceType", "events");
        model.addAttribute("resourceId", eventId);
        return "comment-create";
    }

    @GetMapping("/groups/{groupId}/newcomment")
    public String createGroupCommentForm(@PathVariable("groupId") Long groupId, Model model) {
        UserComment comment = new UserComment();
        model.addAttribute("groupId", groupId);
        model.addAttribute("comment", comment);
        model.addAttribute("resourceType", "groups");
        model.addAttribute("resourceId", groupId);
        return "comment-create";
    }


    @PostMapping("/events/{eventId}/newcomment")
    public String createEventComment(@PathVariable("eventId") Long eventId, UserCommentDto userCommentDto) {

        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        user = userService.findByUsername(username);
        Event newEvent = EventMapper.mapToEvent(eventService.findByEventId(eventId));
        userCommentDto.setUser(user);
        userCommentDto.setEvent(newEvent);
        commentService.addComment(userCommentDto);
        return "redirect:/events/" + eventId +"?success";
    }

    @PostMapping("/groups/{groupId}/newcomment")
    public String createGroupComment(@PathVariable("groupId") Long groupId, UserCommentDto userCommentDto) {

        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        user = userService.findByUsername(username);
        Group newGroup = GroupMapper.mapToGroup(groupService.findGroupById(groupId));
        userCommentDto.setUser(user);
        userCommentDto.setGroup(newGroup);
        commentService.addComment(userCommentDto);
        return "redirect:/groups/" + groupId +"?success";
    }
}


