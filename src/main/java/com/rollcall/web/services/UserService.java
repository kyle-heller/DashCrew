package com.rollcall.web.services;

import com.rollcall.web.dto.RegistrationDto;
import com.rollcall.web.dto.UserProfileDto;
import com.rollcall.web.models.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    void updateUserProfile(UserProfileDto userProfileDto);

    boolean isUserJoinedEvent(String username, Long eventId);

    boolean isUserJoinedGroup(String username, Long groupId);
}