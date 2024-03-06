package com.rollcall.web.services;

import com.rollcall.web.dto.RegistrationDto;
import com.rollcall.web.models.UserEntity;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
}