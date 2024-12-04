package com.example.datadynamo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Save a new event
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // Find events by assigned employee
    public List<Event> getEventsByAssignedEmployee(String assignedEmployee) {
        return eventRepository.findByAssignedEmployee(assignedEmployee);
    }
}
