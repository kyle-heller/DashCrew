package com.rollcall.web.services.external;

import com.rollcall.web.models.UserEntity;
import com.rollcall.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService {

    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new DefaultOAuth2UserService() {
            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) {
                OAuth2User oAuth2User = super.loadUser(userRequest);

                // Extract user information from OAuth2User
                String email = oAuth2User.getAttribute("email");
                String name = oAuth2User.getAttribute("name");

                // Attempt to find user by email
                UserEntity user = userRepository.findByEmail(email);

                // Check if user was found or create a new instance
                if (user == null) {
                    user = new UserEntity();
                    user.setEmail(email);
                }

                user.setUsername(name);
                userRepository.save(user);

                return oAuth2User;
            }
        };
    }
}
