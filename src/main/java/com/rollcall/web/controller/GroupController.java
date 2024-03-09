package com.rollcall.web.controller;

import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Group;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.GroupService;
import com.rollcall.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
public class GroupController {
    private GroupService groupService;
    private UserService userService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.userService = userService;
        this.groupService = groupService;
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
    public String groupDetail(@PathVariable("groupId") Long groupId, Model model) {
        UserEntity user = new UserEntity();
        GroupDto groupDto = groupService.findClubById(groupId);
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("group", groupDto);
        return "groups-detail";
    }

    @GetMapping("/groups/{groupId}/edit")
    public String editGroupForm(@PathVariable("groupId") long groupId, Model model) {
        GroupDto group = groupService.findClubById(groupId);
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
}
