package com.rollcall.web.mapper;

import com.rollcall.web.dto.UserProfileDto;
import com.rollcall.web.models.UserProfile;

public class UserProfileMapper {

    public static UserProfile mapToUserProfile(UserProfileDto userProfileDto) {
        return UserProfile.builder()
                .Id(userProfileDto.getId())
                .aboutMe(userProfileDto.getAboutMe())
                .interests(userProfileDto.getInterests())
                .zip(userProfileDto.getZip())
                .photoURL(userProfileDto.getPhotoURL())
                .darkMode(userProfileDto.isDarkMode())
                .build();
    }

    public static UserProfileDto mapToUserProfileDto(UserProfile userProfile) {
        return UserProfileDto.builder()
                .Id(userProfile.getId())
                .aboutMe(userProfile.getAboutMe())
                .interests(userProfile.getInterests())
                .zip(userProfile.getZip())
                .photoURL(userProfile.getPhotoURL())
                .darkMode(userProfile.isDarkMode())
                .build();
    }
}
