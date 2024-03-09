package com.rollcall.web.services.impl;

import com.rollcall.web.dto.RegistrationDto;
import com.rollcall.web.dto.UserProfileDto;
import com.rollcall.web.models.Role;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.models.UserProfile;
import com.rollcall.web.repository.ProfileRepository;
import com.rollcall.web.repository.RoleRepository;
import com.rollcall.web.repository.UserRepository;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override // Registers a new user with details from RegistrationDto, encodes the password, assigns the USER role, and saves to the database
    public void saveUser(RegistrationDto registrationDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        Role role = roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(role));
        UserProfile userProfile = new UserProfile();
        // Links the user and profile
        user.setProfile(userProfile);
        userProfile.setUser(user);
        userRepository.save(user);
    }

    @Override // Finds a user by their email and returns the UserEntity, or null if not found
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override // Finds a user by their username and returns the UserEntity, or null if not found
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateUserProfile(UserProfileDto userProfileDto) {
        String username = SecurityUtil.getSessionUser(); // Assuming you have a way to retrieve the current user's username
        UserEntity user = userRepository.findByUsername(username); // Assuming you have a repository for managing UserEntity objects
        if (user != null) {
            UserProfile userProfile = user.getProfile();
            if (userProfile == null) {
                userProfile = new UserProfile();
            }
            // Update the UserProfile object with data from UserProfileDto
            userProfile.setAboutMe(userProfileDto.getAboutMe());
            userProfile.setInterests(userProfileDto.getInterests());
            userProfile.setPhotoURL(userProfileDto.getPhotoURL());
            userProfile.setDarkMode(userProfileDto.isDarkMode());
            userProfile.setZip(userProfileDto.getZip());

            // Set the updated profile back to the user entity
            user.setProfile(userProfile);

            // Save the updated user entity
            userRepository.save(user); // Assuming you have a method in userRepository to save the user entity
        } else {
            // Handle the case where the user is not found
            throw new RuntimeException("User not found");
        }
    }
}