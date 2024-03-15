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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);
        if (user != null) {
            UserProfile userProfile = user.getProfile();
            if (userProfile == null) {
                userProfile = new UserProfile();
            }
            userProfile.setAboutMe(userProfileDto.getAboutMe());
            userProfile.setInterests(userProfileDto.getInterests());
            userProfile.setPhotoURL(userProfileDto.getPhotoURL());
            userProfile.setZip(userProfileDto.getZip());
            userProfile.setCity(userProfileDto.getCity());
            userProfile.setState(userProfileDto.getState());

            profileRepository.save(userProfile);

            // Save the updated user entity
            userRepository.save(user);
        } else {
            // Handle the case where the user is not found
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public boolean isUserJoinedEvent(String username, Long eventId) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.getEvents().stream()
                .anyMatch(event -> event.getId().equals(eventId));
    }

    @Override
    public boolean isUserJoinedGroup(String username, Long groupId) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.getGroups().stream()
                .anyMatch(group -> group.getId().equals(groupId));
    }

}