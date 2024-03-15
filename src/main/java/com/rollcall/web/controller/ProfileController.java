package com.rollcall.web.controller;

import com.rollcall.web.dto.UserProfileDto;
import com.rollcall.web.mapper.UserProfileMapper;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.models.UserProfile;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.impl.AvatarService;
import com.rollcall.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
public class ProfileController {

    private final UserService userService;
    @Autowired
    private AvatarService avatarService; // Assuming you have a service to fetch avatar filenames



    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{username}")
    public String showProfile(@PathVariable("username") String username, Model model) {
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "error"; // Or any appropriate error handling mechanism
        }
        UserProfile profile = user.getProfile();

        if (profile == null) {
            // Handle the case where profile is not found
            return "error"; // Or any appropriate error handling mechanism
        }
        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        return "profile-details";
    }

    @GetMapping("/users/profile")
    public String showMyProfile(Model model) {
        String currentUser = SecurityUtil.getSessionUser();
        if(currentUser != null) {
            model.addAttribute("user", currentUser);
            return "redirect:/users/" + currentUser;
        }
        return "index";
    }

    @GetMapping("/profile/edit")
    public String showProfileEditForm(Model model) {
        UserProfile userProfile = getProfile();
        if (userProfile == null) {
            return "error"; // Handle the case where the profile is not found
        }
        List<String> avatarFiles = avatarService.getAvatarFiles();


        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setAboutMe(userProfile.getAboutMe());
        userProfileDto.setInterests(userProfile.getInterests());
        userProfileDto.setPhotoURL(userProfile.getPhotoURL());
        userProfileDto.setZip(userProfile.getZip());
        userProfileDto.setCity(userProfile.getCity());
        userProfileDto.setState(userProfile.getState());

        model.addAttribute("avatarFiles", avatarFiles);
        model.addAttribute("userProfileDto", userProfileDto);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String processUpdateProfile(@ModelAttribute("userProfileDto") @Valid UserProfileDto updatedProfileDto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile-edit"; // If there are validation errors, return back to the edit form
        }

        UserProfile userProfile = getProfile();
        if (userProfile == null) {
            return "error";
        }

        if (!updatedProfileDto.getPhotoURL().equals("Blank")) {
            String photoURL = "http://localhost/assets/avatars/";
            updatedProfileDto.setPhotoURL(photoURL + updatedProfileDto.getPhotoURL());
        }
        else {
            updatedProfileDto.setPhotoURL(userProfile.getPhotoURL());
            System.out.println(userProfile.getPhotoURL());
        }

        userService.updateUserProfile(updatedProfileDto);

        return "redirect:/users/" + SecurityUtil.getSessionUser();
    }



    private UserEntity getUser() {
        String username = SecurityUtil.getSessionUser();
        return username != null ? userService.findByUsername(username) : new UserEntity();
    }

    private UserProfile getProfile() {
        String username = SecurityUtil.getSessionUser();
        UserEntity currentUser = username != null ? userService.findByUsername(username) : null;
        return currentUser != null ? currentUser.getProfile() : null;
    }

}
