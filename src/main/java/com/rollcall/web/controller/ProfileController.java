package com.rollcall.web.controller;

import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class ProfileController {

    private final UserService userService;


    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{username}/profile")
    public String showProfile(@PathVariable("username") String username, Model model) {
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "error"; // Or any appropriate error handling mechanism
        }
        model.addAttribute("user", user);
        return "profile-details";
    }


//    @GetMapping("/user/{userId}/profile")
//    public String listGroups(Model model) {
//        UserEntity user = new UserEntity();
//        List<GroupDto> groups = groupService.findAllGroups();
//        String username = SecurityUtil.getSessionUser();
//        if(username != null) {
//            user = userService.findByUsername(username);
//            model.addAttribute("user", user);
//        }
//        model.addAttribute("user", user);
//        model.addAttribute("groups", groups);
//        return "groups-list";
//    }
}
