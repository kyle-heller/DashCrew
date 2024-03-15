package com.rollcall.web.services.impl;

import com.rollcall.web.models.Event;
import com.rollcall.web.models.Group;
import com.rollcall.web.dto.GroupDto;
import com.rollcall.web.models.UserEntity;
import com.rollcall.web.repository.GroupRepository;
import com.rollcall.web.repository.UserRepository;
import com.rollcall.web.security.SecurityUtil;
import com.rollcall.web.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.rollcall.web.mapper.GroupMapper.mapToGroup;
import static com.rollcall.web.mapper.GroupMapper.mapToGroupDto;
@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override // Retrieves all group entities from the database, converts them to GroupDto objects, and returns them as a list
    public List<GroupDto> findAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(group -> mapToGroupDto(group) ).collect(Collectors.toList());
    }

    @Override // Saves a new group to the database using the provided GroupDto, sets the current logged-in user as the creator
    public Group saveGroup(GroupDto groupDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);
        Group group = mapToGroup(groupDto);
        group.setCreatedBy(user);
        groupRepository.save(group);
        return null;
    }

    @Override // Updates an existing group with the details from the provided GroupDto, includes setting the creator based on the current user
    public void updateGroup(GroupDto groupDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);
        Group group = mapToGroup(groupDto);
        group.setCreatedBy(user);
        groupRepository.save(group);
    }

    @Override // Finds a single group by its ID, converts it to GroupDto, and returns it. Throws an exception if the group is not found
    public GroupDto findGroupById(long groupId) {
        Group group = groupRepository.findById(groupId).get();
        return mapToGroupDto(group);
    }


    @Override // Deletes the group identified by the provided groupId from the database
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override // Searches for groups matching the given query string and returns a list of matching GroupDto objects
    public List<GroupDto> searchGroups(String query) {
        List<Group> groups = groupRepository.searchGroups(query);
        return groups.stream().map(group -> mapToGroupDto(group)).collect(Collectors.toList());
    }
    public void toggleUserParticipationInGroup(Long userId, Long groupId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        // Check if the user is already participating in the event
        boolean isParticipating = user.getGroups().contains(group);

        if (isParticipating) {
            // If the user is already participating, remove them from the event
            user.getGroups().remove(group);
        } else {
            // If the user is not already participating, add them to the event
            user.getGroups().add(group);
        }
        userRepository.save(user); // Save the user entity back to the database
    }

}
