package com.rollcall.web.controller;

import com.rollcall.web.dto.RegistrationDto;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/ssl-test")
    public String inbound(){
        return "Inbound TLS is working!!";
    }

    @GetMapping(value = "/exit")
    public void exit() {
        System.exit(0);
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDto user,
                           BindingResult result, Model model) {
        UserEntity existingUserEmail = userService.findByEmail(user.getEmail());
        if (existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }
        UserEntity existingUserUsername = userService.findByUsername(user.getUsername());
        if (existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/groups?success";
    }

}