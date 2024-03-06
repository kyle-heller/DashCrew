package com.rollcall.web.services.impl;

import com.rollcall.web.dto.EventDto;
import com.rollcall.web.models.Event;
import com.rollcall.web.models.Group;
import com.rollcall.web.repository.EventRepository;
import com.rollcall.web.repository.GroupRepository;
import com.rollcall.web.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.rollcall.web.mapper.EventMapper.mapToEvent;
import static com.rollcall.web.mapper.EventMapper.mapToEventDto;

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

    @Override
    public List<EventDto> findAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(event -> mapToEventDto(event)).collect(Collectors.toList());
    }

    @Override
    public EventDto findByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId).get();
        return mapToEventDto(event);
    }

    @Override
    public void updateEvent(EventDto eventDto) {
        Event event = mapToEvent(eventDto);
        eventRepository.save(event);
    }


    @Override
    public void deleteEvent(long eventId) {
        eventRepository.deleteById(eventId);
    }

}
