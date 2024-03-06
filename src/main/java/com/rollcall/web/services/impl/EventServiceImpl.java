package com.rollcall.web.services.impl;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.models.Event;
import com.rollcall.web.models.Group;
import com.rollcall.web.repository.EventRepository;
import com.rollcall.web.repository.GroupRepository;
import com.rollcall.web.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private GroupRepository groupRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, GroupRepository groupRepository) {
        this.eventRepository = eventRepository;
        this.groupRepository = groupRepository;
    }
    @Override
    public void createEvent(Long groupId, EventDto eventDto) {
        Group group = groupRepository.findById(groupId).get();
        Event event = mapToEvent(eventDto);
        event.setGroup(group);
        eventRepository.save(event);
    }

    private Event mapToEvent(EventDto eventDto) {
        return Event.builder()
                .id(eventDto.getId())
                .name(eventDto.getName())
                .startTime(eventDto.getStartTime())
                .endTime(eventDto.getEndTime())
                .type(eventDto.getType())
                .photoURL(eventDto.getPhotoURL())
                .createdOn(eventDto.getCreatedOn())
                .updatedOn(eventDto.getUpdatedOn())
                .build();
    }
}
