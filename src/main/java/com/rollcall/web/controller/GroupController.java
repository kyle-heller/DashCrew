package com.rollcall.web.controller;

import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GroupController {
    private GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groups")
    public String listGroups(Model model) {
        List<GroupDto> groups = groupService.findAllGroups();
        model.addAttribute("groups", groups);
        return "groups-list";
    }
}
