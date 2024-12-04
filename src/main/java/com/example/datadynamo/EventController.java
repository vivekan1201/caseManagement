package com.example.datadynamo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Create a new event
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.saveEvent(event);
    }

    // Get events by assigned employee
    @GetMapping("/employee/{assignedEmployee}")
    public List<Event> getEventsByAssignedEmployee(@PathVariable String assignedEmployee) {
        return eventService.getEventsByAssignedEmployee(assignedEmployee);
    }
}
