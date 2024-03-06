package com.rollcall.web.services;
import com.rollcall.web.dto.EventDto;

public interface EventService {
    void createEvent(Long groupId, EventDto eventDto);
}
