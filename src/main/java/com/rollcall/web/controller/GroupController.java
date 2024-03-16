package com.rollcall.web.controller;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.dto.UserProfileDto;
import com.rollcall.web.mapper.UserProfileMapper;
import com.rollcall.web.models.Group;
import com.rollcall.web.models.UserCommentDto;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.CommentService;
import com.rollcall.web.services.GroupService;
import com.rollcall.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GroupController {
    private GroupService groupService;
    private UserService userService;
    private CommentService commentService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService, CommentService commentService) {
        this.userService = userService;
        this.groupService = groupService;
        this.commentService = commentService;
    }

    @GetMapping("/groups")
    public String listGroups(Model model) {
        UserEntity user = new UserEntity();
        List<GroupDto> groups = groupService.findAllGroups();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("groups", groups);
        return "groups-list";
    }

    @GetMapping("/groups/new")
    public String createGroupForm(Model model) {
        Group group = new Group();
        model.addAttribute("group", group);
        return "groups-create";
    }

    @PostMapping("groups/new")
    public String saveGroupNew(@Valid @ModelAttribute("group") GroupDto groupDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("group",groupDto);
        }
        groupService.saveGroup(groupDto);
        return "redirect:/groups?addsuccess";
    }

    @GetMapping("/groups/{groupId}")
    public String groupDetail(@PathVariable("groupId") Long groupId, Model model, Principal principal) {
        GroupDto groupDto = groupService.findGroupById(groupId);
        List<UserCommentDto> comments = commentService.findGroupCommentById(groupId);
        boolean isUserJoined = userService.isUserJoinedGroup(principal.getName(), groupId);
        Map<Long, UserProfileDto> userAvatars = new HashMap<>();
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        for (UserCommentDto comment : comments) {
            UserEntity userEntity = comment.getUser();
            if (userEntity != null && userEntity.getProfile() != null) {
                Long userId = userEntity.getId();
                UserProfileDto userProfile = UserProfileMapper.mapToUserProfileDto(userEntity.getProfile());
                userAvatars.put(userId, userProfile);
            }
        }
        model.addAttribute("comments", comments);
        model.addAttribute("userAvatars", userAvatars);
        model.addAttribute("user", user);
        model.addAttribute("group", groupDto);
        model.addAttribute("resourceType", "groups");
        model.addAttribute("resourceId", groupId);
        model.addAttribute("isUserJoined", isUserJoined);
        return "groups-detail";
    }


    @PostMapping("/groups/join")
    public String joinGroup(@RequestParam("groupId") Long groupId, Principal principal, RedirectAttributes redirectAttributes) {
        UserEntity user = userService.findByUsername(principal.getName());
        groupService.toggleUserParticipationInGroup(user.getId(), groupId);
        redirectAttributes.addFlashAttribute("message", "Successfully updated your participation status.");
        return "redirect:/groups/" + groupId;
    }

    @GetMapping("/groups/{groupId}/edit")
    public String editGroupForm(@PathVariable("groupId") long groupId, Model model) {
        GroupDto group = groupService.findGroupById(groupId);
        model.addAttribute("group", group);
        return "groups-edit";
    }

    @PostMapping("/groups/{groupId}/edit")
    public String updateGroup(@PathVariable("groupId") Long groupId,
                              @Valid @ModelAttribute("group") GroupDto group,
                              BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("group", group);
            return "groups-edit";
        }
        group.setId(groupId);
        groupService.updateGroup(group);
        return "redirect:/groups";
    }

    @GetMapping("groups/{groupId}/delete")
    public String deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteGroup(groupId);
        return "redirect:/groups";
    }

    @GetMapping("/groups/search")
    public String searchGroups(@RequestParam(value = "query") String query, Model model) {
        List<GroupDto> groups = groupService.searchGroups(query);

        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);

        model.addAttribute("groups", groups);
        return "groups-list";
    }

    private UserEntity getUser() {
        String username = SecurityUtil.getSessionUser();
        return username != null ? userService.findByUsername(username) : new UserEntity();
    }
}
