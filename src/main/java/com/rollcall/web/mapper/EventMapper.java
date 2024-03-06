package com.rollcall.web.mapper;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.models.Event;

public class EventMapper {

    public static Event mapToEvent(EventDto eventDto) {
        return Event.builder()
                .id(eventDto.getId())
                .name(eventDto.getName())
                .startTime(eventDto.getStartTime())
                .endTime(eventDto.getEndTime())
                .type(eventDto.getType())
                .photoURL(eventDto.getPhotoURL())
                .createdOn(eventDto.getCreatedOn())
                .updatedOn(eventDto.getUpdatedOn())
                .group(eventDto.getGroup())
                .build();
    }

    public static EventDto mapToEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .type(event.getType())
                .photoURL(event.getPhotoURL())
                .createdOn(event.getCreatedOn())
                .updatedOn(event.getUpdatedOn())
                .group(event.getGroup())
                .build();
    }

}
