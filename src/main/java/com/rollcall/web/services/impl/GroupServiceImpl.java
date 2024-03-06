package com.rollcall.web.services.impl;

import com.rollcall.web.models.Group;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.repository.GroupRepository;
import com.rollcall.web.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.rollcall.web.mapper.GroupMapper.mapToGroup;
import static com.rollcall.web.mapper.GroupMapper.mapToGroupDto;

@Service
public class GroupServiceImpl implements GroupService {
    private GroupRepository groupRepository;


    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupDto> findAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(group -> mapToGroupDto(group) ).collect(Collectors.toList());
    }

    @Override
    public Group saveGroup(GroupDto groupDto) {
        Group group = mapToGroup(groupDto);
        groupRepository.save(group);
        return null;
    }

    @Override
    public GroupDto findClubById(long groupId) {
        Group group = groupRepository.findById(groupId).get();
        return mapToGroupDto(group);
    }

    @Override
    public void updateGroup(GroupDto groupDto) {
        Group group = mapToGroup(groupDto);
        groupRepository.save(group);
    }

    @Override
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    public List<GroupDto> searchGroups(String query) {
        List<Group> groups = groupRepository.searchGroups( query);
        return groups.stream().map(group -> mapToGroupDto(group)).collect(Collectors.toList());
    }

}
