package com.rollcall.web.services;
import com.rollcall.web.dto.EventDto;

import java.util.List;

public interface EventService {
    void createEvent(Long groupId, EventDto eventDto);

    List<EventDto> findAllEvents();

    EventDto findByEventId(Long eventId);
}
