package com.rollcall.web.dto;

import com.rollcall.web.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private long Id;
    private String aboutMe;
    private String interests;
    private String photoURL;
    private String selectedAvatar;
    private int zip;
    private String city;
    private String state;

}
