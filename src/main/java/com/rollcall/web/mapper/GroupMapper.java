package com.rollcall.web.mapper;

import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Group;

import java.util.stream.Collectors;

import static com.rollcall.web.mapper.EventMapper.mapToEventDto;

public class GroupMapper {
    public static Group mapToGroup(GroupDto group) {
        Group groupDto = Group.builder()
                .id(group.getId())
                .title(group.getTitle())
                .content(group.getContent())
                .photoURL(group.getPhotoURL())
                .createdBy(group.getCreatedBy())
                .createdOn(group.getCreatedOn())
                .updatedOn(group.getUpdatedOn())
                .build();
        return groupDto;
    }

    public static GroupDto mapToGroupDto(Group group) {
        GroupDto groupDto = GroupDto.builder()
                .id(group.getId())
                .title(group.getTitle())
                .photoURL(group.getPhotoURL())
                .content(group.getContent())
                .createdBy(group.getCreatedBy())
                .createdOn(group.getCreatedOn())
                .updatedOn(group.getUpdatedOn())
                .events(group.getEvents().stream().map((event) -> mapToEventDto(event)).collect(Collectors.toList()))
                .build();
        return groupDto;
    }


}
