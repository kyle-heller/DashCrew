package com.rollcall.web.services;

import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.Group;

import java.util.List;
public interface GroupService {
    List<GroupDto> findAllGroups();

    Group saveGroup(GroupDto groupDto);

    

    GroupDto findGroupById(long groupId);

    void updateGroup(GroupDto group);

    void deleteGroup(Long groupId);

    List<GroupDto> searchGroups(String query);
}
