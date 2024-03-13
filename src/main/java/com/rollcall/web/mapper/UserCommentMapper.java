package com.rollcall.web.mapper;

import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Group;
import com.rollcall.web.models.UserComment;
import com.rollcall.web.models.UserCommentDto;

import java.util.stream.Collectors;

import static com.rollcall.web.mapper.EventMapper.mapToEventDto;

public class UserCommentMapper {
    public static UserComment mapToUserComment(UserCommentDto userCommentDto) {
        return UserComment.builder()
                .id(userCommentDto.getId())
                .content(userCommentDto.getContent())
                .group(userCommentDto.getGroup())
                .event(userCommentDto.getEvent())
                .user(userCommentDto.getUser())
                .build();
    }

    public static UserCommentDto mapToUserCommentDto(UserComment userComment) {

        return UserCommentDto.builder()
                .id(userComment.getId())
                .content(userComment.getContent())
                .group(userComment.getGroup())
                .event(userComment.getEvent())
                .user(userComment.getUser())
                .build();
    }


}
