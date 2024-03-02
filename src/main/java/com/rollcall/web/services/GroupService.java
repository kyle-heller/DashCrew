package com.rollcall.web.services;

import com.rollcall.web.dto.GroupDto;
import org.springframework.stereotype.Service;

import java.util.List;
public interface GroupService {
    List<GroupDto> findAllGroups();
}
