package services;

import com.rollcall.web.dto.GroupDto;

import java.util.List;

public interface GroupService {
    List<GroupDto> findAllGroups();
}
